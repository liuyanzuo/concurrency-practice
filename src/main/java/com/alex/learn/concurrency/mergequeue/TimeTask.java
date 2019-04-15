package com.alex.learn.concurrency.mergequeue;

import java.util.Objects;

public abstract class TimeTask<E extends TimeTask<E>> implements Comparable<E> {

    /**
     * 任务ID
     */
    private String key;
    /**
     * 任务执行时间，单位ms
     */
    private long exTime;

    public TimeTask(String key, long exTime) {
        this.key = key;
        this.exTime = exTime;
    }

    public long getExTime() {
        return this.exTime;
    }

    public String getKey() {
        return key;
    }

    /**
     * 任务执行的具体逻辑
     */
    protected abstract void execute();

    /**
     * 实现从其他任务中拷贝数据
     */
    protected abstract void filedFrom(E e);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeTask<?> timeTask = (TimeTask<?>) o;

        return Objects.equals(key, timeTask.key);
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public int compareTo(E o) {
        //compareTo用于优先级队列中构造最小堆
        return Long.compare(getExTime(), o.getExTime());
    }
}