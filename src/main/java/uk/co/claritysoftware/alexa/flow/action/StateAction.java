package uk.co.claritysoftware.alexa.flow.action;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.SpeechletRequest;

/**
 * A StateAction is called as part of transitioning from one state to the next
 */
public interface StateAction<ENVELOPE_TYPE extends SpeechletRequestEnvelope<? extends SpeechletRequest>, RETURN_TYPE> {

	/**
	 * Method to perform the action
	 *
	 * @param requestEnvelope the entire request envelope from the Alexa request
	 * @return the result of the action whose type is {@link RETURN_TYPE}
	 */
	RETURN_TYPE doAction(ENVELOPE_TYPE requestEnvelope);
}
