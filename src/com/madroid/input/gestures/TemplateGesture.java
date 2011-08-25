package com.madroid.input.gestures;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class TemplateGesture {
	private String name;

	private ArrayList<Vector2> points;

	private Vector2[] vector;

	public TemplateGesture(String name, ArrayList<Vector2> points) {
		this.name = name;
		this.points = points;
		this.vector = DollarUnistrokeRecognizer.Vectorize(this.points);
	}

	public TemplateGesture(String name, ArrayList<Vector2> points,
			Vector2[] vector) {
		this.name = name;
		this.points = points;
		this.vector = vector;
	}

	public Vector2[] getVector() {
		return vector;
	}

	public ArrayList<Vector2> getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

}
