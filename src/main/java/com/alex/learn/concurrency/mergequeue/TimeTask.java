package com.alex.learn.concurrency.mergequeue;

public abstract class TimeTask<E extends TimeTask<E>> implements Comparable<E>{
    
    /**
     * 任务ID
     */
    private String key;
    /**
     * 任务执行时间，单位ms
     */
    private long exTime;

    public TimeTask(String key, long exTime){
        this.key = key;
        this.exTime = exTime;
    }

    public long getExTime(){
        return this.exTime;
    }

    /**
     * 任务执行的具体逻辑
     */
    protected abstract void execute();

    /**
     * 实现从其他任务中拷贝数据
     */
    protected abstract void filedFrom(E e);

}