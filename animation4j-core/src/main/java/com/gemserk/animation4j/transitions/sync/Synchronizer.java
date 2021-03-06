package com.gemserk.animation4j.transitions.sync;

import java.util.ArrayList;

import com.gemserk.animation4j.time.SystemTimeProvider;
import com.gemserk.animation4j.time.UpdateableTimeProvider;
import com.gemserk.animation4j.transitions.Transition;
import com.gemserk.animation4j.transitions.event.TransitionEventHandler;

/**
 * Provides different methods to register Transitions to be updated and/or monitored to call different event handlers.
 * 
 * @author acoppes
 * 
 */
@SuppressWarnings("rawtypes")
public class Synchronizer {

	class TransitionUpdater {

		// Should be a set of transitions?
		
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		ArrayList<Transition> toRemoveTransitions = new ArrayList<Transition>();

		public void update(float delta) {
			for (int i = 0; i < transitions.size(); i++) {
				Transition transition = transitions.get(i);
				transition.update(delta);
				if (transition.isFinished())
					toRemoveTransitions.add(transition);
			}

			if (toRemoveTransitions.isEmpty())
				return;

			for (int i = 0; i < toRemoveTransitions.size(); i++)
				transitions.remove(toRemoveTransitions.get(i));

			toRemoveTransitions.clear();
		}

		public void add(Transition transition) {
			transitions.add(transition);
		}

	}

	private TransitionHandlersManager transitionHandlersManager = new TransitionHandlersManager();

	/**
	 * Used internally to synchronize correctly when calling synchronize(delta) method, a restriction however is it will only work for all Transitions registered using a TransitionBuilder.
	 */
	private UpdateableTimeProvider timeProvider = new UpdateableTimeProvider();
	private SystemTimeProvider systemTimeProvider = new SystemTimeProvider();

	private TransitionUpdater transitionUpdater;

	private float lastTime;

	public Synchronizer() {
		lastTime = systemTimeProvider.getTime();
		transitionUpdater = new TransitionUpdater();
	}

	/**
	 * Performs the synchronization of all the objects with the corresponding registered transitions, it uses a delta based on the last system time.
	 */
	public void synchronize() {
		float currentTime = systemTimeProvider.getTime();
		float delta = currentTime - lastTime;
		synchronize(delta);
		lastTime = currentTime;
	}

	/**
	 * Performs a synchronization of all internal objects with the corresponding registered transitions using the specified delta. It will only work (for now) for those transitions registered using a TransitionBuilder.
	 * 
	 * @param delta
	 *            The delta time in Seconds to use to synchronize.
	 */
	public void synchronize(float delta) {
		timeProvider.update(delta);
		transitionUpdater.update(delta);
		transitionHandlersManager.update();
	}

	/**
	 * Adds the transition to be updated by the synchronizer.
	 * 
	 * @param transition
	 *            The transition to be registered.
	 */
	public void transition(Transition transition) {
		transitionUpdater.add(transition);
	}

	/**
	 * Convenient method which to avoid calling transition(transition) and then monitor(transition, transitionEventHandler).
	 * 
	 * @param transition
	 *            The Transition to be registered.
	 * @param transitionEventHandler
	 *            The TransitionEventHandler to be called when the Transition changes its state.
	 */
	public void transition(Transition transition, TransitionEventHandler transitionEventHandler) {
		transition(transition);
		monitor(transition, transitionEventHandler);
	}

	/**
	 * Monitors the specified Transition by calling the specified TransitionEventHandler whenever the Transition changes its state.
	 * 
	 * @param transition
	 *            The Transition to be monitored.
	 * @param transitionEventHandler
	 *            The TransitionEventHandler to be called when the Transition changes its state.
	 */
	public void monitor(Transition transition, TransitionEventHandler transitionEventHandler) {
		transitionHandlersManager.handle(transition, transitionEventHandler);
	}

}