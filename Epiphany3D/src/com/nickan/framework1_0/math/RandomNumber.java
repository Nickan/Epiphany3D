package com.nickan.framework1_0.math;

import java.util.Random;

/**
 * I don't need to have multiple random number creator, so I did this class
 * @author Nickan
 *
 */
public class RandomNumber {
	private static Random random = new Random();

	private RandomNumber() { }

	/**
	 *
	 * @param min - The minimum integer
	 * @param max - The maximun integer
	 * @return - The values between min and max
	 */
	public static int getRandomInt(int min, int max) {
		random.setSeed(System.currentTimeMillis());
		int randomNumber = random.nextInt(max - min);
		return randomNumber + min + 1;
	}

}
