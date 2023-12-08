package com.high.concurrency.chapter4.section4;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RndTaskDemo {

	public static final int GEN_COUNT = 10000000;
	public static final int THREAD_CONT = 4;

	private static ExecutorService exe = Executors.newFixedThreadPool(THREAD_CONT);
	public static Random rnd = new Random(123);

	public static ThreadLocal<Random> tRnd = new ThreadLocal<Random>() {

		@Override
		protected Random initialValue() {
			return new Random(123);
		}

	};

	public static class RndTask implements Callable<Long> {
		private int mode = 0;

		public RndTask(int mode) {
			this.mode = mode;
		}

		public Random getRandom() {
			if (mode == 0) {
				return rnd;
			} else if (mode == 1) {
				return tRnd.get();
			} else {
				return null;
			}
		}

		@Override
		public Long call() throws Exception {
			long b = System.currentTimeMillis();
			for (long i = 0; i < GEN_COUNT; i++) {
				getRandom().nextInt();
			}
			long e = System.currentTimeMillis();
			System.out.println(Thread.currentThread().getName() + " spend " + (e - b) + "ms");
			return e - b;
		}

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Future<Long>[] futs = new Future[THREAD_CONT];

		for (int i = 0; i < THREAD_CONT; i++) {
			futs[i] = exe.submit(new RndTask(0));
		}
		long totaltime = 0;
		for (int i = 0; i < THREAD_CONT; i++) {
			totaltime += futs[i].get();
		}
		System.out.println("多線程訪問同一個Random實例:" + totaltime + "ms");

		for (int i = 0; i < THREAD_CONT; i++) {
			futs[i] = exe.submit(new RndTask(1));
		}
		totaltime = 0;
		for (int i = 0; i < THREAD_CONT; i++) {
			totaltime += futs[i].get();
		}
		System.out.println("使用ThreadLocal包裝Random實例:" + totaltime + "ms");

		exe.shutdown();
	}

}
