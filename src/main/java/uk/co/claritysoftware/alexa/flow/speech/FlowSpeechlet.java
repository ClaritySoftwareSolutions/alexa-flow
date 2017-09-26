package uk.co.claritysoftware.alexa.flow.speech;

import org.apache.commons.lang3.StringUtils;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import uk.co.claritysoftware.alexa.flow.action.IntentSpeechletStateAction;
import uk.co.claritysoftware.alexa.flow.action.LaunchSpeechletStateAction;
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
	 * <p>Returns the {@link SpeechletResponse} from the {@link LaunchSpeechletStateAction#doAction(SpeechletRequestEnvelope)} method
	 * of the {@link State} registered as the initial state of the {@link Flow}.</p>
	 */
	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		Session session = requestEnvelope.getSession();
		State<LaunchSpeechletStateAction> initialState = flow.getInitialState();
		SpeechletResponse response = initialState.getAction().doAction(requestEnvelope);
		session.setAttribute(CURRENT_STATE, initialState.getId());

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
		if (StringUtils.isBlank(currentStateId)) {
			return flow.getFlowNotLaunchedAction().doAction(requestEnvelope);
		}

		// get intent (string) from request
		String intentName = intentRequest.getIntent().getName();

		// Get SpeechletState from Flow based on state id (ie. current state)
		State<IntentSpeechletStateAction> speechletState = flow.getIntentState(currentStateId)
				.orElseThrow(() -> new IllegalStateException(String.format("State with id %s is not registered in the flow", currentStateId)));

		// Get Transition from SpeechletState based on intent
		Transition transition = speechletState.getTransition(intentName)
				.orElseThrow(() -> new IllegalStateException(String.format("No transition for intent name %s in state %s", intentName, speechletState)));

		// Get state id (string) of target state from Transition
		String targetStateId = transition.getTo();

		// Get SpeechletState from Flow based on state id (ie. target state)
		State<IntentSpeechletStateAction> targetSpeechletState = flow.getIntentState(targetStateId)
				.orElseThrow(() -> new IllegalStateException(String.format("Target state with id %s is not registered in the flow", currentStateId)));

		// Get SpeechletStateAction from Speechlet state and execute (call doAction)
		IntentSpeechletStateAction stateAction = targetSpeechletState.getAction();
		SpeechletResponse speechletResponse = stateAction.doAction(requestEnvelope);

		// Set state id (target state) on session
		session.setAttribute(CURRENT_STATE, targetStateId);

		// Return SpeechletResponse from calling doAction of action
		return speechletResponse;
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {

	}

	private SpeechletRequestEnvelope<LaunchRequest> launchRequestEnvelope(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		IntentRequest intentRequest = requestEnvelope.getRequest();
		LaunchRequest launchRequest = LaunchRequest.builder()
				.withRequestId(intentRequest.getRequestId())
				.withLocale(intentRequest.getLocale())
				.withTimestamp(intentRequest.getTimestamp())
				.build();

		SpeechletRequestEnvelope<LaunchRequest> launchRequestEnvelope = SpeechletRequestEnvelope.<LaunchRequest>builder()
				.withRequest(launchRequest)
				.withVersion(requestEnvelope.getVersion())
				.withSession(requestEnvelope.getSession())
				.build();

		return launchRequestEnvelope;
	}
}
