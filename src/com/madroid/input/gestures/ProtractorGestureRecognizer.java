package com.madroid.input.gestures;

import java.util.ArrayList;

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
		if (handle.isDirectory()) {
			FileHandle[] files = handle.list("json");
			for (FileHandle f : files)
				addGestureFromFile(f);
			
		} else {
			JsonReader jreader = new JsonReader();
			ObjectMap obj = (ObjectMap) jreader.parse(handle);
			
			String _name = (String) obj.get("Name");
			Array<ObjectMap<String, Float>> _points = (Array<ObjectMap<String, Float>>) obj
					.get("Points");
			Array<Float> _vector = (Array<Float>) obj.get("Vector");

			ArrayList<Vector2> _arrlist_vector = new ArrayList<Vector2>();
			for (int i = 0; i < _points.size; i++) {
				ObjectMap<String, Float> data = _points.get(i);
				float x = (float) data.get("X");
				float y = (float) data.get("Y");
				_arrlist_vector.add(new Vector2(x, y));
			}

			float[] _arr_vector = new float[_vector.size];
			for (int i = 0; i < _vector.size; i++)
				_arr_vector[i] = _vector.get(i);
			
			addGesture(new TemplateGesture(_name, _arrlist_vector, _arr_vector));
		}
	}

	public void removeGesture(TemplateGesture tg) {
		registeredGestures.remove(tg);
	}

	public MatchingGesture Recognize(ArrayList<Vector2> originalPath) {
		float[] vector = DollarUnistrokeRecognizer.Vectorize(originalPath);

		TemplateGesture match = null;
		float b = Float.POSITIVE_INFINITY;
		for (TemplateGesture gesture : registeredGestures) {
			float d = OptimalCosineDistance(gesture.getVector(), vector);

			if (d < b) {
				b = d;
				match = gesture;
			}
		}

		return new MatchingGesture(match, 1.0f / b);
	}

	private float OptimalCosineDistance(float[] v1, float[] v2) {
		float a = 0.0f;
		float b = 0.0f;
		float angle = 0.0f;

		int len = Math.min(v1.length, v2.length);
		for (int i = 0; i < len; i += 2) {
			a += v1[i] * v2[i] + v1[i + 1] * v2[i + 1];
			b += v1[i] * v2[i + 1] - v1[i + 1] * v2[i];
		}

		angle = (float) Math.atan(b / a);
		return (float) Math.acos(a * Math.cos(angle) + b * Math.sin(angle));
	}

}
