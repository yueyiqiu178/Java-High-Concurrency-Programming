package com.high.concurrency.chapter3.section1;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description
 * @Author dongzonglei
 * @Date 2018/12/26 下午6:43
 */
public class ReadWriteLockDemo {

    private static Lock lock = new ReentrantLock();

    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static Lock readLock = readWriteLock.readLock();

    private static Lock writeLock = readWriteLock.writeLock();

    private int value;

    public Object handleRead(Lock lock) throws Exception {
        try {
            lock.lock();
            Thread.sleep(1000);
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void handleWrite(Lock lock, int index) throws Exception {
        try {
            lock.lock();
            Thread.sleep(1000);
            value = index;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        final ReadWriteLockDemo demo = new ReadWriteLockDemo();
        Runnable readRunnable = new Runnable() {
            public void run() {
                try {
                    demo.handleRead(readLock);
//                    demo.handleRead(lock);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable writeRunnable = new Runnable() {
            public void run() {
                try {
                    demo.handleWrite(writeLock, new Random().nextInt());
//                    demo.handleWrite(lock, new Random().nextInt());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        /*
        
        Thread[] rThreads = new Thread[18];
        Thread[] wThreads = new Thread[12];
        
        System.out.println(System.currentTimeMillis());
      
        for(int i=0;i<rThreads.length;i++) {
        	rThreads[i] = new Thread(readRunnable);
        	rThreads[i].start();
        	//rThreads[i].join();
        }
        
        for(int i=0;i<wThreads.length;i++) {
        	wThreads[i] = new Thread(writeRunnable);
        	wThreads[i].start();
        	//wThreads[i].join();
        }
        
        for(int i=0;i<rThreads.length;i++) {
        	rThreads[i].join();
        }
        
        for(int i=0;i<wThreads.length;i++) {
        	wThreads[i].join();
        }
        
        System.out.println(System.currentTimeMillis());
        
        */
        
        for (int i = 0; i < 18; i++) {
            new Thread(readRunnable).start();
        }

        for (int i = 18; i < 20; i++) {
            new Thread(writeRunnable).start();
        }
    }
}
