package com.madroid.input.gestures;

public class MatchingGesture {
	private TemplateGesture gesture;
	private float score;

	public MatchingGesture(TemplateGesture gesture, float score) {
		this.gesture = gesture;
		this.score = score;
	}

	public TemplateGesture getGesture() {
		return gesture;
	}

	public float getScore() {
		return score;
	}
}
