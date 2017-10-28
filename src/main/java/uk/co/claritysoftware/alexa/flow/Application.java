package uk.co.claritysoftware.alexa.flow;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that bootstraps spring context and provides static method to retrieve beans
 */
@Slf4j
public class Application {

	private static ApplicationContext springContext = null;

	private static ApplicationContext getSpringContext() {
		if (springContext == null) {
			synchronized (ApplicationContext.class) {
				if (springContext == null) {
					log.trace("Starting Spring context");
					springContext = new ClassPathXmlApplicationContext("/application-context.xml");
				}
			}
		}
		return springContext;
	}

	public static <T> T getBean(Class<T> clazz) {
		return getSpringContext().getBean(clazz);
	}
}
