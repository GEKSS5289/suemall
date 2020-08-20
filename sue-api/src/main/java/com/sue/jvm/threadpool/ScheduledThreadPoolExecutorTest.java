package com.sue.jvm.threadpool;

import org.omg.CORBA.TIMEOUT;

import java.util.concurrent.*;

/**
 * @author sue
 * @date 2020/8/20 10:30
 */

public class ScheduledThreadPoolExecutorTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                10,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
//        executor.schedule(()->{
//            System.out.println("aaa");
//        },3,TimeUnit.SECONDS);
//
//
//        ScheduledFuture<String> schedule = executor.schedule((Callable<String>) () -> {
//            return "bbb";
//        }, 4, TimeUnit.SECONDS);
//        String s = schedule.get();
//        System.out.println(s);

        executor.scheduleAtFixedRate(()->{
            System.out.println("ccc");
        },0,3,TimeUnit.SECONDS);

    }


}
