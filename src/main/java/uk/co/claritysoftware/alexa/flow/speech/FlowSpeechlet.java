package uk.co.claritysoftware.alexa.flow.speech;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import uk.co.claritysoftware.alexa.flow.action.SpeechletStateAction;
import uk.co.claritysoftware.alexa.flow.model.Flow;
import uk.co.claritysoftware.alexa.flow.model.State;
import uk.co.claritysoftware.alexa.flow.model.Transition;

/**
 * The {@link SpeechletV2} that handles all flow control based on the injected {@link Flow} instance.
 */
public class FlowSpeechlet implements SpeechletV2 {

	private static final String CURRENT_STATE = "currentState";

	private final Flow flow;

	public FlowSpeechlet(final Flow flow) {
		this.flow = flow;
	}

	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {

	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Returns the {@link SpeechletResponse} from the {@link SpeechletStateAction#doAction(SpeechletRequestEnvelope)} method
	 * of the {@link State} registered as the initial state of the {@link Flow}.</p>
	 *
	 * @throws IllegalStateException if the initial state cannot be found.
	 */
	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		Session session = requestEnvelope.getSession();
		State<SpeechletStateAction> initialState = flow.getInitialState()
				.orElseThrow(() -> new IllegalStateException(String.format("Flow does not contain state matching initialStateId %s", flow.getInitialStateId())));

		SpeechletResponse response = initialState.getAction().doAction(requestEnvelope);
		session.setAttribute(CURRENT_STATE, flow.getInitialStateId());

		return response;
	}

	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		/**
		 * get current state id (string) from session
		 * get intent (string) from request
		 * Get SpeechletState from Flow based on state id (ie. current state)
		 * Get Transition from SpeechletState based on intent
		 * Get state id (string) of target state from Transition
		 * Get SpeechletState from Flow based on state id (ie. target state)
		 * Get SpeechletStateAction from Speechlet state and execute (call doAction)
		 * Set state id (target state) on session
		 * Return SpeechletResponse from calling doAction of action
		 */

		Session session = requestEnvelope.getSession();
		IntentRequest intentRequest = requestEnvelope.getRequest();

		// get current state id (string) from session
		String currentStateId = (String) session.getAttribute(CURRENT_STATE);

		// get intent (string) from request
		String intentName = intentRequest.getIntent().getName();

		// Get SpeechletState from Flow based on state id (ie. current state)
		State<SpeechletStateAction> speechletState = flow.getSpeechletState(currentStateId)
				.orElseThrow(() -> new IllegalStateException(String.format("State with id %s is not registered in the flow", currentStateId)));

		// Get Transition from SpeechletState based on intent
		Transition transition = speechletState.getTransition(intentName)
				.orElseThrow(() -> new IllegalStateException(String.format("No transition for intent name %s in state %s", intentName, speechletState)));

		// Get state id (string) of target state from Transition
		String targetStateId = transition.getTo();

		// Get SpeechletState from Flow based on state id (ie. target state)
		State<SpeechletStateAction> targetSpeechletState = flow.getSpeechletState(targetStateId)
				.orElseThrow(() -> new IllegalStateException(String.format("Target state with id %s is not registered in the flow", currentStateId)));

		// Get SpeechletStateAction from Speechlet state and execute (call doAction)
		SpeechletStateAction stateAction = targetSpeechletState.getAction();
		SpeechletResponse speechletResponse = stateAction.doAction(requestEnvelope);

		// Set state id (target state) on session
		session.setAttribute(CURRENT_STATE, targetStateId);

		// Return SpeechletResponse from calling doAction of action
		return speechletResponse;
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {

	}
}
