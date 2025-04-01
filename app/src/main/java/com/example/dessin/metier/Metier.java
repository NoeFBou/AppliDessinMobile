package com.example.dessin.metier;

import java.util.ArrayList;

public class Metier {
	private ArrayList<Forme> alForme;

	public Metier() {
		alForme = new ArrayList<>();
	}

	public ArrayList<Forme> getAlForme() {
		return new ArrayList<>(alForme);
	}

	public void setAlForme(ArrayList<Forme> alForme) {
		this.alForme = new ArrayList<>(alForme);
	}

	public void addForme(Forme f) {
		alForme.add(f);
	}

	public void undo() {
		if (alForme.size() == 0)
			return;

		alForme.remove(alForme.size() - 1);
	}

	public void effacerTout() {
		alForme.clear();
	}
}
