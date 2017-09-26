package uk.co.claritysoftware.alexa.flow.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.co.claritysoftware.alexa.flow.model.Flow.flowBuilder;
import static uk.co.claritysoftware.alexa.flow.model.Transition.transitionBuilder;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import uk.co.claritysoftware.alexa.flow.action.FlowNotLaunchedAction;
import uk.co.claritysoftware.alexa.flow.action.IntentSpeechletStateAction;
import uk.co.claritysoftware.alexa.flow.action.LaunchSpeechletStateAction;

/**
 * Unit test class for {@link Flow}
 */
public class FlowTest {

	private static final State<LaunchSpeechletStateAction> INITIAL_STATE = State.<LaunchSpeechletStateAction>stateBuilder()
			.id("launch-state")
			.action(mock(LaunchSpeechletStateAction.class))
			.build();

	private static final State<IntentSpeechletStateAction> STATE_1 = State.<IntentSpeechletStateAction>stateBuilder()
			.id("state1")
			.action(mock(IntentSpeechletStateAction.class))
			.build();

	private static final State<IntentSpeechletStateAction> STATE_2 = State.<IntentSpeechletStateAction>stateBuilder()
			.id("state2")
			.action(mock(IntentSpeechletStateAction.class))
			.build();

	private static final FlowNotLaunchedAction FLOW_NOT_LAUNCHED_ACTION = mock(FlowNotLaunchedAction.class);

	private Flow flow;

	@Before
	public void setup() {
		flow = flowBuilder()
				.initialState(INITIAL_STATE)
				.intentState(STATE_1)
				.intentState(STATE_2)
				.flowNotLaunchedAction(FLOW_NOT_LAUNCHED_ACTION)
				.build();
	}

	@Test
	public void shouldGetIntentState() {
		// Given

		// When
		Optional<State<IntentSpeechletStateAction>> state = flow.getIntentState("state1");

		// Then
		assertThat(state.get(), equalTo(STATE_1));
	}

	@Test
	public void shouldNotGetIntentStateGivenUnknownStateId() {
		// Given

		// When
		Optional<State<IntentSpeechletStateAction>> state = flow.getIntentState("some-unknown-state-id");

		// Then
		assertThat(state.isPresent(), is(false));
	}

	@Test
	public void shouldNotGetIntentStateGivenNoRegisteredStates() {
		// Given
		Flow flow = flowBuilder()
				.initialState(INITIAL_STATE)
				.flowNotLaunchedAction(FLOW_NOT_LAUNCHED_ACTION)
				.build();

		// When
		Optional<State<IntentSpeechletStateAction>> state = flow.getIntentState("some-unknown-state-id");

		// Then
		assertThat(state.isPresent(), is(false));
	}

	@Test
	public void shouldGetInitialState() {
		// Given

		// When
		State<LaunchSpeechletStateAction> state = flow.getInitialState();

		// Then
		assertThat(state, equalTo(INITIAL_STATE));
	}

	@Test
	public void shouldBuild() {
		Flow flow = flowBuilder()
				.initialState(State.<LaunchSpeechletStateAction>stateBuilder()
						.id("initial-state")
						.transition(transitionBuilder()
								.onIntent("forwardIntent").to("page1")
								.build())
						.action(mock(LaunchSpeechletStateAction.class))
						.build())
				.intentState(State.<IntentSpeechletStateAction>stateBuilder()
						.id("page1")
						// .action()
						.transition(transitionBuilder()
								.onIntent("formwardIntent").to("page2")
								.build())
						.transition(transitionBuilder()
								.onIntent("leftIntent").to("page3")
								.build())
						.action(mock(IntentSpeechletStateAction.class))
						.build())
				.intentState(State.<IntentSpeechletStateAction>stateBuilder()
						.id("page2")
						// .action()
						.transition(transitionBuilder()
								.onIntent("backIntent").to("page1")
								.build())
						.action(mock(IntentSpeechletStateAction.class))
						.build())
				.intentState(State.<IntentSpeechletStateAction>stateBuilder()
						.id("page3")
						// .action()
						.transition(transitionBuilder()
								.onIntent("backIntent").to("page1")
								.build())
						.transition(transitionBuilder()
								.onIntent("forwardIntent").to("page4")
								.build())
						.action(mock(IntentSpeechletStateAction.class))
						.build())
				.intentState(State.<IntentSpeechletStateAction>stateBuilder()
						.id("page4")
						.action(mock(IntentSpeechletStateAction.class))
						.build())
				.flowNotLaunchedAction(FLOW_NOT_LAUNCHED_ACTION)
				.build();
	}

}
