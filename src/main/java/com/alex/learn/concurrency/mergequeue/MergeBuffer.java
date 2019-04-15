package com.alex.learn.concurrency.mergequeue;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class MergeBuffer<E extends TimeTask<E>> {

    private PriorityQueue<E> queue = new PriorityQueue<>();

    private ReentrantLock lock = new ReentrantLock();

    private Condition waitCondition = lock.newCondition();

    private volatile boolean isClosed = false;

    private Thread thread;

    public void start(String threadName) {
        if (Objects.nonNull(thread)) {
            return;
        }
        thread = new Thread(this::mainLoop, threadName);
        thread.start();
    }

    public void put(E task, boolean isReplace) {
        lock.lock();
        try {
            if (queue.contains(task)) {
                //如果需要替换任务数据
                if (isReplace) {
                    for (E taskLoop : queue) {
                        if (Objects.equals(task, taskLoop)) {
                            //更新新数据
                            taskLoop.filedFrom(task);
                        }
                    }
                }
                //未变更时间消息，不用唤醒消费者
            } else {
                queue.add(task);
                //唤醒消费者
                waitCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    private void mainLoop() {
        while (!isClosed) {
            lock.lock();
            try {
                if (!queue.isEmpty()) {
                    E task = queue.peek();
                    long cur = System.currentTimeMillis();
                    long exTime = task.getExTime();
                    long delay = exTime - cur;
                    System.out.println(String.format("cur:%s exTime:%s delay:%s", cur, exTime, delay));
                    if (delay <= 0) {
                        //当前时间大于等于执行时间，执行任务
                        queue.poll();
                        task.execute();
                        continue;
                    }
                    //等待
                    waitCondition.await(delay, TimeUnit.MILLISECONDS);
                }else{
                    System.out.println("empty");
                    //队列为空，消费者一直等待
                    waitCondition.await();
                }
            } catch (InterruptedException e) {
                isClosed = true;
            } finally {
                lock.unlock();
            }
        }
    }

    public void close() {
        isClosed = true;
    }

    public PriorityQueue<E> getQueue() {
        return queue;
    }
}