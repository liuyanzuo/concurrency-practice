package com.alex.learn.concurrency.mergequeue;

import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws Exception {
        long current = System.currentTimeMillis();
        TestTimeTask t1 = new TestTimeTask("k1", current + 5 * 1000, "m1");
        TestTimeTask t2 = new TestTimeTask("k2", current + 4 * 1000, "m2");
        TestTimeTask t3 = new TestTimeTask("k3", current + 3 * 1000, "m3");
        MergeBuffer<TestTimeTask> buffer = new MergeBuffer<>();
        buffer.start("test");
        buffer.put(t1, true);
        buffer.put(t2, true);
        buffer.put(t3, true);
        TimeUnit.SECONDS.sleep(7);
        current = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            TestTimeTask repeat = new TestTimeTask("repeat", current + 5 * 1000, "m" + i);
            buffer.put(repeat, true);
        }
        TimeUnit.SECONDS.sleep(100);
    }

    public static class TestTimeTask extends TimeTask<TestTimeTask> {

        private String message;

        public TestTimeTask(String key, long exTime, String message) {
            super(key, exTime);
            this.message = message;
        }

        @Override
        protected void execute() {
            System.out.println(message);
        }

        @Override
        protected void filedFrom(TestTimeTask other) {
            this.message = other.message;
        }

        @Override
        public String toString() {
            return "TestTimeTask{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

}
