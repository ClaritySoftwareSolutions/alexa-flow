package uk.co.claritysoftware.alexa.testsupport;

import java.util.Map;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletRequest;

/**
 * Test Data Factory for {@link SpeechletRequestEnvelope} instances
 */
public class SpeechletRequestEnvelopeTestDataFactory<T extends SpeechletRequest> {

	private String version = "1.0";

	private Context context = Context.builder().build();

	private Session session = Session.builder().withSessionId("67890").build();

	private T request;

	private SpeechletRequestEnvelopeTestDataFactory() {

	}

	public static <T extends SpeechletRequest> SpeechletRequestEnvelopeTestDataFactory speechletRequestEnvelope() {
		return new SpeechletRequestEnvelopeTestDataFactory<T>();
	}

	public SpeechletRequestEnvelopeTestDataFactory withVersion(String version) {
		this.version = version;
		return this;
	}

	public SpeechletRequestEnvelopeTestDataFactory withContext(Context context) {
		this.context = context;
		return this;
	}

	public SpeechletRequestEnvelopeTestDataFactory withSession(Session session) {
		this.session = session;
		return this;
	}

	public SpeechletRequestEnvelopeTestDataFactory withRequest(T request) {
		this.request = request;
		return this;
	}

	public SpeechletRequestEnvelope<T> build() {
		return SpeechletRequestEnvelope.<T>builder()
				.withVersion(this.version)
				.withSession(this.session)
				.withRequest(this.request)
				.withContext(this.context)
				.build();
	}

	public static SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelopeWithIntentName(final String intentName) {
		return speechletRequestEnvelope()
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName(intentName)
								.build())
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelopeWithIntentNameAndSlots(final String intentName, final Map<String, Slot> slots) {
		return speechletRequestEnvelope()
				.withRequest(IntentRequest.builder()
						.withRequestId("12345")
						.withIntent(Intent.builder()
								.withName(intentName)
								.withSlots(slots)
								.build())
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<SessionStartedRequest> sessionStartedSpeechletRequestEnvelope() {
		return speechletRequestEnvelope()
				.withRequest(SessionStartedRequest.builder()
						.withRequestId("12345")
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<SessionStartedRequest> sessionStartedSpeechletRequestEnvelope(final Session session) {
		return speechletRequestEnvelope()
				.withSession(session)
				.withRequest(SessionStartedRequest.builder()
						.withRequestId("12345")
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<LaunchRequest> launchSpeechletRequestEnvelope() {
		return speechletRequestEnvelope()
				.withRequest(LaunchRequest.builder()
						.withRequestId("12345")
						.build())
				.build();
	}

	public static SpeechletRequestEnvelope<LaunchRequest> launchSpeechletRequestEnvelopeWithSession(final Session session) {
		return speechletRequestEnvelope()
				.withSession(session)
				.withRequest(LaunchRequest.builder()
						.withRequestId("12345")
						.build())
				.build();
	}

}
