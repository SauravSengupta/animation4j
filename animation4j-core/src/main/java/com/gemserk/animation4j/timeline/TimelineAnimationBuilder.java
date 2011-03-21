package com.gemserk.animation4j.timeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class TimelineAnimationBuilder {

	Collection<TimelineValueBuilder> timelineValueBuilders = new ArrayList<TimelineValueBuilder>();
	
	private float delay = 0;

	private boolean started = false;

	private float speed = 1f;
	
	private float calculateDuration() {
		float duration = 0;
		for (TimelineValueBuilder timelineValueBuilder : timelineValueBuilders) 
			duration = Math.max(duration, timelineValueBuilder.getDuration());
		return duration;
	}

	private Timeline buildTimeline() {
		Map<String, TimelineValue> timelineValues = new HashMap<String, TimelineValue>();
		for (TimelineValueBuilder timelineValueBuilder : timelineValueBuilders) 
			timelineValues.put(timelineValueBuilder.getName(), timelineValueBuilder.build());
		return new Timeline(timelineValues);
	}
	
	public TimelineAnimation build() {
		TimelineAnimation timelineAnimation = new TimelineAnimation(buildTimeline(), started);
		timelineAnimation.setSpeed(speed);
		timelineAnimation.setDelay(delay);
		timelineAnimation.setDuration(calculateDuration());
		return timelineAnimation;
	}

	public TimelineAnimationBuilder value(String name, TimelineValueBuilder timelineValueBuilder) {
		timelineValueBuilder.setName(name);
		timelineValueBuilders.add(timelineValueBuilder);
		return this;
	}

	public TimelineAnimationBuilder delay(float time) {
		this.delay = time;
		return this;
	}
	
	public TimelineAnimationBuilder started(boolean started) {
		this.started = started;
		return this;
	}
	
	public TimelineAnimationBuilder speed(float speed) {
		this.speed = speed;
		return this;
	}
	
}