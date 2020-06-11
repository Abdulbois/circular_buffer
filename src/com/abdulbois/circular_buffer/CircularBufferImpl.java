package com.abdulbois.circular_buffer;

public class CircularBufferImpl<T> implements CircularBuffer<T>{

    private final Object[] data;
    private final int size;
    private int headPosition = 1;
    private int tailPosition = -1;
    private int freeSpaces = 0;

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
        validateInputData(items);
        for (T item: items) {
            this.tailPosition = (this.tailPosition + 1) % this.size;
            this.data[tailPosition] = item;
        }
        updateFreeSpaces(items.length);
        this.headPosition = (this.freeSpaces + this.tailPosition + 1) % this.size;
    }

    @Override
    public void prepend(T[] items) throws IndexOutOfBoundsException{
        validateInputData(items);
        for (int i = items.length - 1; i > -1; i--) {
            this.headPosition = Math.floorMod(this.headPosition - 1, this.size);
            this.data[headPosition] = items[i];
        }
        updateFreeSpaces(items.length);
        this.tailPosition = Math.floorMod(this.headPosition - this.freeSpaces - 1, this.size);
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException{
        if (getCount() == 0 || index >= getCount() || index < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            index = Math.floorMod(this.headPosition + index, this.size);
            return (T) data[index];
        }
    }

    @Override
    public void clear() {
        this.headPosition = 1;
        this.tailPosition = -1;
        this.freeSpaces = this.size;
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
}
