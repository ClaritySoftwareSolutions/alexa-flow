package uk.co.claritysoftware.alexa.flow.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
	 * The {@link State} that is entered when the flow starts
	 */
	@NonNull
	private State initialState;

	/**
	 * The set of {@link State states} that this flow is composed of
	 */
	@Singular
	@NonNull
	private Set<State> intentStates;

	/**
	 * Finds and returns the {@link State} from this flow based on its id, returned in an {@link Optional}.
	 * This method will search all intent states, and the initial state, and will return the first state that matches the specified id.
	 *
	 * @param id the id of the {@link State} to return
	 * @return an {@link Optional} containing either the found {@link State} or null if not found
	 */
	public Optional<State> getState(final String id) {
		Set<State> allStates = new HashSet<>();
		allStates.addAll(intentStates);
		allStates.add(initialState);

		return Optional.ofNullable(allStates.stream()
				.filter(state -> state.getId().equals(id))
				.findFirst()
				.orElse(null));
	}

}
