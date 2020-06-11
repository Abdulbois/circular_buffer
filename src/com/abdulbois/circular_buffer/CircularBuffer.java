package com.abdulbois.circular_buffer;


public interface CircularBuffer<T> {
    void append(T[] items);
    void prepend(T[] items);
    T get(int index);
    void clear();
    int getCount();
    boolean isFull();
    boolean isEmpty();

}
