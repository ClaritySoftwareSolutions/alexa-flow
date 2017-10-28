package uk.co.claritysoftware.alexa.flow.speech;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import uk.co.claritysoftware.alexa.flow.SimpleFlowStateMachine;
import uk.co.claritysoftware.alexa.flow.action.StateAction;
import uk.co.claritysoftware.alexa.flow.model.Flow;
import uk.co.claritysoftware.alexa.flow.model.State;

import lombok.extern.slf4j.Slf4j;

/**
 * The {@link SpeechletV2} that handles all flow control based on the injected {@link Flow} instance.
 */
@Slf4j
public class FlowSpeechlet implements SpeechletV2 {

	private static final String CURRENT_STATE = "currentState";

	private final Flow flow;

	public FlowSpeechlet(final Flow flow) {
		this.flow = flow;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Creates a new {@link SimpleFlowStateMachine} and sets the current state id on the session.</p>
	 */
	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		log.trace("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		SimpleFlowStateMachine stateMachine = new SimpleFlowStateMachine(flow);
		session.setAttribute(CURRENT_STATE, stateMachine.getState().getId());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Returns the {@link SpeechletResponse} from the {@link StateAction#doAction(SpeechletRequestEnvelope)} method
	 * of the {@link State} registered as the initial state of the {@link Flow}.</p>
	 */
	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		log.trace("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		String currentStateId = (String) session.getAttribute(CURRENT_STATE);
		SimpleFlowStateMachine stateMachine = new SimpleFlowStateMachine(flow, currentStateId);

		return stateMachine.getAction().doAction(requestEnvelope);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Sends the intent name to the {@link SimpleFlowStateMachine} and returns the {@link SpeechletResponse}
	 * from the {@link StateAction#doAction(SpeechletRequestEnvelope)} method of the current {@link State}
	 * of the {@link Flow}.</p>
	 */
	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		log.trace("onIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Session session = requestEnvelope.getSession();
		String currentStateId = (String) session.getAttribute(CURRENT_STATE);
		SimpleFlowStateMachine stateMachine = new SimpleFlowStateMachine(flow, currentStateId);

		String intentName = getIntentName(requestEnvelope);
		stateMachine.send(intentName);
		session.setAttribute(CURRENT_STATE, stateMachine.getState().getId());

		return stateMachine.getAction().doAction(requestEnvelope);
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {

	}

	private String getIntentName(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		IntentRequest intentRequest = requestEnvelope.getRequest();
		return intentRequest.getIntent().getName();
	}

}
