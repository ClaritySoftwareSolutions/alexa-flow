package uk.co.claritysoftware.alexa.flow.model;

import java.util.Optional;
import java.util.Set;
import uk.co.claritysoftware.alexa.flow.action.SpeechletStateAction;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * Top level flow class, encapsulating the definition of the flow.
 *
 * Deliberately not immutable so that setters are available for bean creation in xml if required
 */
@Data
@Builder(builderMethodName = "flowBuilder")
public class Flow {

	/**
	 * The set of {@link State<SpeechletStateAction> states} that this flow is composed of
	 */
	@Singular
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
				.filter(state -> id.equals(state.getId()))
				.findFirst()
				.orElse(null));
	}
}
