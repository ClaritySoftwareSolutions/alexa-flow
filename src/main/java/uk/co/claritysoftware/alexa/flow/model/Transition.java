package uk.co.claritysoftware.alexa.flow.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Class describing a transition away from a {@link State} expressed through the intent name that should trigger it,
 * and the id of the target speechlet state
 */
@Value
@Builder(builderMethodName = "transitionBuilder")
public class Transition {

	/**
	 * The intent name that should trigger this transition
	 */
	@NonNull
	private String onIntent;

	/**
	 * The id of the target {@link State} of this transition
	 */
	@NonNull
	private String to;
}
