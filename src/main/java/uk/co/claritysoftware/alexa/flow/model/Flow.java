package uk.co.claritysoftware.alexa.flow.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import uk.co.claritysoftware.alexa.flow.action.FlowNotLaunchedAction;
import uk.co.claritysoftware.alexa.flow.action.IntentSpeechletStateAction;
import uk.co.claritysoftware.alexa.flow.action.LaunchSpeechletStateAction;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

/**
 * Top level flow class, encapsulating the definition of the flow.
 *
 * The flow contains the initial state that is triggered by {@code onLaunch} of the speechlet handler,
 * and a set of intent states that are triggered by {@code onIntent} of the speechlet hander.
 */
@Value
@Builder(builderMethodName = "flowBuilder")
public class Flow {

	/**
	 * The {@link State<LaunchSpeechletStateAction> state} that is entered when the flow starts
	 */
	@NonNull
	private State<LaunchSpeechletStateAction> initialState;

	/**
	 * The set of {@link State<IntentSpeechletStateAction> states} that this flow is composed of
	 */
	@Singular
	@NonNull
	private Set<State<IntentSpeechletStateAction>> intentStates;

	/**
	 * The {@link FlowNotLaunchedAction action} that is triggered when the flow is started with an intent,
	 * IE. before the {@link Flow#initialState} has been entered.
	 */
	@NonNull
	private FlowNotLaunchedAction flowNotLaunchedAction;

	/**
	 * Finds and returns the {@link State<IntentSpeechletStateAction>} from this flow based on its id,
	 * returned in an {@link Optional}
	 *
	 * @param id the id of the {@link State<IntentSpeechletStateAction>} to return
	 * @return an {@link Optional} containing either the found {@link State<IntentSpeechletStateAction>} or null if not found
	 */
	public Optional<State<IntentSpeechletStateAction>> getIntentState(final String id) {
		Set<State> allStates = new HashSet<>();
		allStates.addAll(intentStates);
		allStates.add(initialState);

		return Optional.ofNullable(allStates.stream()
				.filter(state -> state.getId().equals(id))
				.findFirst()
				.orElse(null));
	}
}
