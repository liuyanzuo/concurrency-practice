package com.alex.learn.concurrency.disadvantage;

/**
 * 对比大量线程和单线程执行效率
 * 任务：计算10组1-1000000的和
 */
public class LotsOfThread {

    static long MAX;

    static class Sum implements Runnable {
        long start;
        long end;
        long[] sums;
        int index;

        Sum(long start, long end, long[] sums, int index) {
            this.start = start;
            this.end = end;
            this.sums = sums;
            this.index = index;
        }

        @Override
        public void run() {
            long sum = 0;
            long startTime = System.currentTimeMillis();
            for (long i = start; i < end; i++) {
                sum += i;
            }
            long endTime = System.currentTimeMillis();
            sums[index] = sum;
        }
    }

    public static void singleThread() throws InterruptedException {
        inner(1);
    }

    public static void multipleThread(int threadCount) throws InterruptedException {
        inner(threadCount);
    }

    private static void inner(int threadCount) throws InterruptedException {
        //这里分配1000个线程来完成
        long index = 0;
        long segment = MAX / threadCount;
        long[] sums = new long[threadCount];
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < sums.length; i++) {
            sums[i] = 0L;
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new Sum(index, index + segment, sums, i));
            index += segment;
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
        long end = System.currentTimeMillis();
        long sum = 0L;
        for (int i = 0; i < threadCount; i++) {
            sum += sums[i];
        }
        System.out.println(String.format("Sum:%d,Cost:%dms", sum, (end - start)));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=======MAX:1000 * 1000 * 1000=====");
        MAX = 1000 * 1000 * 1000L;
        System.out.println("单线程执行：");
        singleThread();
        System.out.println("10个线程");
        multipleThread(10);
        System.out.println("100个线程");
        multipleThread(100);
        System.out.println("1000个线程");
        multipleThread(1000);
        System.out.println("2000个线程");
        multipleThread(2000);
        System.out.println("5000个线程");
        multipleThread(3000);
        System.out.println("8000个线程");
        multipleThread(8000);
        System.out.println("10000个线程");
        multipleThread(10000);
    }

}
