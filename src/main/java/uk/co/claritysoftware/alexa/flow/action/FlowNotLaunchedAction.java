package uk.co.claritysoftware.alexa.flow.action;

/**
 * Abstract class used by the flow speechlet's intent handler when the session does not contain the current state.
 * IE. the Alexa skill has been started with an intent directly, which does not trigger onLaunch
 */
public abstract class FlowNotLaunchedAction implements IntentSpeechletStateAction {

}
