package com.example.dessin.metier;

import android.graphics.Color;

public class Rectangle extends Forme {
	private int xFin;
	private int yFin;

	public Rectangle(int x, int y, int couleur, boolean fill, int xFin, int yFin) {
		super(x, y, couleur, fill);

		this.xFin = xFin;
		this.yFin = yFin;
	}

	public int getxFin() {
		return xFin;
	}

	public void setxFin(int xFin) {
		this.xFin = xFin;
	}

	public int getyFin() {
		return yFin;
	}

	public void setyFin(int yFin) {
		this.yFin = yFin;
	}
}
