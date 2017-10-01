package uk.co.claritysoftware.alexa.flow.model;

import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.co.claritysoftware.alexa.flow.model.Transition.transitionBuilder;

import java.util.Optional;
import org.junit.Test;
import uk.co.claritysoftware.alexa.flow.action.IntentSpeechletStateAction;

/**
 * Unit test class for {@link State}
 */
public class StateTest {

	private static final Transition TRANSITION_1 = transitionBuilder()
			.onIntent("intent1")
			.to("some-state")
			.build();

	private static final Transition TRANSITION_2 = transitionBuilder()
			.onIntent("intent2")
			.to("other-state")
			.build();

	@Test
	public void shouldGetTransition() {
		// Given
		State<IntentSpeechletStateAction> state = State.<IntentSpeechletStateAction>stateBuilder()
				.id("state1")
				.transition(TRANSITION_1)
				.transition(TRANSITION_2)
				.action(mock(IntentSpeechletStateAction.class))
				.build();

		// When
		Optional<Transition> transition = state.getTransition("intent2");

		// Then
		assertThat(transition.get(), equalTo(TRANSITION_2));
	}

	@Test
	public void shouldNotGetTransitionGivenUnknownIntentName() {
		// Given
		State<IntentSpeechletStateAction> state = State.<IntentSpeechletStateAction>stateBuilder()
				.id("state1")
				.transition(TRANSITION_1)
				.transition(TRANSITION_2)
				.action(mock(IntentSpeechletStateAction.class))
				.build();

		// When
		Optional<Transition> transition = state.getTransition("some-unknown-intent-name");

		// Then
		assertThat(transition.isPresent(), is(false));
	}

	@Test
	public void shouldNotGetTransitionGivenNoRegisteredTransitions() {
		// Given
		State<IntentSpeechletStateAction> state = State.<IntentSpeechletStateAction>stateBuilder()
				.transitions(EMPTY_LIST)
				.action(mock(IntentSpeechletStateAction.class))
				.id("state1").build();

		// When
		Optional<Transition> transition = state.getTransition("some-unknown-state-id");

		// Then
		assertThat(transition.isPresent(), is(false));
	}

}
