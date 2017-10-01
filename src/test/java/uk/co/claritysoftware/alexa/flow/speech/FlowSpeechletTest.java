package uk.co.claritysoftware.alexa.flow.speech;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.co.claritysoftware.alexa.flow.model.Flow.flowBuilder;
import static uk.co.claritysoftware.alexa.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelopeWithSession;
import static uk.co.claritysoftware.alexa.testsupport.SpeechletRequestEnvelopeTestDataFactory.speechletRequestEnvelope;

import org.junit.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.flow.action.LaunchSpeechletStateAction;
import uk.co.claritysoftware.alexa.flow.model.Flow;
import uk.co.claritysoftware.alexa.flow.model.State;

/**
 * Unit test class for {@link FlowSpeechlet}
 */
public class FlowSpeechletTest {

	private static final String INITIAL_STATE_ID = "initial-state";

	private Session session = mock(Session.class);

	private LaunchSpeechletStateAction initialStateAction = mock(LaunchSpeechletStateAction.class);

	private State<LaunchSpeechletStateAction> initialState = State.<LaunchSpeechletStateAction>stateBuilder()
			.id(INITIAL_STATE_ID)
			.action(initialStateAction)
			.build();

	@Test
	public void shouldOnLaunch() {
		// Given
		Flow flow = flowBuilder()
				.initialState(initialState)
				.build();

		FlowSpeechlet flowSpeechlet = new FlowSpeechlet(flow);

		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelopeWithSession(session);

		SpeechletResponse expectedResponse = new SpeechletResponse();
		given(initialStateAction.doAction(requestEnvelope)).willReturn(expectedResponse);

		// When
		SpeechletResponse speechletResponse = flowSpeechlet.onLaunch(requestEnvelope);

		// Then
		assertThat(speechletResponse).isEqualTo(expectedResponse);
		verify(session).setAttribute("currentState", INITIAL_STATE_ID);
	}

	@Test
	public void shouldOnIntentGivenFlowNotStarted() {
		// Given
		SpeechletRequestEnvelope<IntentRequest> requestEnvelope = speechletRequestEnvelope()
				.withSession(session)
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName("intent-1")
								.build())
						.build())
				.build();

		Flow flow = flowBuilder()
				.initialState(initialState)
				.build();

		FlowSpeechlet flowSpeechlet = new FlowSpeechlet(flow);

		SpeechletResponse expectedResponse = new SpeechletResponse();
		given(initialStateAction.doAction(any(SpeechletRequestEnvelope.class))).willReturn(expectedResponse);

		// When
		SpeechletResponse speechletResponse = flowSpeechlet.onIntent(requestEnvelope);

		// Then
		assertThat(speechletResponse).isEqualTo(expectedResponse);
		verify(session).setAttribute("currentState", INITIAL_STATE_ID);
	}
}
