package uk.co.claritysoftware.alexa.flow.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.claritysoftware.alexa.flow.model.Flow.flowBuilder;
import static uk.co.claritysoftware.alexa.flow.model.Transition.transitionBuilder;

import java.util.Optional;
import org.junit.Test;
import uk.co.claritysoftware.alexa.flow.action.SpeechletStateAction;

/**
 * Unit test class for {@link Flow}
 */
public class FlowTest {

	@Test
	public void shouldGetSpeechletState() {
		// Given
		State<SpeechletStateAction> state1 = State.<SpeechletStateAction>stateBuilder()
				.id("state1").build();
		State<SpeechletStateAction> state2 = State.<SpeechletStateAction>stateBuilder()
				.id("state2").build();
		Flow flow = flowBuilder()
				.speechletState(state1)
				.speechletState(state2)
				.build();

		// When
		Optional<State<SpeechletStateAction>> state = flow.getSpeechletState("state1");

		// Then
		assertThat(state.get(), equalTo(state1));
	}

	@Test
	public void shouldNotGetSpeechletStateGivenUnknownStateId() {
		// Given
		State<SpeechletStateAction> state1 = State.<SpeechletStateAction>stateBuilder()
				.id("state1").build();
		State<SpeechletStateAction> state2 = State.<SpeechletStateAction>stateBuilder()
				.id("state2").build();
		Flow flow = flowBuilder()
				.speechletState(state1)
				.speechletState(state2)
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
				.build();

		// When
		Optional<State<SpeechletStateAction>> state = flow.getSpeechletState("some-unknown-state-id");

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
						.build())
				.speechletState(State.<SpeechletStateAction>stateBuilder()
						.id("page2")
						// .action()
						.transition(transitionBuilder()
								.onIntent("backIntent")
								.to("page1")
								.build())
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
						.build())
				.speechletState(State.<SpeechletStateAction>stateBuilder()
						.id("page4")
						// .action()
						.build())
				.build();
	}

}
