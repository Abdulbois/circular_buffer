package com.abdulbois.circular_buffer;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CircularBufferImpl<T> implements CircularBuffer<T>, Iterable<T>{

    private final Object[] data;
    private final int size;
    private int headPosition = 1;
    private int tailPosition = -1;
    private int freeSpaces;
    private final ReadWriteLock rw_lock = new ReentrantReadWriteLock(true);
    private final Lock r_lock = rw_lock.readLock();
    private final Lock w_lock = rw_lock.writeLock();

    public CircularBufferImpl(int maxSize){
        this.data = new Object[maxSize];
        this.size = maxSize;
        this.freeSpaces = maxSize;

    }

    private void validateInputData(T[] items){
        if (items.length > this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void updateFreeSpaces(int usedSpaces){
        if (this.freeSpaces > 0) {
            if (this.freeSpaces >= usedSpaces) {
                this.freeSpaces = this.freeSpaces - usedSpaces;
            } else {
                this.freeSpaces = 0;
            }
        }
    }

    @Override
    public void append(T[] items) throws IndexOutOfBoundsException {
        w_lock.lock();
        try {
            validateInputData(items);
            for (T item: items) {
                this.tailPosition = (this.tailPosition + 1) % this.size;
                this.data[tailPosition] = item;
            }
            updateFreeSpaces(items.length);
            this.headPosition = (this.freeSpaces + this.tailPosition + 1) % this.size;
        } finally {
            w_lock.unlock();
        }

    }

    @Override
    public void prepend(T[] items) throws IndexOutOfBoundsException {
        w_lock.lock();
        try {
            validateInputData(items);
            for (int i = items.length - 1; i > -1; i--) {
                this.headPosition = Math.floorMod(this.headPosition - 1, this.size);
                this.data[headPosition] = items[i];
            }
            updateFreeSpaces(items.length);
            this.tailPosition = Math.floorMod(this.headPosition - this.freeSpaces - 1, this.size);
        } finally {
            w_lock.unlock();
        }
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException{
        r_lock.lock();
        try {
            if (getCount() == 0 || index >= getCount() || index < 0) {
                throw new IndexOutOfBoundsException();
            } else {
                index = Math.floorMod(this.headPosition + index, this.size);
                return (T) data[index];
            }
        } finally {
            r_lock.unlock();
        }
    }

    @Override
    public void clear() {
        w_lock.lock();
        try {
            this.headPosition = 1;
            this.tailPosition = -1;
            this.freeSpaces = this.size;
        } finally {
            w_lock.unlock();
        }
    }

    @Override
    public int getCount() {
        return this.size - this.freeSpaces;
    }

    @Override
    public boolean isFull() {
        return getCount() == this.size;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularBufferIterator(this);
    }

    private class CircularBufferIterator implements Iterator<T>{
        int cursor = 0;
        int lastRet = -1;
        CircularBuffer<T> buffer;

        public CircularBufferIterator(CircularBuffer<T> buffer){
            this.buffer = buffer;
        }

        @Override
        public boolean hasNext() {
            return cursor != this.buffer.getCount();
        }

        @Override
        public T next() {
            cursor++;
            return this.buffer.get(lastRet+cursor);
        }
    }
}

