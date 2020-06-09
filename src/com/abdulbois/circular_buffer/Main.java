package com.abdulbois.circular_buffer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var list = new CircularBufferImpl<Integer>(5);
//        String [] arr = {"A", "B", "C", "D"};
        Integer [] arr = {1, 2, 3, 4, 5};
        var size = arr.length;
        list.append(arr);
        System.out.print(list.get(1));
    }
}
