package uk.co.claritysoftware.alexa.flow.model;

import java.util.Optional;
import java.util.Set;
import uk.co.claritysoftware.alexa.flow.action.StateAction;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * Class describing a state or point within an Alexa skill flow with a {@link StateAction} of type {@link T}
 *
 * Deliberately not immutable so that setters are available for bean creation in xml if required
 */
@Data
@Builder(builderMethodName = "stateBuilder")
public class State<T extends StateAction> {

	/**
	 * The id of this state, such that it can be referenced
	 */
	private String id;

	/**
	 * The {@link StateAction<T>} that should be executed on entering this state
	 */
	private T action;

	/**
	 * The set of {@link Transition transitions} that this state can transition or move to
	 */
	@Singular
	private Set<Transition> transitions;

	/**
	 * Finds and returns the {@link Transition} from this state based on its intent name,
	 * returned in an {@link Optional}
	 *
	 * @param intentName the intent name of the {@link Transition} to return
	 * @return an {@link Optional} containing either the found {@link Transition} or null if not found
	 */
	public Optional<Transition> getTransition(final String intentName) {
		return Optional.ofNullable(transitions.stream()
				.filter(transition -> intentName.equals(transition.getOnIntent()))
				.findFirst()
				.orElse(null));
	}

}
