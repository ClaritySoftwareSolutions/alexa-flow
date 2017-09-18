package uk.co.claritysoftware.alexa.flow.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.claritysoftware.alexa.flow.model.Transition.transitionBuilder;

import java.util.Optional;
import org.junit.Test;
import uk.co.claritysoftware.alexa.flow.action.SpeechletStateAction;

/**
 * Unit test class for {@link State}
 */
public class StateTest {

	@Test
	public void shouldGetTransition() {
		// Given
		Transition transition1 = transitionBuilder()
				.onIntent("intent1").build();
		Transition transition2 = transitionBuilder()
				.onIntent("intent2").build();
		State<SpeechletStateAction> state = State.<SpeechletStateAction>stateBuilder()
				.id("state1")
				.transition(transition1)
				.transition(transition2)
				.build();

		// When
		Optional<Transition> transition = state.getTransition("intent2");

		// Then
		assertThat(transition.get(), equalTo(transition2));
	}

	@Test
	public void shouldNotGetTransitionGivenUnknownIntentName() {
		// Given
		Transition transition1 = transitionBuilder()
				.onIntent("intent1").build();
		Transition transition2 = transitionBuilder()
				.onIntent("intent2").build();
		State<SpeechletStateAction> state = State.<SpeechletStateAction>stateBuilder()
				.id("state1")
				.transition(transition1)
				.transition(transition2)
				.build();

		// When
		Optional<Transition> transition = state.getTransition("some-unknown-intent-name");

		// Then
		assertThat(transition.isPresent(), is(false));
	}

	@Test
	public void shouldNotGetTransitionGivenNoRegisteredTransitions() {
		// Given
		State<SpeechletStateAction> state = State.<SpeechletStateAction>stateBuilder()
				.id("state1").build();

		// When
		Optional<Transition> transition = state.getTransition("some-unknown-state-id");

		// Then
		assertThat(transition.isPresent(), is(false));
	}

}
