package com.example.helbblitz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    // -------------- Declaration des elements --------------------------- //
    private EditText usernameInput, emailInput, passwordInput, cpasswordInput;
    private Button signupButton;
    private TextView alreadyHaveAccountTextView;
    private CheckBox acceptCheckBox;

    public ImageView paysImage;  //Public car je dois l'utiliser dans ma classe UserProfile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // -------------- Initialisation de mes éléments --------------------------- //

        usernameInput = (EditText) findViewById(R.id.username_input);
        emailInput = (EditText) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        cpasswordInput = (EditText) findViewById(R.id.confirmPassword_input);

        signupButton = (Button) findViewById(R.id.signup_button);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Message");
        myRef.setValue("Welcome to Helb Blitz Realtime Database !");

        paysImage = findViewById(R.id.pays_image);
        Spinner paysSpinner = findViewById(R.id.pays_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pays_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paysSpinner.setAdapter(adapter);

        paysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountry = parent.getItemAtPosition(position).toString();

                // Par rapport au pays selectionné l'image changera
                if (selectedCountry.equals("Australia")) {
                    paysImage.setImageResource(R.drawable.flag_australia);
                } else if (selectedCountry.equals("Belgium")) {
                    paysImage.setImageResource(R.drawable.flag_belgium);
                } else if (selectedCountry.equals("Brazil")) {
                    paysImage.setImageResource(R.drawable.flag_brazil);
                } else if (selectedCountry.equals("Canada")) {
                    paysImage.setImageResource(R.drawable.flag_canada);
                } else if (selectedCountry.equals("China")) {
                    paysImage.setImageResource(R.drawable.flag_china);
                } else if (selectedCountry.equals("Denmark")) {
                    paysImage.setImageResource(R.drawable.flag_denmark);
                } else if (selectedCountry.equals("England")) {
                    paysImage.setImageResource(R.drawable.flag_england);
                } else if (selectedCountry.equals("Finland")) {
                    paysImage.setImageResource(R.drawable.flag_finland);
                } else if (selectedCountry.equals("France")) {
                    paysImage.setImageResource(R.drawable.flag_france);
                } else if (selectedCountry.equals("Germany")) {
                    paysImage.setImageResource(R.drawable.flag_germany);
                } else if (selectedCountry.equals("Greece")) {
                    paysImage.setImageResource(R.drawable.flag_greece);
                } else if (selectedCountry.equals("India")) {
                    paysImage.setImageResource(R.drawable.flag_india);
                } else if (selectedCountry.equals("Indonesia")) {
                    paysImage.setImageResource(R.drawable.flag_indonesia);
                } else if (selectedCountry.equals("Ireland")) {
                    paysImage.setImageResource(R.drawable.flag_ireland);
                } else if (selectedCountry.equals("Italy")) {
                    paysImage.setImageResource(R.drawable.flag_italy);
                } else if (selectedCountry.equals("Japan")) {
                    paysImage.setImageResource(R.drawable.flag_japan);
                } else if (selectedCountry.equals("Mexico")) {
                    paysImage.setImageResource(R.drawable.flag_mexico);
                } else if (selectedCountry.equals("Morocco")) {
                    paysImage.setImageResource(R.drawable.flag_morocco);
                } else if (selectedCountry.equals("Norway")) {
                    paysImage.setImageResource(R.drawable.flag_norway);
                } else if (selectedCountry.equals("Russia")) {
                    paysImage.setImageResource(R.drawable.flag_russia);
                } else if (selectedCountry.equals("South Africa")) {
                    paysImage.setImageResource(R.drawable.flag_southafrica);
                } else if (selectedCountry.equals("Denmark")) {
                    paysImage.setImageResource(R.drawable.flag_denmark);
                } else if (selectedCountry.equals("Spain")) {
                    paysImage.setImageResource(R.drawable.flag_spain);
                } else if (selectedCountry.equals("Turkey")) {
                    paysImage.setImageResource(R.drawable.flag_turkey);
                } else if (selectedCountry.equals("United States")) {
                    paysImage.setImageResource(R.drawable.flag_usa);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Rien se passe si rien n'est sélectionné
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signup_button:
                        registerUser();
                        break;
                }

                // -------------- Partie Already have an account -> activity Log In --------------------------- //
                alreadyHaveAccountTextView = (TextView) findViewById(R.id.alreadyAccount_TextView);
                alreadyHaveAccountTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.alreadyAccount_TextView:
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                break;
                        }
                    }
                });
            }

            // -------------- Partie Firebase --------------------------- //
            private void registerUser() {
                ProgressBar myProgressBar = findViewById(R.id.progressBar);
                myProgressBar.setVisibility(View.VISIBLE);

                acceptCheckBox = findViewById(R.id.checkbox_accept);


                // Récupérez les valeurs des champs de saisie
                String name = usernameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String confirmPassword = cpasswordInput.getText().toString().trim();
                String country = paysSpinner.getSelectedItem().toString().trim();

                // Vérifiez si tous les champs sont remplis
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || !isCheckBoxChecked()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields and agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                    myProgressBar.setVisibility(View.GONE);
                    return;
                }


                // Vérifiez si l'adresse e-mail est valide
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Invalid email address");
                    emailInput.requestFocus();

                    myProgressBar.setVisibility(View.GONE);
                    return;
                }

                // Vérifiez si le mot de passe répond aux exigences de longueur minimale
                if (password.length() < 6) {
                    passwordInput.setError("Password should be at least 6 characters long");
                    passwordInput.requestFocus();

                    myProgressBar.setVisibility(View.GONE);
                    return;
                }

                // Vérifiez si les mots de passe correspondent
                if (!password.equals(confirmPassword)) {
                    cpasswordInput.setError("Passwords do not match");
                    cpasswordInput.requestFocus();
                    myProgressBar.setVisibility(View.GONE);
                    return;
                }

                // Vérifiez si le CheckBox est coché
                if (!isCheckBoxChecked()) {
                    acceptCheckBox.setError("Please agree to the terms and conditions");
                    acceptCheckBox.requestFocus();
                    myProgressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        myProgressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            User user = new User(name, email, null, country);

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));


                                        Toast.makeText(SignUpActivity.this, "User is registered !", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        intent.putExtra("email", email);
                                        intent.putExtra("password", password);

                                        startActivity(intent);

                                        //sendNotification();
                                        usernameInput.setText("");
                                        emailInput.setText("");
                                        passwordInput.setText("");
                                        cpasswordInput.setText("");
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to register !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private boolean isCheckBoxChecked() {
        // Vérifiez si le CheckBox est coché
        if (!acceptCheckBox.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

  /*  private void sendNotification() {
        // -------------- Partie notification --------------------------- //
        try {
            // Créez un canal de notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_id", "Nom du canal", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Description du canal");
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // Creer une notification avec le canal de notification
            EditText userNameEditText = findViewById(R.id.username_input);
            String userName = userNameEditText.getText().toString();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(SignUpActivity.this, "channel_id").setSmallIcon(R.drawable.ic_notifications_signup).setContentTitle("Inscription réussie").setContentText("Bienvenue " + userName + " !").setPriority(NotificationCompat.PRIORITY_DEFAULT);
            try {
                // Afficher la notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(SignUpActivity.this);
                notificationManager.notify(1, builder.build());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } */
}
