package pc;

/**
 * 基于 wait() notify() 实现生产者消费者模型
 *
 * @author qihaodong
 */
public class WaitNotifyAchieve {

    public static void main(String[] args) {
        for (int i = 0; i < WORKER_NUM; i++) {
            new Thread(new Producer("生产者-" + i)).start();
            new Thread(new Consumer("消费者-" + i)).start();
        }
    }

    /**
     * 用来保证同步的锁
     */
    private final static String LOCK = "lock";
    /**
     * 生产者、消费者最大数量
     */
    private final static int WORKER_NUM = 10;
    /**
     * 当前容器中的总数量
     */
    private static volatile int COUNT = 0;
    /**
     * 容器的最大值
     */
    private final static int MAX = 100;
    /**
     * 当前共完成任务数量
     */
    private static volatile int TOTAL_COUNT = 0;
    /**
     * 当前共消费任务数量
     */
    private static volatile int CON_TOTAL_COUNT = 0;
    /**
     * 需要完成的任务数量
     */
    private final static int END = 3000;

    static class Producer implements Runnable {

        private String name;

        public Producer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // 模拟工作时间，每生产一个需要1s
                    Thread.sleep(1000);
                    synchronized (LOCK) {
                        if (TOTAL_COUNT == END) {
                            System.out.println("【" + name + "】:生产任务已满足条件，线程停止");
                            break;
                        }
                        if (COUNT == 0) {
                            System.out.println("【" + name + "】:生产时容器为空，唤醒消费者");
                            LOCK.notifyAll();
                        }
                        if (COUNT == MAX) {
                            System.out.println("【" + name + "】:生产时容器已满，线程阻塞");
                            LOCK.wait();
                        }
                        COUNT++;
                        TOTAL_COUNT++;
                        System.out.println("【" + name + "】:生产完成，" +
                                "此时容器中含有" + COUNT + "个未被消费工作，" +
                                "其中共生产" + TOTAL_COUNT + "个任务");
                        if (TOTAL_COUNT == END) {
                            System.out.println("【" + name + "】:生产任务已满足条件，线程停止");
                            break;
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable {

        private String name;

        public Consumer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // 模拟工作时间，每生产一个需要1s
                    Thread.sleep(1000);
                    synchronized (LOCK) {
                        if (CON_TOTAL_COUNT == END) {
                            System.out.println("【" + name + "】:消费任务已满足条件，线程停止");
                            break;
                        }
                        if (COUNT == 0) {
                            System.out.println("【" + name + "】:消费时时容器为空，消费者阻塞");
                            LOCK.wait();
                        }
                        if (COUNT == MAX) {
                            System.out.println("【" + name + "】:消费时发现容器已满，唤醒生产者");
                            LOCK.notifyAll();
                        }
                        COUNT--;
                        CON_TOTAL_COUNT++;
                        System.out.println("【" + name + "】:消费完成，" +
                                "此时容器中含有" + COUNT + "个未被消费工作，" +
                                "其中共消费" + CON_TOTAL_COUNT + "个任务");
                        if (CON_TOTAL_COUNT == END) {
                            System.out.println("【" + name + "】:消费任务已满足条件，线程停止");
                            break;
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}