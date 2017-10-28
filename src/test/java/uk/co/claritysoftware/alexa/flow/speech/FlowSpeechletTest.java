package uk.co.claritysoftware.alexa.flow.speech;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static uk.co.claritysoftware.alexa.flow.model.Flow.flowBuilder;
import static uk.co.claritysoftware.alexa.flow.model.State.stateBuilder;
import static uk.co.claritysoftware.alexa.flow.model.Transition.transitionBuilder;
import static uk.co.claritysoftware.alexa.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelopeWithSession;
import static uk.co.claritysoftware.alexa.testsupport.SpeechletRequestEnvelopeTestDataFactory.sessionStartedSpeechletRequestEnvelope;
import static uk.co.claritysoftware.alexa.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelope;

import org.junit.Before;
import org.junit.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.flow.action.StateAction;
import uk.co.claritysoftware.alexa.flow.model.Flow;
import uk.co.claritysoftware.alexa.flow.model.State;

/**
 * Unit test class for {@link FlowSpeechlet}
 */
public class FlowSpeechletTest {

	private static final Session SESSION = mock(Session.class);

	private static final String FORWARD_INTENT = "forwardIntent";

	private static final String STATE_1_ID = "state1";

	private static final StateAction STATE_1_ACTION = mock(StateAction.class);

	private static final State STATE_1 = stateBuilder()
			.id(STATE_1_ID)
			.action(STATE_1_ACTION)
			.build();

	private static final String INITIAL_STATE_ID = "initial-state";

	private static final StateAction INITIAL_STATE_ACTION = mock(StateAction.class);

	private static final State INITIAL_STATE = stateBuilder()
			.id(INITIAL_STATE_ID)
			.action(INITIAL_STATE_ACTION)
			.transition(transitionBuilder()
					.onIntent(FORWARD_INTENT)
					.to(STATE_1_ID)
					.build())
			.build();

	private static final Flow FLOW = flowBuilder()
			.initialState(INITIAL_STATE)
			.intentState(STATE_1)
			.build();

	private FlowSpeechlet flowSpeechlet;

	@Before
	public void resetMocks() {
		reset(SESSION, INITIAL_STATE_ACTION, STATE_1_ACTION);
	}

	@Before
	public void setup() {
		flowSpeechlet = new FlowSpeechlet(FLOW);
	}

	@Test
	public void shouldOnSessionStarted() {
		// Given
		SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope = sessionStartedSpeechletRequestEnvelope(SESSION);

		// When
		flowSpeechlet.onSessionStarted(requestEnvelope);

		// Then
		then(SESSION).should().setAttribute("currentState", INITIAL_STATE_ID);
	}

	@Test
	public void shouldOnLaunch() {
		// Given
		given(SESSION.getAttribute("currentState")).willReturn(INITIAL_STATE_ID);

		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelopeWithSession(SESSION);

		SpeechletResponse expectedResponse = new SpeechletResponse();
		given(INITIAL_STATE_ACTION.doAction(requestEnvelope)).willReturn(expectedResponse);

		// When
		SpeechletResponse speechletResponse = flowSpeechlet.onLaunch(requestEnvelope);

		// Then
		assertThat(speechletResponse).isEqualTo(expectedResponse);
	}

	@Test
	public void shouldOnIntent() {
		// Given
		given(SESSION.getAttribute("currentState")).willReturn(INITIAL_STATE_ID);

		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(SESSION)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName(FORWARD_INTENT)
								.build())
						.build())
				.build();

		SpeechletResponse expectedResponse = new SpeechletResponse();
		given(STATE_1_ACTION.doAction(any(SpeechletRequestEnvelope.class))).willReturn(expectedResponse);

		// When
		SpeechletResponse speechletResponse = flowSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse).isEqualTo(expectedResponse);
		verify(SESSION).setAttribute("currentState", STATE_1_ID);
	}
}
