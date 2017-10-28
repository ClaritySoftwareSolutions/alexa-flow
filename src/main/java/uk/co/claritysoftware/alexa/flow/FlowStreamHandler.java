package uk.co.claritysoftware.alexa.flow;

import static uk.co.claritysoftware.alexa.flow.Application.getBean;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import uk.co.claritysoftware.alexa.flow.speech.FlowSpeechlet;

import lombok.extern.slf4j.Slf4j;

/**
 * Main {@link RequestStreamHandler} for the Flow
 */
@Slf4j
public final class FlowStreamHandler extends SpeechletRequestStreamHandler {

	private static final String APPLICATION_IDS = "com_amazon_speech_speechlet_servlet_supportedApplicationIds";

	public FlowStreamHandler() {
		super(getBean(FlowSpeechlet.class), applicationIds());
	}

	private static Set<String> applicationIds() {
		final String appIds = System.getenv(APPLICATION_IDS) != null ? System.getenv(APPLICATION_IDS) : "";
		final Set<String> applicationIds = Arrays.stream(appIds.split(",\\s*"))
				.map(String::trim)
				.filter(applicationId -> applicationId.length() > 0)
				.collect(Collectors.toSet());

		if (applicationIds.isEmpty()) {
			throw new IllegalStateException("Cannot instantiate FlowStreamHandler with null or empty " + APPLICATION_IDS + " system property");
		}

		log.trace("Returning application ids {}", applicationIds);
		return applicationIds;
	}
}
