package com.sue.jvm.threadpool;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author sue
 * @date 2020/8/20 10:22
 */

public class LinkBlockingQueueTest {
    public static void main(String[] args){
        LinkedBlockingDeque<Object> queue = new LinkedBlockingDeque<>(1);
        queue.add("abc");
        boolean abc = queue.offer("abc");
        System.out.println(abc);
    }
}
