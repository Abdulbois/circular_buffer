package com.abdulbois.circular_buffer;

public class Utils {

    public static Integer[] createRange(int start, int end){
        Integer [] arr = new Integer[end - start + 1];
        for (int i=start, j=0; i <= end; i++, j++){
            arr[j] = i;
        }
        return arr;
    }
}
