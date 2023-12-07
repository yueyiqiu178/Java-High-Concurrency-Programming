package com.high.concurrency.chapter2;

public class PlusTask {

	static volatile int i = 0;

	//static private Object o = new Object();

	static class Plus implements Runnable {

		@Override
		public void run() {
			//synchronized (o) {
				for (int k = 0; k < 10000; k++)
					i++;
			//}
		}
	}

	public static void main(String[] args) throws InterruptedException {

		Thread[] threads = new Thread[10];

		for (int i = 0; i < 10; i++) {
			threads[i] = new Thread(new Plus());
			threads[i].start();
		}

		for (int i = 0; i < 10; i++) {
			threads[i].join();
		}

		System.out.println(i);
	}

}
