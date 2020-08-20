package com.sue.jvm.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sue
 * @date 2020/8/20 10:03
 */

public class ThreadPoolTest {
    public static void main(String[] args){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                10,
                10L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
