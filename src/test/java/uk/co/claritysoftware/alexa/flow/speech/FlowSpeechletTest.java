package uk.co.claritysoftware.alexa.flow.speech;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.co.claritysoftware.alexa.flow.model.Flow.flowBuilder;
import static uk.co.claritysoftware.alexa.testsupport.SpeechletRequestEnvelopeTestDataFactory.launchSpeechletRequestEnvelopeWithSession;

import org.junit.Test;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import uk.co.claritysoftware.alexa.flow.action.SpeechletStateAction;
import uk.co.claritysoftware.alexa.flow.model.Flow;
import uk.co.claritysoftware.alexa.flow.model.State;

/**
 * Unit test class for {@link FlowSpeechlet}
 */
public class FlowSpeechletTest {

	@Test
	public void shouldOnLaunch() {
		// Given
		String initialStateId = "state1";
		SpeechletStateAction stateAction = mock(SpeechletStateAction.class);

		State<SpeechletStateAction> state1 = State.<SpeechletStateAction>stateBuilder()
				.id(initialStateId)
				.action(stateAction)
				.build();
		Flow flow = flowBuilder()
				.initialStateId(initialStateId)
				.speechletState(state1)
				.build();

		FlowSpeechlet flowSpeechlet = new FlowSpeechlet(flow);

		Session session = mock(Session.class);

		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelopeWithSession(session);

		SpeechletResponse expectedResponse = new SpeechletResponse();
		given(stateAction.doAction(requestEnvelope))
				.willReturn(expectedResponse);

		// When
		SpeechletResponse speechletResponse = flowSpeechlet.onLaunch(requestEnvelope);

		// Then
		assertThat(speechletResponse).isEqualTo(expectedResponse);
		verify(session).setAttribute("currentState", initialStateId);
	}

	@Test
	public void shouldFailToOnLaunchGivenInitialStateNotFound() {
		// Given
		String initialStateId = "non-registered-state";
		SpeechletStateAction stateAction = mock(SpeechletStateAction.class);

		State<SpeechletStateAction> state1 = State.<SpeechletStateAction>stateBuilder()
				.id("state1")
				.action(stateAction)
				.build();
		Flow flow = flowBuilder()
				.initialStateId(initialStateId)
				.speechletState(state1)
				.build();

		FlowSpeechlet flowSpeechlet = new FlowSpeechlet(flow);

		Session session = mock(Session.class);

		SpeechletRequestEnvelope<LaunchRequest> requestEnvelope = launchSpeechletRequestEnvelopeWithSession(session);

		SpeechletResponse expectedResponse = new SpeechletResponse();
		given(stateAction.doAction(requestEnvelope))
				.willReturn(expectedResponse);

		// When
		try {
			flowSpeechlet.onLaunch(requestEnvelope);

			fail("Was expecting an IllegalStateException");
		}
		// Then
		catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("Flow does not contain state matching initialStateId non-registered-state");
			verify(session, never()).setAttribute("currentState", initialStateId);
			verify(stateAction, never()).doAction(requestEnvelope);
		}
	}

}
