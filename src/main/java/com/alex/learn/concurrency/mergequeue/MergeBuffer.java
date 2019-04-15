package com.alex.learn.concurrency.mergequeue;

import java.util.Objests;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class MergeBuffer<E extends TimeTask>{
    
    private PriorityQueue queue = new PriorityQueue();

    private ReentrantLock lock = new ReentrantLock();

    private Condition waitCondition = lock.newCondition();

    private volatile boolean isClosed = false;

    private Thread thread;

    public void start(String threadName){
        if(Objects.nonNull(thread)) {
            return;
        }
        thread = new Thead(this::mainLoop,threadName).start();
    }

    private void mainLoop(){
        while(!isClosed){
            lock.lock();
            try{
                if(!queue.isEmpty()){
                    E task = queue.peek();
                    long cur = System.currentTimeMillis();
                    long exTime = task.getExTime();
                    long delay = exTime - cur;
                    if(delay <= 0){
                        //当前时间大于等于执行时间，执行任务
                        queue.poll();
                        task.execute();
                        continue;
                    }
                    //等待
                    waitCondition.await(delay, TimeUnit.SECONDS);
                }
                //队列为空，消费者一直等待
                waitCondition.await();
            }finally{
                lock.unLock();
            }
        }
    }

    public void close(){
        isClosed = true;
    }

}