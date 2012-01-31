package com.org.jk.wallpaper;

import java.util.Random;

import android.graphics.Color;

public class Circle {

	int centerX;
	int centerY;
	int radiusMin;
	int radiusMax;
	int currentRadius;
	int incrementRadius;
	int width;
	int height;
	int currentColor;
	int currentAlpha;
	int decrementAlpha = 20;
	int colors[] = { Color.RED, Color.BLUE, Color.YELLOW, Color.YELLOW,
			Color.GREEN, Color.CYAN };
	boolean isDrawn;

	Random r;

	public Circle(int width, int height) {
		this.width = width;
		this.height = height;
		r = new Random();
		randomizeRadius();
		currentRadius = radiusMin;
	}

	public void randomizeRadius() {
		radiusMin = r.nextInt(40 - 30 + 1) + 30;
		radiusMax = r.nextInt(60 - 40 + 1) + 40;
		incrementRadius = r.nextInt(5 - 3 + 1) + 2;
		currentRadius = radiusMin;
	}

	public void randomizeCenter() {
		centerX = (int) (width * Math.random());
		centerY = (int) (height * Math.random());
	}

	public void updateValues() {
		currentRadius = currentRadius + incrementRadius;
		currentAlpha = currentAlpha - decrementAlpha;
		if (currentRadius > radiusMax) {
			currentColor = colors[r.nextInt(6)];
			randomizeRadius();
			randomizeCenter();
			currentAlpha = 150;

		}
	}

}
