package com.abdulbois.circular_buffer;

public class CircularBufferImpl<T> implements CircularBuffer<T>{

    private Object[] data;
    private int size;
    private int headPosition;
    private int tailPosition;

    public CircularBufferImpl(int maxSize){
        this.data = new Object[maxSize];
        size = maxSize;
    }

    @Override
    public void append(T[] items) {
        if (items.length >= 1 && items.length < size) System.arraycopy(items, 0, data, 0, items.length);
    }

    @Override
    public void prepend(T[] items) {

    }

    @Override
    public T get(int index) {
        return (T) data[index];
    }

    @Override
    public void clear() {

    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
