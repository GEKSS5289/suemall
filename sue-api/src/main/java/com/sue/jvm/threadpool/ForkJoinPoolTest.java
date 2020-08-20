package com.sue.jvm.threadpool;

import ch.qos.logback.classic.joran.ReconfigureOnChangeTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author sue
 * @date 2020/8/20 10:47
 */

public class ForkJoinPoolTest {
    //1-100
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> task = forkJoinPool.submit(new MyTask(1, 100));
        Integer integer = task.get();
        System.out.println(integer);
    }
}

class MyTask extends RecursiveTask<Integer>{

    public MyTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    //当前任务计算的起始
    private int start;
    //当前任务计算的结束
    private int end;
    //阈值，如果end-start在阈值以内那么就不用再去细化任务
    public static final int threshold=2;

    @Override
    protected Integer compute() {
        int sum = 0;
        boolean needFork = (end-start) > threshold;
        if(needFork){
            int middle = (start+end)/2;
            MyTask leftTask = new MyTask(start,middle);
            MyTask rightTask = new MyTask(middle+1,end);

            leftTask.fork();
            rightTask.fork();

            //子任务执行完成之后的结果
            Integer leftResult = leftTask.join();
            Integer rightResult = rightTask.join();

            sum = leftResult + rightResult;

        }else{

            for(int i = start;i<=end;i++){
                sum+=i;
            }
        }
        return sum;
    }
}
