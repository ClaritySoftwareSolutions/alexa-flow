package uk.co.claritysoftware.alexa.flow.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.co.claritysoftware.alexa.flow.model.Flow.flowBuilder;
import static uk.co.claritysoftware.alexa.flow.model.State.stateBuilder;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import uk.co.claritysoftware.alexa.flow.action.StateAction;

/**
 * Unit test class for {@link Flow}
 */
public class FlowTest {

	private static final State INITIAL_STATE = stateBuilder()
			.id("initial-state")
			.action(mock(StateAction.class))
			.build();

	private static final State STATE_1 = stateBuilder()
			.id("state1")
			.action(mock(StateAction.class))
			.build();

	private static final State STATE_2 = stateBuilder()
			.id("state2")
			.action(mock(StateAction.class))
			.build();

	private Flow flow;

	@Before
	public void setup() {
		flow = flowBuilder()
				.initialState(INITIAL_STATE)
				.intentState(STATE_1)
				.intentState(STATE_2)
				.build();
	}

	@Test
	public void shouldGetState() {
		// Given

		// When
		Optional<State> state = flow.getState("state1");

		// Then
		assertThat(state.get(), equalTo(STATE_1));
	}

	@Test
	public void shouldGetStateGivenIdIsForIntiialState() {
		// Given

		// When
		Optional<State> state = flow.getState("initial-state");

		// Then
		assertThat(state.get(), equalTo(INITIAL_STATE));
	}

	@Test
	public void shouldNotGetStateGivenUnknownStateId() {
		// Given

		// When
		Optional<State> state = flow.getState("some-unknown-state-id");

		// Then
		assertThat(state.isPresent(), is(false));
	}

	@Test
	public void shouldNotGetStateGivenNoRegisteredStates() {
		// Given
		Flow flow = flowBuilder()
				.initialState(INITIAL_STATE)
				.build();

		// When
		Optional<State> state = flow.getState("some-unknown-state-id");

		// Then
		assertThat(state.isPresent(), is(false));
	}

	@Test
	public void shouldGetInitialState() {
		// Given

		// When
		State state = flow.getInitialState();

		// Then
		assertThat(state, equalTo(INITIAL_STATE));
	}

}
