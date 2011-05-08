package com.gemserk.animation4j.examples;

import com.gemserk.animation4j.converters.Converters;
import com.gemserk.animation4j.transitions.Transition;
import com.gemserk.animation4j.transitions.Transitions;
import com.gemserk.animation4j.transitions.event.TransitionEventHandler;
import com.gemserk.animation4j.transitions.event.TransitionMonitor;
import com.gemserk.animation4j.transitions.event.TransitionMonitorProcessor;

public class TransitionEventHandlerExample {

	public static void main(String[] args) throws InterruptedException {
		// register a type converter for Vector2f class.
		Converters.register(Vector2f.class, new Vector2fConverter());
		testWithDefaultSystemProvider();
	}

	protected static void testWithDefaultSystemProvider() throws InterruptedException {
		Transition<Vector2f> transition = Transitions.transitionBuilder(new Vector2f(0f, 0f)).end(new Vector2f(100f, 100f)).time(500).build();
		
		TransitionMonitor transitionMonitor = new TransitionMonitor();
		transitionMonitor.monitor(transition);
		
		TransitionMonitorProcessor transitionMonitorProcessor = new TransitionMonitorProcessor();
		transitionMonitorProcessor.setTransitionMonitor(transitionMonitor);
		transitionMonitorProcessor.setTransitionEventHandler(new TransitionEventHandler<Vector2f>(){
			
			@Override
			public void onTransitionStarted(Transition<Vector2f> transition) {
				System.out.println("Transition started!!! - " + transition.get());
			}
			
			@Override
			public void onTransitionFinished(Transition<Vector2f> transition) {
				System.out.println("Transition finished!!! - " + transition.get());
			}
			
		});
		
		transitionMonitorProcessor.update();
		Thread.sleep(300);
		transitionMonitorProcessor.update();
		Thread.sleep(300);
		transitionMonitorProcessor.update();

	}


}