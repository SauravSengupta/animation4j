package com.gemserk.animation4j.transitions;


public class MockTransition<T> implements Transition<T> {

	boolean started = false;
	boolean finished = false;

	@Override
	public T get() {
		return null;
	}

	@Override
	public void set(T t) {

	}

	@Override
	public void set(T t, float time) {

	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public float getSpeed() {
		// TODO Auto-generated function stub
		return 0;
		
	}

	@Override
	public void setSpeed(float speed) {
		// TODO Auto-generated function stub
		
	}

	@Override
	public void setStart(T t) {
		// TODO Auto-generated function stub
		
	}

	@Override
	public void setEnd(T t) {
		// TODO Auto-generated function stub
		
	}

}