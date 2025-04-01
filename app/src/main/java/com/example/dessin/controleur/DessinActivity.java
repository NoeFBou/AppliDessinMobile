package com.example.dessin.controleur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.dessin.R;
import com.example.dessin.metier.Cercle;
import com.example.dessin.metier.Forme;
import com.example.dessin.metier.Ligne;
import com.example.dessin.metier.Metier;
import com.example.dessin.metier.Rectangle;

import java.util.ArrayList;

public class DessinActivity extends AppCompatActivity {
	private LinearLayout pnlDessin;

	private Button btnQuitter;
	private Button btnEffacer;

	private Button btnRouge;
	private Button btnVert;
	private Button btnBleu;
	private Button btnJaune;
	private Button btnNoir;

	private ImageButton imgBtnCarreVide;
	private ImageButton imgBtnCarrePlein;
	private ImageButton imgBtnCercleVide;
	private ImageButton imgBtnCerclePlein;
	private ImageButton imgBtnTrait;
	private ImageButton imgBtnRetour;

	private Metier metier;
	private ZoneDessin zoneDessin;

	private int couleur;
	private Class<? extends Forme> forme;
	private boolean fill;

	public static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
	public static final String SHARED_PREF_USER_INFO_LIST_SHAPE = "SHARED_PREF_USER_INFO_LIST_SHAPE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dessin);

		metier = new Metier();
		couleur = Color.BLACK;
		forme = Rectangle.class;
		fill = false;

		String alForme = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_LIST_SHAPE, null);

		if (alForme != null)
			metier.setAlForme(lireAlForme(alForme));

		pnlDessin = findViewById(R.id.pnlDessin);

		btnQuitter = findViewById(R.id.btnQuitter);
		btnEffacer = findViewById(R.id.btnEffacer);

		btnRouge = findViewById(R.id.btnRouge);
		btnVert = findViewById(R.id.btnVert);
		btnBleu = findViewById(R.id.btnBleu);
		btnJaune = findViewById(R.id.btnJaune);
		btnNoir = findViewById(R.id.btnNoir);

		imgBtnCarreVide = findViewById(R.id.imgBtnCarreVide);
		imgBtnCarrePlein = findViewById(R.id.imgBtnCarrePlein);
		imgBtnCercleVide = findViewById(R.id.imgBtnCercleVide);
		imgBtnCerclePlein = findViewById(R.id.imgBtnCerclePlein);
		imgBtnTrait = findViewById(R.id.imgBtnTrait);
		imgBtnRetour = findViewById(R.id.imgBtnRetour);

		zoneDessin = new ZoneDessin(this);

		pnlDessin.addView(zoneDessin);

		btnQuitter.setOnClickListener(view -> finish());
		btnEffacer.setOnClickListener(view ->  {
			metier.effacerTout();
			zoneDessin.invalidate();
		});

		btnRouge.setOnClickListener(view -> couleur = Color.RED);
		btnVert.setOnClickListener(view -> couleur = Color.GREEN);
		btnBleu.setOnClickListener(view -> couleur = Color.BLUE);
		btnJaune.setOnClickListener(view -> couleur = Color.YELLOW);
		btnNoir.setOnClickListener(view -> couleur = Color.BLACK);

		imgBtnCarreVide.setOnClickListener(view -> {
			forme = Rectangle.class;
			fill = false;
		});

		imgBtnCarrePlein.setOnClickListener(view -> {
			forme = Rectangle.class;
			fill = true;
		});

		imgBtnCercleVide.setOnClickListener(view -> {
			forme = Cercle.class;
			fill = false;
		});

		imgBtnCerclePlein.setOnClickListener(view -> {
			forme = Cercle.class;
			fill = true;
		});

		imgBtnTrait.setOnClickListener(view -> forme = Ligne.class);

		imgBtnRetour.setOnClickListener(view -> {
			metier.undo();
			zoneDessin.invalidate();
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
				.edit()
				.putString(SHARED_PREF_USER_INFO_LIST_SHAPE, genererAlForme())
				.apply();
	}

	private String genererAlForme() {
		String s = "";

		for (Forme f : metier.getAlForme()) {
			String[] t = f.getClass().getName().split("\\.");

			s += t[t.length - 1] + "," + f.getX() + "," + f.getY() + "," + f.getCouleur() + "," + f.isFill();

			String classe = t[t.length - 1];

			switch (classe) {
				case "Rectangle":
					Rectangle r = (Rectangle) f;
					s += "," + r.getxFin() + "," + r.getyFin();
					break;

				case "Cercle":
					Cercle c = (Cercle) f;
					s += "," + c.getRayon();
					break;

				case "Ligne":
					Ligne l = (Ligne) f;
					s += "," + l.getxFin() + "," + l.getyFin();
					break;
			}

			s += "\n";
		}

		return s;
	}

	private ArrayList<Forme> lireAlForme(String s) {
		ArrayList<Forme> alForme = new ArrayList<>();
		String[] lignes = s.split("\n");

		for (String ligne : lignes) {
			String[] t = ligne.split(",");
			switch (t[0]) {
				case "Rectangle":
					alForme.add(new Rectangle(Integer.parseInt(t[1]),
							Integer.parseInt(t[2]),
							Integer.parseInt(t[3]),
							Boolean.parseBoolean(t[4]),
							Integer.parseInt(t[5]),
							Integer.parseInt(t[6])
					));
					break;

				case "Cercle":
					alForme.add(new Cercle(Integer.parseInt(t[1]),
							Integer.parseInt(t[2]),
							Integer.parseInt(t[3]),
							Boolean.parseBoolean(t[4]),
							Integer.parseInt(t[5])
					));
					break;

				case "Ligne":
					alForme.add(new Ligne(Integer.parseInt(t[1]),
							Integer.parseInt(t[2]),
							Integer.parseInt(t[3]),
							Boolean.parseBoolean(t[4]),
							Integer.parseInt(t[5]),
							Integer.parseInt(t[6])
					));
					break;
			}
		}

		return alForme;
	}

	class ZoneDessin extends View implements View.OnTouchListener {
		Paint p = new Paint();
		Forme f;
		int x, y;
		public ZoneDessin(Context context) {
			super(context);

			p.setStrokeWidth(10);
			setOnTouchListener(this);
		}

		public void onDraw(Canvas c) {
			c.drawLine(300, 300, 300, 300, p);

			ArrayList<Forme> alForme = metier.getAlForme();

			if (f != null)
				alForme.add(f);

			for (Forme f : alForme) {
				p.setColor(f.getCouleur());

				if (f.isFill())
					p.setStyle(Paint.Style.FILL_AND_STROKE);
				else
					p.setStyle(Paint.Style.STROKE);

				switch (f.getClass().getName()) {
					case "com.example.dessin.metier.Rectangle":
						Rectangle r = (Rectangle) f;
						c.drawRect(r.getX(), r.getY(), r.getxFin(), r.getyFin(), p);
						break;

					case "com.example.dessin.metier.Cercle":
						Cercle ce = (Cercle) f;
						c.drawCircle(ce.getX(), ce.getY(), ce.getRayon(), p);
						break;

					case "com.example.dessin.metier.Ligne":
						Ligne l = (Ligne) f;
						c.drawLine(l.getX(), l.getY(), l.getxFin(), l.getyFin(), p);
						break;
				}
			}
		}

		@Override
		public boolean onTouch(View view, MotionEvent e) {
			switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x = (int) e.getX();
					y = (int) e.getY();

					switch (forme.getName()) {
						case "com.example.dessin.metier.Rectangle":
							f = new Rectangle(x, y, couleur, fill, x, y);
							break;

						case "com.example.dessin.metier.Cercle":
							f = new Cercle(x, y, couleur, fill, 0);
							break;

						case "com.example.dessin.metier.Ligne":
							f = new Ligne(x, y, couleur, fill, x, y);
							break;
					}
					break;

				case MotionEvent.ACTION_MOVE:
					if (f == null)
						return false;

					switch (f.getClass().getName()) {
						case "com.example.dessin.metier.Rectangle":
							Rectangle r = (Rectangle) f;

							r.setxFin((int) e.getX());
							r.setyFin((int) e.getY());
							break;

						case "com.example.dessin.metier.Cercle":
							Cercle ce = (Cercle) f;
							double length = Math.sqrt(Math.pow(ce.getX() - e.getX(), 2) + Math.pow(ce.getY() - e.getY(), 2));

							ce.setRayon((int) length);
							break;

						case "com.example.dessin.metier.Ligne":
							Ligne l = (Ligne) f;

							l.setxFin((int) e.getX());
							l.setyFin((int) e.getY());
							break;
					}
					break;

				case MotionEvent.ACTION_UP:
					if (f != null)
						metier.addForme(f);

					f = null;
			}

			invalidate();
			return true;
		}
	}
}