package com.pigeon.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pigeon.provider.config.ConfigProvider;

public class PigeonProviderExample {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("provider.xml");
		ConfigProvider configProvider = (ConfigProvider) ctx.getBean("configProvider");
		configProvider.send("/config.test", "flysqrlboy");
	}
}
