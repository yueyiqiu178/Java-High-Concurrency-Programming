package com.high.concurrency.chapter4.section4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleDateFormatDemo {

	static volatile ThreadLocal<SimpleDateFormat> t1 = new ThreadLocal<SimpleDateFormat>();
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static class ParseDate implements Runnable {

		int i = 0;

		public ParseDate(int i) {
			this.i = i;
		}

		@Override
		public void run() {
			try {
				
                if (t1.get() == null) {
                    t1.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                    System.out.println(Thread.currentThread().getId() + ":create SimpleDateFormat");
                }
				
				Date t = t1.get().parse("2015-03-29 19:29:" + i % 60);
				//Date t = sdf.parse("2015-03-29 19:29:" + i % 60);
				System.out.println(i + ":" + t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 1000; i++) {
			es.execute(new ParseDate(i));
		}
		es.shutdown();
	}
}
