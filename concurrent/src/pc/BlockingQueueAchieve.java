package pc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author qihaodong
 */
public class BlockingQueueAchieve {

    public static void main(String[] args) {
        for (int i = 0; i < WORKER_NUM; i++) {
            new Thread(new Producer("生产者-" + i)).start();
            new Thread(new Consumer("消费者-" + i)).start();
        }
    }

    /**
     * 生产者、消费者最大数量
     */
    private final static int WORKER_NUM = 10;
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

    /**
     * 用来保证同步
     */
    private static final BlockingQueue queue = new ArrayBlockingQueue(MAX);


    static class Producer implements Runnable {

        private String name;

        public Producer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 模拟工作时间，每生产一个需要1s
                    Thread.sleep(1000);
                    if (TOTAL_COUNT == END) {
                        System.out.println("【" + name + "】:生产任务已满足条件，线程停止");
                        break;
                    }
                    if (queue.size() == 0) {
                        System.out.println("【" + name + "】:生产时容器为空，唤醒消费者");
                    }
                    while (queue.size() == MAX) {
                        System.out.println("【" + name + "】:生产时容器已满，线程阻塞");
                    }
                    queue.put(1);
                    TOTAL_COUNT++;
                    System.out.println("【" + name + "】:生产完成，" +
                            "此时容器中含有" + queue.size() + "个未被消费工作，" +
                            "其中共生产" + TOTAL_COUNT + "个任务");
                    if (TOTAL_COUNT == END) {
                        System.out.println("【" + name + "】:生产任务已满足条件，线程停止");
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            while (true) {
                try {
                    // 模拟工作时间，每生产一个需要1s
                    Thread.sleep(1000);
                    if (CON_TOTAL_COUNT == END) {
                        System.out.println("【" + name + "】:消费任务已满足条件，线程停止");
                        break;
                    }
                    while (queue.size() == 0) {
                        System.out.println("【" + name + "】:消费时时容器为空，消费者阻塞");
                    }
                    if (queue.size() == MAX) {
                        System.out.println("【" + name + "】:消费时发现容器已满，唤醒生产者");
                    }
                    queue.take();
                    CON_TOTAL_COUNT++;
                    System.out.println("【" + name + "】:消费完成，" +
                            "此时容器中含有" + queue.size() + "个未被消费工作，" +
                            "其中共消费" + CON_TOTAL_COUNT + "个任务");
                    if (CON_TOTAL_COUNT == END) {
                        System.out.println("【" + name + "】:消费任务已满足条件，线程停止");
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
