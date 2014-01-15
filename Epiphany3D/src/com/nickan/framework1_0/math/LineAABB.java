package com.nickan.framework1_0.math;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class LineAABB {
	
	private LineAABB() { }
	
	public static float min;
	public static float max;
	
	/** @return the intersection of the line and the box, null if it does not intersect */
	public static Vector3 getLineAABBIntersection(Vector3 rayOrigin, Vector3 rayEnd, BoundingBox box) {
		Vector3 intersection = new Vector3();
		min = 0;
		max = 1;

		if (!clipLine(Vector3.X, rayOrigin, rayEnd, box))
			return null;

		if (!clipLine(Vector3.Y, rayOrigin, rayEnd, box))
			return null;

		if (!clipLine(Vector3.Z, rayOrigin, rayEnd, box))
			return null;

		// The line path of the ray (Mouse click)
		Vector3 linePath = new Vector3();
		linePath.set(rayEnd);
		linePath.sub(rayOrigin);

		//...
//		System.out.println("Line path " + linePath);

		intersection = new Vector3();
		linePath.scl(min);
		intersection.add(linePath.add(rayOrigin));

		return intersection;
	}

	/**
	 *
	 * @param axis
	 * @param rayOrigin
	 * @param rayEnd
	 * @return If there is existing clip line
	 */
	private static boolean clipLine(Vector3 axis, Vector3 rayOrigin, Vector3 rayEnd, BoundingBox box) {
		float fracMin;
		float fracMax;
		if (axis.x == 1) {
			fracMin = (box.min.x - rayOrigin.x) / (rayEnd.x - rayOrigin.x);
			fracMax = (box.max.x - rayOrigin.x) / (rayEnd.x - rayOrigin.x);
		} else if (axis.y == 1) {
			fracMin = (box.min.y - rayOrigin.y) / (rayEnd.y - rayOrigin.y);
			fracMax = (box.max.y - rayOrigin.y) / (rayEnd.y - rayOrigin.y);
		} else {
			fracMin = (box.min.z - rayOrigin.z) / (rayEnd.z - rayOrigin.z);
			fracMax = (box.max.z - rayOrigin.z) / (rayEnd.z - rayOrigin.z);
		}
		//...
//		System.out.println("B FracMin " + fracMin);
//		System.out.println("B FracMax " + fracMax);

		// Make sure that the fracMin is lower than the fracMax
		if (fracMin > fracMax) {
			float tempFrac = fracMin;
			fracMin = fracMax;
			fracMax = tempFrac;
		}

		//...
//		System.out.println("A FracMin " + fracMin);
//		System.out.println("A FracMax " + fracMax);

		// Totally missed the box based on the min and max fractions of the path of line on the specific axis
		if (fracMax < min || fracMin > max)
			return false;

		// Get the highest min and lowest max
		min = Math.max(fracMin, min);
		max = Math.min(fracMax, max);

		// Totally missed the box too, after setting both min and max
		if (min > max)
			return false;

		return true;
	}
}
