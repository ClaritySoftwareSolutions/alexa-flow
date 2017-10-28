package uk.co.claritysoftware.alexa.flow;

import static java.lang.String.format;

import java.util.Optional;
import uk.co.claritysoftware.alexa.flow.action.StateAction;
import uk.co.claritysoftware.alexa.flow.model.Flow;
import uk.co.claritysoftware.alexa.flow.model.State;
import uk.co.claritysoftware.alexa.flow.model.Transition;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple state machine to encapsulate and reflect the state of a {@link Flow}.
 *
 * State changed can be triggered by calling {@link SimpleFlowStateMachine#send(String)}
 */
@Slf4j
public class SimpleFlowStateMachine {

	private final Flow flow;

	private String currentStateId;

	public SimpleFlowStateMachine(Flow flow) {
		this.flow = flow;
		this.currentStateId = flow.getInitialState().getId();
	}

	public SimpleFlowStateMachine(Flow flow, String currentStateId) {
		this.flow = flow;
		flow.getState(currentStateId)
				.orElseThrow(() -> new IllegalStateException(format("Cannot instantiate SimpleFlowStateMachine with state %s in flow %s", currentStateId, flow)));
		this.currentStateId = currentStateId;
	}

	/**
	 * @return the {@link StateAction} associated with the current state
	 */
	public StateAction getAction() {
		return getState().getAction();
	}

	/**
	 * @return the current {@link State}
	 */
	public State getState() {
		return flow.getState(currentStateId)
				.orElseThrow(() -> new IllegalStateException(format("Cannot get state with %s from flow %s", currentStateId, flow)));
	}

	/**
	 * Triggers a state change based on the specified intent name
	 *
	 * @param intent
	 */
	public void send(String intent) {
		log.debug("Sending intent {} to state {}", intent, currentStateId);

		Optional<Transition> transition = flow.getState(currentStateId).get().getTransitions().stream()
				.filter(t -> t.getOnIntent().equals(intent))
				.findFirst();

		if (transition.isPresent()) {
			transitionToNewState(transition.get());
		} else {
			log.warn("Could not find transition for intent {} on state {}. State not changed.", intent, currentStateId);
		}
	}

	private void transitionToNewState(Transition transition) {
		flow.getState(transition.getTo())
				.ifPresent(state -> this.currentStateId = state.getId());
	}
}
