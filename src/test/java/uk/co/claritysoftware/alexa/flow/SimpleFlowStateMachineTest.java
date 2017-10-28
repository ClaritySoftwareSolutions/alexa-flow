package uk.co.claritysoftware.alexa.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static uk.co.claritysoftware.alexa.flow.model.Flow.flowBuilder;
import static uk.co.claritysoftware.alexa.flow.model.State.stateBuilder;
import static uk.co.claritysoftware.alexa.flow.model.Transition.transitionBuilder;

import org.junit.Before;
import org.junit.Test;
import uk.co.claritysoftware.alexa.flow.action.StateAction;
import uk.co.claritysoftware.alexa.flow.model.Flow;
import uk.co.claritysoftware.alexa.flow.model.State;

/**
 * Unit test class for {@link SimpleFlowStateMachine}
 */
public class SimpleFlowStateMachineTest {

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

	private SimpleFlowStateMachine stateMachine;

	@Before
	public void setup() {
		stateMachine = new SimpleFlowStateMachine(FLOW);
	}

	@Test
	public void shouldGetState() {
		// Given

		// When
		State state = stateMachine.getState();

		// Then
		assertThat(state).isEqualTo(INITIAL_STATE);
	}

	@Test
	public void shouldGetAction() {
		// Given

		// When
		StateAction stateAction = stateMachine.getAction();

		// Then
		assertThat(stateAction).isEqualTo(INITIAL_STATE_ACTION);
	}

	@Test
	public void shouldSendGivenIntentName() {
		// Given
		String intentName = FORWARD_INTENT;

		// When
		stateMachine.send(intentName);

		// Then
		assertThat(stateMachine.getAction()).isEqualTo(STATE_1_ACTION);
	}

	@Test
	public void shouldSendGivenInvalidIntentName() {
		// Given
		String intentName = "some invalid intent";

		// When
		stateMachine.send(intentName);

		// Then
		assertThat(stateMachine.getAction()).isEqualTo(INITIAL_STATE_ACTION);
	}

	@Test
	public void shouldConstructWithCurrentStateId() {
		// Given

		// When
		stateMachine = new SimpleFlowStateMachine(FLOW, STATE_1_ID);

		// Then
		assertThat(stateMachine.getState()).isEqualTo(STATE_1);
	}

	@Test
	public void shouldFailToConstructGivenInvalidStateId() {
		// Given
		String currentStateId = "some-invalid-state-id";

		// When
		try {
			new SimpleFlowStateMachine(FLOW, currentStateId);

			fail("Was expecting an IllegalStateException");
		}
		// Then
		catch (IllegalStateException e) {
			assertThat(e.getMessage()).startsWith("Cannot instantiate SimpleFlowStateMachine with state some-invalid-state-id in flow");
		}
	}
}
