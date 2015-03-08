package com.pigeon.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pigeon.consumer.config.ConfigConsumer;

public class PigeonConsumerExample {

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("consumer.xml");
		final ConfigConsumer configConsumer = (ConfigConsumer) ctx.getBean("configConsumer");
		for (int i = 0; i < 1; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(configConsumer.consume("/config.test"));
				}
			});
			thread.start();
		}
		//		System.out.println(configConsumer.consume("/config.test"));
		Thread.sleep(1000000);
	}
}
