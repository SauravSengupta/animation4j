package com.gemserk.animation4j.interpolator;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsSame;
import org.junit.Test;

import com.gemserk.animation4j.interpolator.function.InterpolationFunctions;


public class InterpolatorProviderTest {

	@Test
	public void shouldInferTypeForRegisteredLinearInterpolator() {
		InterpolatorProvider interpolatorProvider = new InterpolatorProvider();
		
		Interpolator<Float> interpolator = Interpolators.floatInterpolator(InterpolationFunctions.linear());
		interpolatorProvider.register(Float.class, interpolator);
		Interpolator<Float> interpolatorInfered = interpolatorProvider.inferInterpolator(new Float(100f));
		
		assertThat(interpolatorInfered, IsSame.sameInstance(interpolator));
	}
	
}
