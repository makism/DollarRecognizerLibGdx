package com.madroid.input.gestures;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap;

public class ProtractorGestureRecognizer {

	private ArrayList<TemplateGesture> registeredGestures;

	public ProtractorGestureRecognizer() {
		registeredGestures = new ArrayList<TemplateGesture>();
	}

	public void addGesture(TemplateGesture tg) {
		registeredGestures.add(tg);
	}

	@SuppressWarnings("unchecked")
	public void addGestureFromFile(FileHandle handle) {
		JsonReader jreader = new JsonReader();
		ObjectMap obj = (ObjectMap) jreader.parse(handle);

		String _name = (String) obj.get("Name");
		Array<Array> _points = (Array<Array>) obj.get("Points");
		Array<Array> _vector = (Array<Array>) obj.get("Vector");

		ArrayList<Vector2> _arrlist_points = new ArrayList<Vector2>();
		for (Array arr : _points) {
			Vector2 v = new Vector2((Float) arr.get(0), (Float) arr.get(1));
			_arrlist_points.add(v);
		}

		Vector2[] _arr_vector = new Vector2[_vector.size];
		for (int i = 0; i < _vector.size; i++) {
			Array arr = _vector.get(i);
			_arr_vector[i] = new Vector2((Float) arr.get(0), (Float) arr.get(1));
		}

		addGesture(new TemplateGesture(_name, _arrlist_points, _arr_vector));
	}

	public void removeGesture(TemplateGesture tg) {
		registeredGestures.remove(tg);
	}

	public MatchingGesture Recognize(ArrayList<Vector2> originalPath) {
		Vector2[] vector = DollarUnistrokeRecognizer.Vectorize(originalPath);

		float b = Float.POSITIVE_INFINITY;
		int t = 0;
		int i = 0;
		for (TemplateGesture gesture : registeredGestures) {
			float d = OptimalCosineDistance(gesture.getVector(), vector);

			if (d < b) {
				b = d;
				t = i;
			}

			i++;
		}

		return new MatchingGesture(registeredGestures.get(t), 1.0f / b);
	}

	private float OptimalCosineDistance(Vector2[] v1, Vector2[] v2) {
		float a = 0.0f;
		float b = 0.0f;
		float angle = 0.0f;

		for (int i = 0; i < v1.length; i += 2) {
			a += v1[i].dot(v2[i]) + v1[i + 1].dot(v2[i + 1]);
			b += v1[i].dot(v2[i + 1]) - v1[i + 1].dot(v2[i]);
		}

		angle = (float) Math.atan(b / a);
		return (float) Math.acos(a * Math.cos(angle) + b * Math.sin(angle));
	}

}
