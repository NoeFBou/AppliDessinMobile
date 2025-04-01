package com.example.dessin.metier;

import android.graphics.Color;

public class Cercle extends Forme {
	private int rayon;

	public Cercle(int x, int y, int couleur, boolean fill, int rayon) {
		super(x, y, couleur, fill);

		this.rayon = rayon;
	}

	public int getRayon() {
		return rayon;
	}

	public void setRayon(int rayon) {
		this.rayon = rayon;
	}

	}
