package uk.co.claritysoftware.alexa.flow.model;

import java.util.Optional;
import java.util.Set;
import uk.co.claritysoftware.alexa.flow.action.SpeechletStateAction;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

/**
 * Top level flow class, encapsulating the definition of the flow.
 */
@Value
@Builder(builderMethodName = "flowBuilder")
public class Flow {

	@NonNull
	private String initialStateId;

	/**
	 * The set of {@link State<SpeechletStateAction> states} that this flow is composed of
	 */
	@Singular
	@NonNull
	private Set<State<SpeechletStateAction>> speechletStates;

	/**
	 * Finds and returns the {@link State<SpeechletStateAction>} from this flow based on its id,
	 * returned in an {@link Optional}
	 *
	 * @param id the id of the {@link State<SpeechletStateAction>} to return
	 * @return an {@link Optional} containing either the found {@link State<SpeechletStateAction>} or null if not found
	 */
	public Optional<State<SpeechletStateAction>> getSpeechletState(final String id) {
		return Optional.ofNullable(speechletStates.stream()
				.filter(state -> state.getId().equals(id))
				.findFirst()
				.orElse(null));
	}

	/**
	 * Finds and returns the initial {@link State<SpeechletStateAction>} from this flow based on the flows initialStateId,
	 * returned in an {@link Optional}
	 *
	 * @return an {@link Optional} containing either the initial {@link State<SpeechletStateAction>} or null if not found
	 */
	public Optional<State<SpeechletStateAction>> getInitialState() {
		return getSpeechletState(initialStateId);
	}
}
