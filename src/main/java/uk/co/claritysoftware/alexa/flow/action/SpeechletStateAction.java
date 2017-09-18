package uk.co.claritysoftware.alexa.flow.action;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * A convenience extension of {@link StateAction} for {@link IntentRequest intent requests} that return a {@link SpeechletResponse}.
 *
 * It is envisaged that most StateAction implementations will be an implementation of this interface.
 */
public interface SpeechletStateAction extends StateAction<SpeechletRequestEnvelope<IntentRequest>, SpeechletResponse> {

}
