package com.gemserk.animation4j.timeline;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gemserk.animation4j.Vector2f;
import com.gemserk.animation4j.Vector2fConverter;
import com.gemserk.animation4j.converters.Converters;

public class BuildersTest {

	@Before
	public void before() {
		Converters.register(Vector2f.class, new Vector2fConverter());
		Converters.register(Float.class, Converters.floatValue());
	}

	@After
	public void after() {
		Converters.unregister(Vector2f.class);
		Converters.unregister(Float.class);
	}

	@Test
	public void testAnimationBuilder() {
		TimelineAnimation timelineAnimation = Builders.animation( //
				Builders.timeline() //
						.value(Builders.timelineValue("alpha") //
								.typeConverter(Converters.floatValue()) //
								.keyFrame(0f, 500f) //
								.keyFrame(0.5f, 12500f) //
								.keyFrame(1f, 500f) //
						) //
						.value(Builders.timelineValue("gamma") //
								.keyFrame(0f, 3f) //
								.keyFrame(0.5f, 10f) //
								.keyFrame(1f, 50f) //
						)) //
				.speed(2f) //
				.delay(0.2f) //
				.started(true) //
				.build();
		assertThat(timelineAnimation.getDelay(), IsEqual.equalTo(0.2f));
		assertThat(timelineAnimation.getDuration(), IsEqual.equalTo(1.2f));
		System.out.println(timelineAnimation.getTimeline());
	}

}
