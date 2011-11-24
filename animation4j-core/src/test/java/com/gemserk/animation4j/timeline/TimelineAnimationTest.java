package com.gemserk.animation4j.timeline;

import static org.junit.Assert.*;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.gemserk.animation4j.animations.Animation;
import com.gemserk.animation4j.converters.Converters;

public class TimelineAnimationTest {

	public static class IsNear {

		public static Matcher<Float> isNear(float x, float range) {
			return new Near(x, range);
		}

	}

	public static class Near extends BaseMatcher<Float> {

		private final float x;

		private final float range;

		public Near(float x, float range) {
			this.x = x;
			this.range = range;
		}

		@Override
		public boolean matches(Object o) {
			if (!(o instanceof Float))
				return false;
			Float x1 = (Float) o;
			return Math.abs(x1 - x) < range;
		}

		@Override
		public void describeTo(Description description) {

		}
	}
	
	@Test
	public void shouldNotBeStartedBeforeDelay() {

		TimelineAnimation animation = new TimelineAnimationBuilder() {
			{
				started(true);
				delay(100);
				value("myvalue", new TimelineValueBuilder(Converters.floatValue()) {
					{
						keyFrame(0, 100f);
						keyFrame(100, 200f);
					}
				});
			}
		}.build();

		animation.resume();

		assertFalse(animation.isStarted());
		animation.update(101);
		assertTrue(animation.isStarted());
		animation.update(100);
		assertTrue(animation.isFinished());
	}

	@Test
	public void shouldIterateIterationCountTimes() {

		TimelineAnimation animation = new TimelineAnimationBuilder() {
			{
				value("myvalue", new TimelineValueBuilder(Converters.floatValue()) {
					{
						keyFrame(0, 100f);
						keyFrame(100, 200f);
					}
				});
			}
		}.build();

		animation.start(2);

		// assertThat(animation.getIteration(), IsEqual.equalTo(1));
		assertThat((Float) animation.getValue("myvalue"), IsEqual.equalTo(100f));
		animation.update(0.09999f);
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(200f, 0.1f));
		animation.update(0.01f * 0.001f);
		// assertThat(animation.getIteration(), IsEqual.equalTo(2));
		assertThat(animation.isFinished(), IsEqual.equalTo(false));
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(100f, 0.1f));
		animation.update(100f * 0.001f);
		assertThat(animation.isFinished(), IsEqual.equalTo(true));
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(200f, 0.1f));
	}

	@Test
	public void shouldIterateAnIndefiniteNumberOfTimes() {

		TimelineAnimation animation = new TimelineAnimationBuilder() {
			{
				value("myvalue", new TimelineValueBuilder(Converters.floatValue()) {
					{
						keyFrame(0, 100f);
						keyFrame(100, 200f);
					}
				});
			}
		}.build();

		animation.start(0);

		// assertThat(animation.getIteration(), IsEqual.equalTo(1));
		assertThat((Float) animation.getValue("myvalue"), IsEqual.equalTo(100f));
		animation.update(99.99f * 0.001f);
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(200f, 0.1f));
		animation.update(0.01f * 0.001f);
		// assertThat(animation.getIteration(), IsEqual.equalTo(2));
		assertThat(animation.isFinished(), IsEqual.equalTo(false));
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(100f, 0.1f));
		animation.update(99.99f * 0.001f);
		assertThat(animation.isFinished(), IsEqual.equalTo(false));
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(200f, 0.1f));

		animation.update(9999999f * 0.001f);
		assertThat(animation.isFinished(), IsEqual.equalTo(false));
		// assertThat(animation.getIteration(), IsEqual.equalTo(100));

	}

	@Test
	public void shouldBeStartedBetweenIterationsWithDelays() {

		TimelineAnimation animation = new TimelineAnimationBuilder() {
			{
				value("myvalue", new TimelineValueBuilder(Converters.floatValue()) {
					{
						delay(100);
						keyFrame(0, 100f);
						keyFrame(100, 200f);
					}
				});
			}
		}.build();

		animation.start(2);
		assertThat(animation.isStarted(), IsEqual.equalTo(false));
		animation.update(101);
		assertThat(animation.isStarted(), IsEqual.equalTo(true));
		animation.update(101);
		assertThat(animation.isStarted(), IsEqual.equalTo(true));
	}

	@Test
	public void shouldPlayInReversIfAlternateDirection() {

		TimelineAnimation animation = new TimelineAnimationBuilder() {
			{
				value("myvalue", new TimelineValueBuilder(Converters.floatValue()) {
					{
						keyFrame(0, 200f);
						keyFrame(100, 900f);
						keyFrame(150, 1900f);
						keyFrame(200, 2000f);
					}
				});
			}
		}.build();
		
		animation.setAlternateDirection(true);
		animation.start(2);
		
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(200f, 0.1f));
		
		animation.update(100 * 0.001f);
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(900f, 0.1f));

		animation.update(100 * 0.001f);
		assertThat(animation.isStarted(), IsEqual.equalTo(true));
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(2000f, 0.1f));

		animation.update(50 * 0.001f);
		assertThat(animation.isStarted(), IsEqual.equalTo(true));
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(1900f, 0.1f));
		assertThat(animation.isFinished(), IsEqual.equalTo(false));
		
		animation.update(151 * 0.001f);
		assertThat(animation.isFinished(), IsEqual.equalTo(true));
		assertThat((Float) animation.getValue("myvalue"), IsNear.isNear(200f, 0.1f));

	}
	
	// test: delay should be used only to start the animation, not in each iteration.
	
	@Test
	public void shouldProcessDelayOnlyWhenAnimationIsNotStarted() {

		final String valueName = "myvalue";
		
		TimelineAnimation animation = new TimelineAnimationBuilder() {
			{
				value(valueName, new TimelineValueBuilder(Converters.floatValue()) {
					{
						delay(100);
						keyFrame(0, 100f);
						keyFrame(100, 200f);
					}
				});
			}
		}.build();

		animation.start(2);
		assertThat((Float) animation.getValue(valueName), IsNear.isNear(100f, 0.1f));
		animation.update(100 * 0.001f);
		assertThat((Float) animation.getValue(valueName), IsNear.isNear(100f, 0.1f));
		animation.update(99.999f * 0.001f);
		assertThat((Float) animation.getValue(valueName), IsNear.isNear(200f, 0.1f));
		
		animation.update(0.001f * 0.001f);
		assertThat((Float) animation.getValue(valueName), IsNear.isNear(100f, 0.1f));
		
		// the second time it should only move without using the delay.
		
		animation.update(99.999f * 0.001f);
		assertThat(animation.isFinished(), IsEqual.equalTo(false));
		assertThat((Float) animation.getValue(valueName), IsNear.isNear(200f, 0.1f));
	}
	
	// test: same thing with an animation alternating directions
	
	// test restart is not setting iteration to 0? (fixed without test)
	
	// test: restart should restart delay?
	
	@Test
	public void bugWhenRestartAnimationPlayingInReverseItWasStillInReverse() {
		final String valueName = "myvalue";
		
		TimelineAnimation animation = new TimelineAnimationBuilder() {
			{
				value(valueName, new TimelineValueBuilder(Converters.floatValue()) {
					{
						keyFrame(0, 100f);
						keyFrame(100, 200f);
					}
				});
			}
		}.build();

		animation.start(2, true);
		assertThat(animation.getIteration(), IsEqual.equalTo(1));
		assertThat(animation.getPlayingDirection(), IsEqual.equalTo(Animation.PlayingDirection.Normal));
		animation.update(101 * 0.001f);
		assertThat(animation.getIteration(), IsEqual.equalTo(2));
		assertThat(animation.getPlayingDirection(), IsEqual.equalTo(Animation.PlayingDirection.Reverse));
		animation.restart();
		assertThat(animation.getIteration(), IsEqual.equalTo(1));
		assertThat(animation.getPlayingDirection(), IsEqual.equalTo(Animation.PlayingDirection.Normal));
		animation.update(20 * 0.001f);
		assertThat(animation.getIteration(), IsEqual.equalTo(1));
		assertThat(animation.getPlayingDirection(), IsEqual.equalTo(Animation.PlayingDirection.Normal));
		
	}
	
	@Test
	public void shouldBeFinishedAfterTotalTimeSpent() {
		
		float totalTime = 20f;
		
		TimelineAnimation animation = Builders.animation(Builders.timeline() //
				.value(Builders.timelineValue("alpha") //
						.typeConverter(Converters.floatValue()) //
						.keyFrame(0f, 0f) //
						.keyFrame(0.2f, 1f) //
						.keyFrame(0.8f, 1f) //
						.keyFrame(1f, 0f)) //
				) //
				.speed(1f / totalTime) //
				.build();
		animation.start(1);
		
		animation.update(totalTime * 0.5f);
		
		assertFalse(animation.isFinished());
		
		animation.update(totalTime * 0.5f);
		
		assertTrue(animation.isFinished());
	}
	
	@Test
	public void bugTimelineAnimationShouldHaveTimelineDuration() {
		Timeline timeline = Builders.timeline() //
				.value(Builders.timelineValue("alpha") //
						.typeConverter(Converters.floatValue()) //
						.keyFrame(0f, 0f) //
						.keyFrame(0.2f, 1f)).build();
		
		TimelineAnimation animation = new TimelineAnimation(timeline, 0.2f);
		
		assertThat(animation.getDuration(), IsEqual.equalTo(0.2f));
	}

}
