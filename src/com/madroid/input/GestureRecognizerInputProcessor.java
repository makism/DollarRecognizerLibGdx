package com.madroid.input;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.madroid.input.gestures.MatchingGesture;
import com.madroid.input.gestures.ProtractorGestureRecognizer;

public class GestureRecognizerInputProcessor extends InputAdapter {
	
	private ProtractorGestureRecognizer recognizer;
	private ArrayList<Vector2> originalPath;
	
	public GestureRecognizerInputProcessor() {
		super();
		
		recognizer = new ProtractorGestureRecognizer();
		recognizer.addGestureFromFile(Gdx.files.internal("gestures/triangle.json"));
		recognizer.addGestureFromFile(Gdx.files.internal("gestures/chi.json"));
		
		originalPath = new ArrayList<Vector2>();
	}
	
	@Override public boolean touchDown(int x, int y, int pointer, int button) {
		originalPath.add(new Vector2(x, y));

		return false;
	}

	@Override public boolean touchDragged(int x, int y, int pointer) {
		originalPath.add(new Vector2(x, y));

		return false;
	}

	@Override public boolean touchUp(int x, int y, int pointer, int button) {
		if (originalPath.size() >= 10) {
			originalPath.add(new Vector2(x, y));
			MatchingGesture match = recognizer.Recognize(originalPath);
			
			Gdx.app.log("Gesture Name/Score", match.getGesture().getName()
					+ Float.toString(match.getScore()));
		}

		originalPath.clear();

		return false;
	}
	
}
