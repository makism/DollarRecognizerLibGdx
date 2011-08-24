import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ProtractorGestureRecognizer extends InputAdapter {

	private class TemplateGesture {
		String name;
		ArrayList<Vector2> points;
		Vector2[] vector;

		public TemplateGesture(String name, ArrayList<Vector2> points) {
			this.name = name;
			this.points = new ArrayList<Vector2>(points);
			
			this.points = Resample(this.points);
			float radians = IndicativeAngle(this.points);
			this.points = RotateBy(this.points, -radians);
			this.points = ScaleTo(this.points, SquareSize);
			this.points = TranslateTo(this.points, Origin);
			this.vector = Vectorize(this.points);
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

	public class MatchingGesture {
		TemplateGesture gesture;
		float score;

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

	ArrayList<TemplateGesture> registeredGestures;

	ArrayList<Vector2> originalPath;

	int numSamples = 64;

	float SquareSize = 250.0f;

	Vector2 Origin = new Vector2(0, 0);

	float Diagonal = (float) Math.sqrt(SquareSize * SquareSize + SquareSize
			* SquareSize);

	float HalfDiagonal = 0.5f * Diagonal;

	float AngleRange = MathUtils.degreesToRadians * 45.0f;

	float AnglePrecision = MathUtils.degreesToRadians * 2.0f;

	float Phi = 0.5f * (float) (-1.0 + Math.sqrt(5.0f));

	public ProtractorGestureRecognizer() {
		super();

		registeredGestures = new ArrayList<TemplateGesture>();
		originalPath = new ArrayList<Vector2>();

		ArrayList<Vector2> trianglePoints = new ArrayList<Vector2>();
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(135, 141));
		trianglePoints.add(new Vector2(133, 144));
		trianglePoints.add(new Vector2(132, 146));
		trianglePoints.add(new Vector2(130, 149));
		trianglePoints.add(new Vector2(128, 151));
		trianglePoints.add(new Vector2(126, 155));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(137, 139));
		trianglePoints.add(new Vector2(123, 160));
		trianglePoints.add(new Vector2(120, 166));
		trianglePoints.add(new Vector2(116, 171));
		trianglePoints.add(new Vector2(112, 177));
		trianglePoints.add(new Vector2(107, 183));
		trianglePoints.add(new Vector2(102, 188));
		trianglePoints.add(new Vector2(100, 191));
		trianglePoints.add(new Vector2(95, 195));
		trianglePoints.add(new Vector2(90, 199));
		trianglePoints.add(new Vector2(86, 203));
		trianglePoints.add(new Vector2(82, 206));
		trianglePoints.add(new Vector2(80, 209));
		trianglePoints.add(new Vector2(75, 213));
		trianglePoints.add(new Vector2(73, 213));
		trianglePoints.add(new Vector2(70, 216));
		trianglePoints.add(new Vector2(67, 219));
		trianglePoints.add(new Vector2(64, 221));
		trianglePoints.add(new Vector2(61, 223));
		trianglePoints.add(new Vector2(60, 225));
		trianglePoints.add(new Vector2(62, 226));
		trianglePoints.add(new Vector2(65, 225));
		trianglePoints.add(new Vector2(67, 226));
		trianglePoints.add(new Vector2(74, 226));
		trianglePoints.add(new Vector2(77, 227));
		trianglePoints.add(new Vector2(85, 229));
		trianglePoints.add(new Vector2(91, 230));
		trianglePoints.add(new Vector2(99, 231));
		trianglePoints.add(new Vector2(108, 232));
		trianglePoints.add(new Vector2(116, 233));
		trianglePoints.add(new Vector2(125, 233));
		trianglePoints.add(new Vector2(134, 234));
		trianglePoints.add(new Vector2(145, 233));
		trianglePoints.add(new Vector2(153, 232));
		trianglePoints.add(new Vector2(160, 233));
		trianglePoints.add(new Vector2(170, 234));
		trianglePoints.add(new Vector2(177, 235));
		trianglePoints.add(new Vector2(179, 236));
		trianglePoints.add(new Vector2(186, 237));
		trianglePoints.add(new Vector2(193, 238));
		trianglePoints.add(new Vector2(198, 239));
		trianglePoints.add(new Vector2(200, 237));
		trianglePoints.add(new Vector2(202, 239));
		trianglePoints.add(new Vector2(204, 238));
		trianglePoints.add(new Vector2(206, 234));
		trianglePoints.add(new Vector2(205, 230));
		trianglePoints.add(new Vector2(202, 222));
		trianglePoints.add(new Vector2(197, 216));
		trianglePoints.add(new Vector2(192, 207));
		trianglePoints.add(new Vector2(186, 198));
		trianglePoints.add(new Vector2(179, 189));
		trianglePoints.add(new Vector2(174, 183));
		trianglePoints.add(new Vector2(170, 178));
		trianglePoints.add(new Vector2(164, 171));
		trianglePoints.add(new Vector2(161, 168));
		trianglePoints.add(new Vector2(154, 160));
		trianglePoints.add(new Vector2(148, 155));
		trianglePoints.add(new Vector2(143, 150));
		trianglePoints.add(new Vector2(138, 148));
		trianglePoints.add(new Vector2(136, 148));
		TemplateGesture triangleGesture = new TemplateGesture("triangle",
				trianglePoints);

		ArrayList<Vector2> chiPoints = new ArrayList<Vector2>();
		chiPoints.add(new Vector2(87, 142));
		chiPoints.add(new Vector2(89, 145));
		chiPoints.add(new Vector2(91, 148));
		chiPoints.add(new Vector2(93, 151));
		chiPoints.add(new Vector2(96, 155));
		chiPoints.add(new Vector2(98, 157));
		chiPoints.add(new Vector2(100, 160));
		chiPoints.add(new Vector2(102, 162));
		chiPoints.add(new Vector2(106, 167));
		chiPoints.add(new Vector2(108, 169));
		chiPoints.add(new Vector2(110, 171));
		chiPoints.add(new Vector2(115, 177));
		chiPoints.add(new Vector2(119, 183));
		chiPoints.add(new Vector2(123, 189));
		chiPoints.add(new Vector2(127, 193));
		chiPoints.add(new Vector2(129, 196));
		chiPoints.add(new Vector2(133, 200));
		chiPoints.add(new Vector2(137, 206));
		chiPoints.add(new Vector2(140, 209));
		chiPoints.add(new Vector2(143, 212));
		chiPoints.add(new Vector2(146, 215));
		chiPoints.add(new Vector2(151, 220));
		chiPoints.add(new Vector2(153, 222));
		chiPoints.add(new Vector2(155, 223));
		chiPoints.add(new Vector2(157, 225));
		chiPoints.add(new Vector2(158, 223));
		chiPoints.add(new Vector2(157, 218));
		chiPoints.add(new Vector2(155, 211));
		chiPoints.add(new Vector2(154, 208));
		chiPoints.add(new Vector2(152, 200));
		chiPoints.add(new Vector2(150, 189));
		chiPoints.add(new Vector2(148, 179));
		chiPoints.add(new Vector2(147, 170));
		chiPoints.add(new Vector2(147, 158));
		chiPoints.add(new Vector2(147, 148));
		chiPoints.add(new Vector2(147, 141));
		chiPoints.add(new Vector2(147, 136));
		chiPoints.add(new Vector2(144, 135));
		chiPoints.add(new Vector2(142, 137));
		chiPoints.add(new Vector2(140, 139));
		chiPoints.add(new Vector2(135, 145));
		chiPoints.add(new Vector2(131, 152));
		chiPoints.add(new Vector2(124, 163));
		chiPoints.add(new Vector2(116, 177));
		chiPoints.add(new Vector2(108, 191));
		chiPoints.add(new Vector2(100, 206));
		chiPoints.add(new Vector2(94, 217));
		chiPoints.add(new Vector2(91, 222));
		chiPoints.add(new Vector2(89, 225));
		chiPoints.add(new Vector2(87, 226));
		chiPoints.add(new Vector2(87, 224));
		TemplateGesture chiGesture = new TemplateGesture("x", chiPoints);

		registeredGestures.add(triangleGesture);
		registeredGestures.add(chiGesture);
	}

	public ProtractorGestureRecognizer(String savedGestures) {
		super();

		registeredGestures = new ArrayList<TemplateGesture>();
		originalPath = new ArrayList<Vector2>();
	}

	public void addGesture(TemplateGesture tg) {
		registeredGestures.add(tg);
	}

	public void removeGesture(TemplateGesture tg) {
		registeredGestures.remove(tg);
	}

	public MatchingGesture Recognize() {
		ArrayList<Vector2> points = Resample(originalPath);
		float radians = IndicativeAngle(points);
		points = RotateBy(points, -radians);
		points = ScaleTo(points, SquareSize);
		points = TranslateTo(points, Origin);

		Vector2[] vector = Vectorize(points);

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

	private ArrayList<Vector2> Resample(ArrayList<Vector2> points) {
		float I = PathLength(points) / (numSamples - 1);
		float D = 0.0f;
		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();
		newPoints.add(points.get(0));
		
		for (int idx = 1; idx < points.size(); idx++) {
			Vector2 curr = points.get(idx);
			Vector2 prev = points.get(idx - 1);

			float d = Distance(prev, curr);

			if (D + d >= I) {
				Vector2 q = new Vector2();

				q.x = prev.x + ((I - D) / d) * (curr.x - prev.x);
				q.y = prev.y + ((I - D) / d) * (curr.y - prev.y);

				newPoints.add(q);
				points.add(idx, q);

				D = 0.0f;
			} else {
				D += d;
			}
		}

		if (newPoints.size() == numSamples - 1)
			newPoints.add(new Vector2(points.get(points
					.size() - 1).x, points.get(points.size() - 1).y));
		
		Gdx.app.log("Resample", "Complete: " + newPoints.size());

		return newPoints;
	}

	private Vector2 Centroid(ArrayList<Vector2> points) {
		float x = 0.0f;
		float y = 0.0f;

		for (Vector2 v : points) {
			x += v.x;
			y += v.y;
		}

		x /= points.size();
		y /= points.size();

		return new Vector2(x, y);
	}

	private float IndicativeAngle(ArrayList<Vector2> points) {
		Vector2 c = Centroid(points);
		return MathUtils.atan2(c.y - points.get(0).y, c.x - points.get(0).x);
	}

	private ArrayList<Vector2> RotateBy(ArrayList<Vector2> points, float radians) {
		Vector2 c = Centroid(points);
		float cos = MathUtils.cos(radians);
		float sin = MathUtils.sin(radians);

		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();

		for (Vector2 v : points) {
			float qx = (v.x - c.x) * cos - (v.y - c.y) * sin + c.x;
			float qy = (v.x - c.x) * sin + (v.y - c.y) * cos + c.y;

			newPoints.add(new Vector2(qx, qy));
		}

		return newPoints;
	}

	private ArrayList<Vector2> ScaleTo(ArrayList<Vector2> points, float size) {
		Rectangle B = BoundingBox(points);
		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();

		for (Vector2 v : points) {
			float qx = v.x * (size / B.width);
			float qy = v.y * (size / B.height);

			newPoints.add(new Vector2(qx, qy));
		}

		return newPoints;
	}

	private Rectangle BoundingBox(ArrayList<Vector2> points) {
		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;

		for (Vector2 v : points) {
			if (v.x < minX)
				minX = v.x;
			if (v.x > maxX)
				maxX = v.x;
			if (v.y < minY)
				minY = v.y;
			if (v.y > maxY)
				maxY = v.y;
		}

		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	private ArrayList<Vector2> TranslateTo(ArrayList<Vector2> points, Vector2 pt) {
		Vector2 c = Centroid(points);
		ArrayList<Vector2> newPoints = new ArrayList<Vector2>();

		for (Vector2 v : points) {
			float qx = v.x + pt.x - c.x;
			float qy = v.y + pt.y - c.y;

			newPoints.add(new Vector2(qx, qy));
		}

		return newPoints;
	}

	private Vector2[] Vectorize(ArrayList<Vector2> points) {
		float sum = 0.0f;
		Vector2[] vector = new Vector2[points.size()];

		for (int i = 0; i < points.size(); i++) {
			vector[i] = new Vector2(points.get(i));
			sum += vector[i].x * vector[i].x + vector[i].y * vector[i].y;
		}

		float magnitude = (float) Math.sqrt(sum);
		for (int i = 0; i < vector.length; i++) {
			vector[i].x /= magnitude;
			vector[i].y /= magnitude;
		}

		return vector;
	}

	private float PathLength(ArrayList<Vector2> points) {
		float d = 0.0f;

		for (int i = 1; i < points.size(); i++) {
			Vector2 a = points.get(i);
			Vector2 b = points.get(i - 1);

			d += Distance(b, a);
		}

		return d;
	}

	private float Distance(Vector2 a, Vector2 b) {
		float dx = b.x - a.x;
		float dy = b.y - a.y;

		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	private float OptimalCosineDistance(Vector2[] v1, Vector2[] v2) {
		float a = 0.0f;
		float b = 0.0f;
		float angle = 0.0f;
		
		Gdx.app.log("OptimalCosineDistance:v1 size", Integer.toString(v1.length));
		Gdx.app.log("OptimalCosineDistance:v2 size", Integer.toString(v2.length));

		for (int i = 0; i < v1.length; i += 2) {
			a += v1[i].dot(v2[i]) + v1[i + 1].dot(v2[i + 1]);
			b += v1[i].dot(v2[i + 1]) - v1[i + 1].dot(v2[i]);
		}

		angle = (float) Math.atan(b / a);
		return (float) Math.acos(a * Math.cos(angle) + b * Math.sin(angle));
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		originalPath.add(new Vector2(x, y));

		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		originalPath.add(new Vector2(x, y));

		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (originalPath.size() >= 10) {
			MatchingGesture match = Recognize();
			Gdx.app.log("Gesture Name/Score", match.getGesture().getName()
					+ Float.toString(match.getScore()));
		}

		originalPath.clear();

		return false;
	}

}
