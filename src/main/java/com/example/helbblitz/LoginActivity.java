package com.example.helbblitz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // -------------- Declaration des elements --------------------------- //
    private EditText editTextEmail, editTextPassword;
    private TextView forgotPassword, noAccountTextView;
    private Button logInButton;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // -------------- Initialisation de mes éléments --------------------------- //
        logInButton = findViewById(R.id.login_button);
        editTextEmail = findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();

        logInButton.setOnClickListener(this);

        // -------------- Partie Don't have an account -> activity Sign Up --------------------------- //
        noAccountTextView = findViewById(R.id.noAccount_TextView);
        noAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.noAccount_TextView:
                        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                        break;
                }
            }
        });
        // -------------- Partie forgot password --------------------------- //
        forgotPassword = findViewById(R.id.forgotPassword_TextView);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.forgotPassword_TextView:
                        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                        break;
                }
            }
        });

        // -------------- Pré-remplissage des champs d'édition --------------------------- //
        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("email");
            String password = intent.getStringExtra("password");

            if (email != null) {
                editTextEmail.setText(email);
            }

            if (password != null) {
                editTextPassword.setText(password);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == logInButton) {
            userLogin();
        }
    }

    // -------------- Partie Firebase --------------------------- //
    private void userLogin() {
        ProgressBar myProgressbar = findViewById(R.id.login_progressBar);
        myProgressbar.setVisibility(View.VISIBLE);


        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            editTextEmail.setError("");
            editTextPassword.setError("");
            Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();

            myProgressbar.setVisibility(View.GONE);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email format");
            editTextEmail.requestFocus();

            myProgressbar.setVisibility(View.GONE);
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password must have at least 6 characters");
            editTextPassword.requestFocus();

            myProgressbar.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect informations !", Toast.LENGTH_LONG).show();
                    Log.d("LOGIN", "Error: " + task.getException().getMessage());
                }
            }
        });
    }
}


