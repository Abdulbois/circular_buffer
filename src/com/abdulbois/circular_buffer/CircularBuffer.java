package com.abdulbois.circular_buffer;

import java.util.Iterator;

public interface CircularBuffer<T> {
    public void append(T[] items);
    public void prepend(T[] items);
    public T get(int index);
    public void clear();
    public int getCount();
    public boolean isFull();
    public boolean isEmpty();

}
