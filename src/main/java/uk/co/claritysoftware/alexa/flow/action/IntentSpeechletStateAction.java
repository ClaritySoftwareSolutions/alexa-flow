package uk.co.claritysoftware.alexa.flow.action;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * A convenience extension of {@link StateAction} for {@link IntentRequest intent requests} that return a {@link SpeechletResponse}.
 */
public interface IntentSpeechletStateAction extends StateAction<SpeechletRequestEnvelope<IntentRequest>, SpeechletResponse> {

}
