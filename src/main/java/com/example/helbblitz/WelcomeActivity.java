package com.example.helbblitz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    // -------------- Declaration des elements --------------------------- //
    private Button loginButton, signUpButton, guestButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // -------------- Initialisation de mes éléments --------------------------- //
        loginButton = (Button) findViewById(R.id.login_button);
        signUpButton = (Button) findViewById(R.id.signup_button);
        guestButton = (Button) findViewById(R.id.guest_button);

        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        guestButton.setOnClickListener(this);

        // Si un Utilisateur est connecté, alors redirection directement vers la MainActivity
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                Intent LoginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
                break;

            case R.id.signup_button:
                Intent SignUpIntent = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(SignUpIntent);
                break;

            case R.id.guest_button:
                Intent GuestIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(GuestIntent);
                break;

        }
    }
}
