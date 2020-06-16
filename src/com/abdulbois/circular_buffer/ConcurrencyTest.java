package com.abdulbois.circular_buffer;

import org.junit.jupiter.api.*;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrencyTest {

    private CircularBufferImpl<Integer> circularBuffer;
    private List<Callable<String>> tasks;

    @BeforeEach
    void setUp() {
        circularBuffer = new CircularBufferImpl<>(100);
        tasks = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void concurrentlyAppend(){
        var nThreads = 2;
        var executor = Executors.newFixedThreadPool(nThreads);
        final var arr_1 = Utils.createRange(1, 50);
        final var arr_2 = Utils.createRange(51, 100);
        Callable<String> task_1 = () -> {
            for (int i=0, j=0, k=10; i < 5; i++, j=k, k+=10){
                circularBuffer.append(Arrays.copyOfRange(arr_1, j, k));
            }
            return "Task is finished by " + Thread.currentThread().getName();
        };
        Callable<String> task_2 = () -> {
            for (int i=0, j=0, k=5; i < 10; i++, j=k, k+=5){
                circularBuffer.append(Arrays.copyOfRange(arr_2, j, k));

            }
            return "Task is finished" + Thread.currentThread().getName();
        };

        tasks.add(task_1);
        tasks.add(task_2);
        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkBufferElements(Utils.createRange(1, 100));
    }
    @Test
    void concurrentlyPrepend(){
        final var arr_1 = Utils.createRange(1, 50);
        final var arr_2 = Utils.createRange(51, 100);
        var latch = new CountDownLatch(2);
        Runnable task_1 = () -> {
            for (int i=0, j=0, k=10; i < 5; i++, j=k, k+=10){
                circularBuffer.prepend(Arrays.copyOfRange(arr_1, j, k));
            }
            latch.countDown();
        };
        Runnable task_2 = () -> {
            for (int i = 0, j = 0, k = 5; i < 10; i++, j = k, k += 5) {
                circularBuffer.prepend(Arrays.copyOfRange(arr_2, j, k));

            }
            latch.countDown();
        };
        var thread_1 = new Thread(task_1);
        var thread_2 = new Thread(task_2);

        thread_1.start();
        thread_2.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkBufferElements(Utils.createRange(1, 100));
    }

    private void checkBufferElements(Integer [] resultArr){
        var result_list = new ArrayList<Integer>();
        for (Integer item: circularBuffer) {
            result_list.add(item);
        }
        for(Integer item: resultArr){
            assertTrue(result_list.contains(item));
        }
    }
}

