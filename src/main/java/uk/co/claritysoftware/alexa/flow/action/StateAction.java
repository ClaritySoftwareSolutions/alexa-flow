package uk.co.claritysoftware.alexa.flow.action;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.SpeechletRequest;

/**
 * A StateAction is called as part of transitioning from one state to the next
 */
public interface StateAction<T> {

	/**
	 * Method to perform the action
	 *
	 * @param requestEnvelope the entire request envelope from the Alexa request
	 * @return the result of the action whose type is {@link T}
	 */
	T doAction(SpeechletRequestEnvelope<? extends SpeechletRequest> requestEnvelope);
}
