package uk.co.claritysoftware.alexa.flow.action;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * A convenience extension of {@link StateAction} for {@link LaunchRequest intent requests} that return a {@link SpeechletResponse}.
 */
public interface LaunchSpeechletStateAction extends StateAction<SpeechletRequestEnvelope<LaunchRequest>, SpeechletResponse> {

}
