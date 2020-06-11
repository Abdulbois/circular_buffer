package com.abdulbois.circular_buffer;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CircularBufferImplTest {

    private CircularBufferImpl<Integer> circularBuffer;
    private Integer [] integerList;

    @BeforeEach
    void setUp() {
        circularBuffer = new CircularBufferImpl<>(10);
        integerList = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13};

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void append() {
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 4));
        assertEquals(0, circularBuffer.get(0));
        assertEquals(3, circularBuffer.get(3));
        circularBuffer.append(Arrays.copyOfRange(integerList, 4, 11));
        assertEquals(1, circularBuffer.get(0));
        assertEquals(10, circularBuffer.get(9));
        circularBuffer.append(Arrays.copyOfRange(integerList, 11, 14));
        assertEquals(4, circularBuffer.get(0));
        assertEquals(13, circularBuffer.get(9));
        assertEquals(10, circularBuffer.get(6));
        assertEquals(9, circularBuffer.get(5));
        assertEquals(5, circularBuffer.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.append(integerList));
    }

    @Test
    void prepend() {
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 0, 4));
        assertEquals(0, circularBuffer.get(0));
        assertEquals(3, circularBuffer.get(3));
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 4, 11));
        assertEquals(4, circularBuffer.get(0));
        assertEquals(2, circularBuffer.get(9));
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 11, 14));
        assertEquals(11, circularBuffer.get(0));
        assertEquals(10, circularBuffer.get(9));
        assertEquals(7, circularBuffer.get(6));
        assertEquals(6, circularBuffer.get(5));
        assertEquals(12, circularBuffer.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.append(integerList));
    }

    @Test
    void prepend_with_append() {
        circularBuffer.append(Arrays.copyOfRange(integerList, 2, 5));
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 0, 2));
        assertEquals(0, circularBuffer.get(0));
        assertEquals(2, circularBuffer.get(2));
        assertEquals(4, circularBuffer.get(4));

    }

    @Test
    void prepend_with_append_override() {
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 10));
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 10, 14));
        assertEquals(10, circularBuffer.get(0));
        assertEquals(5, circularBuffer.get(9));
        assertEquals(2, circularBuffer.get(6));

        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 3));
        assertEquals(13, circularBuffer.get(0));

       }

    @Test
    void clear() {
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 10));
        assertEquals(0, circularBuffer.get(0));
        assertEquals(9, circularBuffer.get(9));
        assertEquals(4, circularBuffer.get(4));
        circularBuffer.clear();
        assertEquals(0, circularBuffer.getCount());
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.get(9));
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.get(4));
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 10, 14));
        assertEquals(10, circularBuffer.get(0));
        assertEquals(13, circularBuffer.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.get(9));
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.get(4));
        circularBuffer.clear();
        assertEquals(0, circularBuffer.getCount());
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.get(9));
        assertThrows(IndexOutOfBoundsException.class, () -> circularBuffer.get(3));

    }

    @Test
    void getCount() {
        assertEquals(0, circularBuffer.getCount());
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 1));
        assertEquals(1, circularBuffer.getCount());
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 1, 3));
        assertEquals(3, circularBuffer.getCount());
        circularBuffer.append(Arrays.copyOfRange(integerList, 3, 9));
        assertEquals(9, circularBuffer.getCount());
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 9, 10));
        assertEquals(10, circularBuffer.getCount());
        circularBuffer.clear();
        assertEquals(0, circularBuffer.getCount());
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 10));
        assertEquals(10, circularBuffer.getCount());
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 10, 14));
        assertEquals(10, circularBuffer.getCount());

    }

    @Test
    void isFull() {
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 1));
        assertFalse(circularBuffer.isFull());
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 1, 9));
        assertFalse(circularBuffer.isFull());
        circularBuffer.append(Arrays.copyOfRange(integerList, 9, 10));
        assertTrue(circularBuffer.isFull());
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 10, 14));
        assertTrue(circularBuffer.isFull());
        circularBuffer.clear();
        assertFalse(circularBuffer.isFull());
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 10));
        assertTrue(circularBuffer.isFull());
    }

    @Test
    void isEmpty() {
        assertTrue(circularBuffer.isEmpty());
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 1));
        assertFalse(circularBuffer.isEmpty());
        circularBuffer.clear();
        assertTrue(circularBuffer.isEmpty());
        circularBuffer.prepend(Arrays.copyOfRange(integerList, 0, 10));
        assertFalse(circularBuffer.isEmpty());

    }

    @Test
    void iterator(){
        circularBuffer.append(Arrays.copyOfRange(integerList, 0, 10));
        int i=0;
        for(Integer e: circularBuffer){
            assertEquals(integerList[i], e);
            i++;
        }
    }
}