package com.gemserk.animation4j.examples;

import com.gemserk.animation4j.converters.Converters;
import com.gemserk.animation4j.interpolator.function.InterpolationFunction;
import com.gemserk.animation4j.interpolator.function.InterpolationFunctions;
import com.gemserk.animation4j.transitions.Transition;
import com.gemserk.animation4j.transitions.Transitions;

public class TransitionsExample {

	public static void main(String[] args) throws InterruptedException {
		// register a type converter for Vector2f class.
		Converters.register(Vector2f.class, new Vector2fConverter());

		System.out.println("Tests how to update a transition");
		testWithUserTimeProvider();
	}

	protected static void testWithUserTimeProvider() {
		InterpolationFunction[] functions = { InterpolationFunctions.easeOut(), InterpolationFunctions.easeIn() };

		// In this case, we are using a custom time provider.
		Transition<Vector2f> transition = Transitions.transitionBuilder(new Vector2f(0f, 0f)).functions(functions).build();
		
		// you can build transitions using default linear interpolation functions.
		Transition<Vector2f> anotherTransition = Transitions.transitionBuilder(new Vector2f(0f, 0f)).build();

		System.out.println("Transition value: " + transition.get());

		transition.set(new Vector2f(100f, 100f), 500);

		System.out.println("Transition value: " + transition.get());
		transition.update(300);
		System.out.println("Transition value: " + transition.get());
		transition.update(200);
		System.out.println("Transition value: " + transition.get());

		transition.set(new Vector2f(200f, 200f), 0);
		System.out.println("Transition value: " + transition.get());
	}

}
