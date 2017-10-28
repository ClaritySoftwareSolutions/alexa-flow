package uk.co.claritysoftware.alexa.flow.action;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * A StateAction is called as part of transitioning from one state to the next
 */
public interface StateAction {

	/**
	 * Method to perform the action
	 *
	 * @param requestEnvelope the entire request envelope from the Alexa request
	 * @return the {@link SpeechletResponse} of the action
	 */
	SpeechletResponse doAction(SpeechletRequestEnvelope requestEnvelope);
}
