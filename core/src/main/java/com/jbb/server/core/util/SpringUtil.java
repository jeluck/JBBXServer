package com.jbb.server.core.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Utility class helping to use Spring Framework
 * 
 * @author Vincent Tang
 */
public class SpringUtil {
	private static final String[] contextFiles = { "core-application-config.xml", "datasource-config.xml" };
	private static volatile ClassPathXmlApplicationContext context = null;

	private static final Object FINAL = new Object() {
		@Override
		protected void finalize() {
			SpringUtil.close();
		}
	};

	private static void initApplicationContext() throws BeansException {
		if (context == null) {
			synchronized (FINAL) {
				if (context == null) {
					context = new ClassPathXmlApplicationContext(contextFiles);
				}
			}
		}
	}

	/**
	 * Instantiate bean object
	 * 
	 * @param <T>
	 *            bean class or interface
	 * @param beanName
	 *            bean name
	 * @return bean object
	 */
	public static <T> T getBean(String beanName, Class<T> clazz) throws BeansException {
		initApplicationContext();
		return context.getBean(beanName, clazz);
	}

	public static <T> T getBeanWithClassCheck(String beanName, Class<T> clazz) throws BeansException {
		try {
			return getBean(beanName, clazz);
		} catch (BeanNotOfRequiredTypeException e) {
			return null;
		}
	}

	public static <T> T getBeanWithFullCheck(String beanName, Class<T> clazz) {
		try {
			return getBean(beanName, clazz);
		} catch (BeansException e) {
			return null;
		}
	}

	public static void close() {
		if (context != null)
			context.close();
	}
}
