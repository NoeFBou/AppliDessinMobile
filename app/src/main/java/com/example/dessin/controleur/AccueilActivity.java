package com.example.dessin.controleur;

import static com.example.dessin.controleur.DessinActivity.SHARED_PREF_USER_INFO;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.dessin.R;

public class AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        findViewById(R.id.btnNouvDessin).setOnClickListener(view -> {
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            startActivity(new Intent(AccueilActivity.this, DessinActivity.class));
        });

        findViewById(R.id.btnReprendreDessin).setOnClickListener(view -> {
            startActivity(new Intent(AccueilActivity.this, DessinActivity.class));
        });

        findViewById(R.id.btnQuitter).setOnClickListener(view -> finish());
    }
}