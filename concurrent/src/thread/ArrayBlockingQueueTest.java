package thread;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author qihaodong
 */
public class ArrayBlockingQueueTest {

    ArrayBlockingQueue<TestProduct> queue = new ArrayBlockingQueue<TestProduct>(1);


    public static void main(String[] args) {
        ArrayBlockingQueueTest test = new ArrayBlockingQueueTest();
        new Thread(test.new Product()).start();
        new Thread(test.new Customer()).start();
    }

    class Product implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    queue.put(new TestProduct());
                    System.out.println("生产者创建产品等待消费者消费");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Customer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    queue.take();
                    System.out.println("消费者消费产品等待生产者创建");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class TestProduct {
    }

}
