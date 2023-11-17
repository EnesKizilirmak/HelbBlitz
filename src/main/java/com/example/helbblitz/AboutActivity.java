package com.example.helbblitz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Récupération du bouton de retour
        backButton = findViewById(R.id.back_button_about);

        // Ajout d'un listener sur le bouton de retour
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedWithAnimation();
            }
        });
    }

    // Méthode appelée lors du clic sur le bouton de retour
    public void onBackPressedWithAnimation() {
        // Ajout d'une animation de transition lors du retour à l'activité précédente
        finish();
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
    }
}
