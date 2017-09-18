package uk.co.claritysoftware.alexa.flow.model;

import lombok.Builder;
import lombok.Data;

/**
 * Class describing a transition away from a {@link State} expressed through the intent name that should trigger it,
 * and the id of the target speechlet state
 *
 * Deliberately not immutable so that setters are available for bean creation in xml if required
 */
@Data
@Builder(builderMethodName = "transitionBuilder")
public class Transition {

	/**
	 * The intent name that should trigger this transition
	 */
	private String onIntent;

	/**
	 * The id of the target {@link State} of this transition
	 */
	private String to;
}
