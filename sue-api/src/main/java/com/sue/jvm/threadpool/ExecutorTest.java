package com.sue.jvm.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sue
 * @date 2020/8/20 10:59
 */

public class ExecutorTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.execute();
    }
}
