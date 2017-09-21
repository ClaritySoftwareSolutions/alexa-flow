package uk.co.claritysoftware.alexa.flow.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.co.claritysoftware.alexa.flow.model.Flow.flowBuilder;
import static uk.co.claritysoftware.alexa.flow.model.Transition.transitionBuilder;

import java.util.Optional;
import org.junit.Test;
import uk.co.claritysoftware.alexa.flow.action.SpeechletStateAction;

/**
 * Unit test class for {@link Flow}
 */
public class FlowTest {

	private static final State<SpeechletStateAction> STATE_1 = State.<SpeechletStateAction>stateBuilder()
			.id("state1")
			.action(mock(SpeechletStateAction.class))
			.build();

	private static final State<SpeechletStateAction> STATE_2 = State.<SpeechletStateAction>stateBuilder()
			.id("state2")
			.action(mock(SpeechletStateAction.class))
			.build();

	@Test
	public void shouldGetSpeechletState() {
		// Given
		Flow flow = flowBuilder()
				.speechletState(STATE_1)
				.speechletState(STATE_2)
				.initialStateId("state1")
				.build();

		// When
		Optional<State<SpeechletStateAction>> state = flow.getSpeechletState("state1");

		// Then
		assertThat(state.get(), equalTo(STATE_1));
	}

	@Test
	public void shouldNotGetSpeechletStateGivenUnknownStateId() {
		// Given
		Flow flow = flowBuilder()
				.speechletState(STATE_1)
				.speechletState(STATE_2)
				.initialStateId("state1")
				.build();

		// When
		Optional<State<SpeechletStateAction>> state = flow.getSpeechletState("some-unknown-state-id");

		// Then
		assertThat(state.isPresent(), is(false));
	}

	@Test
	public void shouldNotGetSpeechletStateGivenNoRegisteredStates() {
		// Given
		Flow flow = flowBuilder()
				.initialStateId("state1")
				.build();

		// When
		Optional<State<SpeechletStateAction>> state = flow.getSpeechletState("some-unknown-state-id");

		// Then
		assertThat(state.isPresent(), is(false));
	}

	@Test
	public void shouldGetInitialState() {
		// Given
		Flow flow = flowBuilder()
				.speechletState(STATE_1)
				.speechletState(STATE_2)
				.initialStateId("state1")
				.build();

		// When
		Optional<State<SpeechletStateAction>> state = flow.getInitialState();

		// Then
		assertThat(state.get(), equalTo(STATE_1));
	}

	@Test
	public void shouldNotGetInitialStateGivenInitialStateIsInvalidForFlow() {
		// Given
		Flow flow = flowBuilder()
				.speechletState(STATE_1)
				.speechletState(STATE_2)
				.initialStateId("some-unknown-state-id")
				.build();

		// When
		Optional<State<SpeechletStateAction>> state = flow.getInitialState();

		// Then
		assertThat(state.isPresent(), is(false));
	}

	@Test
	public void shouldNotGetInitialStateGivenNoRegisteredStates() {
		// Given
		Flow flow = flowBuilder()
				.initialStateId("state1")
				.build();

		// When
		Optional<State<SpeechletStateAction>> state = flow.getInitialState();

		// Then
		assertThat(state.isPresent(), is(false));
	}

	@Test
	public void shouldBuild() {
		Flow flow = flowBuilder()
				.speechletState(State.<SpeechletStateAction>stateBuilder()
						.id("page1")
						// .action()
						.transition(transitionBuilder()
								.onIntent("formwardIntent")
								.to("page2")
								.build())
						.transition(transitionBuilder()
								.onIntent("leftIntent")
								.to("page3")
								.build())
						.action(mock(SpeechletStateAction.class))
						.build())
				.speechletState(State.<SpeechletStateAction>stateBuilder()
						.id("page2")
						// .action()
						.transition(transitionBuilder()
								.onIntent("backIntent")
								.to("page1")
								.build())
						.action(mock(SpeechletStateAction.class))
						.build())
				.speechletState(State.<SpeechletStateAction>stateBuilder()
						.id("page3")
						// .action()
						.transition(transitionBuilder()
								.onIntent("backIntent")
								.to("page1")
								.build())
						.transition(transitionBuilder()
								.onIntent("forwardIntent")
								.to("page4")
								.build())
						.action(mock(SpeechletStateAction.class))
						.build())
				.speechletState(State.<SpeechletStateAction>stateBuilder()
						.id("page4")
						// .action()
						.action(mock(SpeechletStateAction.class))
						.build())
				.initialStateId("page1")
				.build();
	}

}
