package condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author qihaodong
 */
public class ConditionTest {

    Lock lock = new ReentrantLock();

    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();

    int count = 0;

    class Product implements Runnable {
        @Override
        public void run() {
            while (count < 5) {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "生产一个面包并唤醒消费者消费，自身挂起");
                    condition2.signal();
                    condition1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    count++;
                    lock.unlock();
                }
            }
        }
    }

    class Customer implements Runnable {
        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    condition2.await();
                    System.out.println(Thread.currentThread().getName() + "消费一个面包并唤醒生产者创建，自身挂起");
                    condition1.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionTest c = new ConditionTest();
        Thread thread1 = new Thread(c.new Product());
        Thread thread2 = new Thread(c.new Customer());
        thread2.start();
        Thread.sleep(1000);
        thread1.start();
    }

}