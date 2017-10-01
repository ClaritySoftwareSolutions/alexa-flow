package uk.co.claritysoftware.alexa.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Class that bootstraps spring context and provides static method to retrieve beans
 */
public class Application {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private static ApplicationContext springContext = null;

	private static ApplicationContext getSpringContext() {
		if (springContext == null) {
			synchronized (ApplicationContext.class) {
				if (springContext == null) {
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
