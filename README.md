[![Build Status](https://travis-ci.org/ClaritySoftwareSolutions/spring-alexa-flow.svg?branch=master)](https://travis-ci.org/ClaritySoftwareSolutions/spring-alexa-flow)

----
# spring-alexa-flow
### Java library providing Alexa skill flow control
Library that aims to make it easier to create conversational or flow based Amazon Alexa lambda skills in Java.

### Motivation
The Amazon Alexa lambda java API provides classes and interfaces to respond to Alexa intents. The primary interface for this is 
`com.amazon.speech.speechlet.SpeechletV2` which defines 4 lifecycle methods, with the method to handle intent invocations being 
`onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope)`.

The `requestEnvelope` passed into `onIntent` contains the `com.amazon.speech.slu.Intent` which in turn contains the intent name. There is
however no API to determine the previous intent invocation, or if the intent is valid based on the last intent invocation. For example,
the intent 'YES' may only be valid if the previous intent asked a yes or no question. The 'YES' intent makes no sense if the previous intent
asked the question 'What is your favourite colour' for example.  
Intents are defined in the Alexa Developer portal for the skill, but there is no syntax to configure intent dependencies or relationships 
between other intents.

Typical implementations of the `onIntent` method will include code to manage this problem themselves. There are methods on the 
`com.amazon.speech.speechlet.Session` object to hold and retrieve state, but having to manage this state and flow logic in each project is
essentially boilerplate code which this library aims to remove.

### Simple State Machine
Managing state and flow control logic is commonly solved with a state machine, and that is how this library addresses the problem. The library
provides classes to define the flow of the skill, and a simple state machine implementation to manage the flow control.  

### Flow classes
The flow of the skill is defined with the following classes:
* `uk.co.claritysoftware.alexa.flow.model.Flow`  
The top level class. An Alexa skill has one `Flow`.  
A `Flow` contains 1 `State` which is the `Flow`'s initial state, and a Set of `States`s which are the `Flow`'s remaining states.
* `uk.co.claritysoftware.alexa.flow.model.State`  
A `State` contains 1 `StateAction` which is invoked on entering the state, and a Set of `Transition`s describing the transitions away from the state.
* `uk.co.claritysoftware.alexa.flow.model.Transition`  
A `Transition` contains the intent name that triggers this `Transition`, and the id of the target `State` to transition to.
* `uk.co.claritysoftware.alexa.flow.action.StateAction`  
A `StateAction` implements the `doAction` method which is invoked on entering the containing `State`.

The `Flow`, `State` and `Transition` classes have builders to aid creation of a `Flow` in a fluent manner.

### SpeechletV2 implementation
An implementation of `com.amazon.speech.speechlet.SpeechletV2` is provided which manages the flow of the skill using the simple state machine
and the `Flow`.

### Usage
The minimum requirement is for a simple spring context configuration in the file `application-context.xml` on the classpath. The context needs to 
define a bean `uk.co.claritysoftware.alexa.flow.speech.FlowSpeechlet` whose constructor argument is a `uk.co.claritysoftware.alexa.flow.model.Flow`.  
The API does not currently support creating a `Flow` bean using xml, so it is sometimes easier to define a small factory bean that is responsible
for creating the `Flow`

##### Example Flow Factory returning a `Flow` built with the fluent API
```java
package your.skill;
  
public class FlowFactory {
	public static Flow flow() {
		return flowBuilder()
				.initialState(stateBuilder()
						.id("initialState")
						.action(new LaunchAction())
						.transition(transitionBuilder()
								.onIntent("forwardIntent")
								.to("room1")
								.build())
						.build())
				.intentState(stateBuilder()
						.id("room1")
						.action(new room1Action())
						.transition(transitionBuilder()
								.onIntent("forwardIntent")
								.to("room2")
								.build())
						.transition(transitionBuilder()
								.onIntent("leftIntent")
								.to("room3")
								.build())
						.build())
				.intentState(stateBuilder()
						.id("room2")
						.action(new room2Action())
						.transition(transitionBuilder()
								.onIntent("backIntent")
								.to("room1")
								.build())
						.build())
				.intentState(stateBuilder()
						.id("room3")
						.action(new room3Action())
						.build())
				.build();
	}
}
```

##### Example `/applications-content.xml`
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.3.xsd">
  
	<bean class="uk.co.claritysoftware.alexa.flow.speech.FlowSpeechlet">
		<constructor-arg ref="flow" />
	</bean>
  
	<bean class="your.skill.FlowFactory" id="flow" factory-method="flow" />
</beans>
```

##### Example `StateAction` implementation
```java
public class Page1Action implements StateAction {
  
	@Override
	public SpeechletResponse doAction(SpeechletRequestEnvelope intentRequestSpeechletRequestEnvelope) {
		Reprompt reprompt = new Reprompt();
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
		repromptSpeech.setText("What would you like to do?");
		reprompt.setOutputSpeech(repromptSpeech);
  
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText("You are in room 1. There is a door to your left, or you can go forward. What would you like to do?");
  
		SpeechletResponse response = new SpeechletResponse();
		response.setOutputSpeech(outputSpeech);
		response.setReprompt(reprompt);
		response.setShouldEndSession(false);
  
		return response;
	}
}
```

#### POM dependencies
THe only required POM dependency is of this library. 
```xml
	<dependency>
		<groupId>uk.co.claritysoftware</groupId>
		<artifactId>spring-alexa-flow</artifactId>
		<version>1.0.0</version>
	</dependency>
```
The Spring and Amazon dependencies will be brought into your project transitively with the following versions:
```xml
	<spring.version>4.3.8.RELEASE</spring.version>
	<alexa-skills-kit.version>1.3.1</alexa-skills-kit.version>
	<aws-lambda-java-core.version>1.1.0</aws-lambda-java-core.version>
	<aws-lambda-java-log4j.version>1.0.0</aws-lambda-java-log4j.version>
```
If your project requires a different version of any of these dependencies, you can override these properties in your POM.

### Change Log
* 1.0.0 Initial release

----
Copyright &copy; 2017 [Clarity Software Solutions Limited](https://claritysoftware.co.uk)

