package com.example.dessin.metier;

import android.graphics.Color;

public class Forme {
	protected int x;
	protected int y;

	protected int couleur;
	protected boolean fill;

	public Forme(int x, int y, int couleur, boolean fill) {
		this.x = x;
		this.y = y;

		this.couleur = couleur;
		this.fill = fill;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getCouleur() {
		return couleur;
	}

	public boolean isFill() {
		return fill;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
