package com.example.helbblitz;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInformationsActivity extends BaseActivity {
    private FirebaseUser myFirebaseUser;
    private FirebaseAuth mAuth;
    private Button backButton;
    private Button favoriteButtonImage;
    private TextView gameNameTextView;
    private ImageView gameImageView;
    private TextView gameDescriptionTextView;
    private TextView gameRatingTextView;
    private TextView gamePlaytimeTextView;
    private TextView gameReleaseDateTextView;
    private DatabaseReference currentUserFavoriteGame;

    private TextView textViewScore;

    private String gameName;

    private Button postSpeedrunScoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game_informations);

        gameNameTextView = (TextView) findViewById(R.id.game_name);

        setContentView(R.layout.activity_game_informations);
        postCurrentUserSpeedrunScore();
        displayNameAndScore();


        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String gameImageResource = getIntent().getStringExtra("gameImageResId");

        favoriteButtonImage = findViewById(R.id.favorite_button_image);
        favoriteButtonImage.setOnClickListener(new View.OnClickListener() {
            boolean isFavorite = false;

            @Override
            public void onClick(View v) {
                String gameName = gameNameTextView.getText().toString();
                if (isFavorite) {
                    //  anim de rétrécissement
                    Animation myAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    myAnimation.setDuration(200);
                    favoriteButtonImage.startAnimation(myAnimation);

                    // Coeur non rempli
                    favoriteButtonImage.setBackgroundResource(R.drawable.favorite_non_rempli);
                    isFavorite = false;

                    // On supprime le jeu a la liste des favoris du currentUser() dans firebase
                    currentUserFavoriteGame.child(gameName).removeValue();

                } else {
                    // anim de grossissement
                    Animation growAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    growAnimation.setDuration(200);
                    favoriteButtonImage.startAnimation(growAnimation);

                    // Coeur rempli
                    favoriteButtonImage.setBackgroundResource(R.drawable.favorite_rempli);
                    isFavorite = true;

                    // On ajoute le jeu a la liste des favoris du currentUser() dans firebase
                    Map<String, Object> favoriteData = new HashMap<>();
                    favoriteData.put("gameName", gameName);
                    favoriteData.put("gameImageUrl", gameImageResource);
                    currentUserFavoriteGame.child(gameName).setValue(favoriteData);
                }
            }
        });

        // Récupérer la référence de la liste de favoris de l'utilisateur courant dans Firebase
        currentUserFavoriteGame = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorites");

        // Vérification si le jeu est en favori pour définir le bon drawable de l'image favorite
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("favorites");
            favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String gameName = gameNameTextView.getText().toString();
                    if (snapshot.hasChild(gameName)) {
                        favoriteButtonImage.setBackgroundResource(R.drawable.favorite_rempli);
                    } else {
                        favoriteButtonImage.setBackgroundResource(R.drawable.favorite_non_rempli);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "loadFavorites:onCancelled", error.toException());
                }
            });
        }


        // Vérifiez si l'Intent est null avant d'appeler getIntExtra
        Intent intent = getIntent();
        if (intent != null) {
            String gameName = intent.getStringExtra("gameName");
            if (gameName == null) {
                Log.e(TAG, "gameName is null");
                return;
            }
            String gameDescription = intent.getStringExtra("gameDescription");
            float gameRating = intent.getFloatExtra("gameRating", 0.0f);
            int gamePlaytime = intent.getIntExtra("gamePlaytime", 0);
            String gameReleaseDate = intent.getStringExtra("gameReleaseDate");

            gameNameTextView = findViewById(R.id.game_name);
            gameImageView = findViewById(R.id.game_image);
            gameDescriptionTextView = findViewById(R.id.game_description);
            gameRatingTextView = findViewById(R.id.game_rating);
            gamePlaytimeTextView = findViewById(R.id.game_playtime);
            gameReleaseDateTextView = findViewById(R.id.game_releaseDate);

            gameNameTextView.setText(gameName); // Titre du jeu
            Glide.with(this).load(gameImageResource).into(gameImageView); // On utilise la bibli Glide pour charger une image a prtir d'un string
            gameDescription = Html.fromHtml("Description of the game :  " + gameDescription).toString();
            gameRatingTextView.setText("Rating: " + gameRating);
            gamePlaytimeTextView.setText("Averrage playtime: " + gamePlaytime);
            gameReleaseDateTextView.setText("Release Date: " + gameReleaseDate);

            updateGameInformation(gameImageResource, gameName, gameDescription, gameRating, gamePlaytime, gameReleaseDate);
        }
    }

    public void updateGameInformation(String gameImageResource, String gameName, String gameDescription, float gameRating, int gamePlaytime, String gameReleaseDate) {

        this.gameName = gameName;

        // Mettre à jour l'image du jeu
        gameImageView = findViewById(R.id.game_image);
        Glide.with(this).load(gameImageResource).into(gameImageView);

        // Mettre à jour le nom du jeu
        gameNameTextView = findViewById(R.id.game_name);
        gameNameTextView.setText(gameName);

        // Mettre à jour la description du jeu
        gameDescriptionTextView = findViewById(R.id.game_description);
        gameDescriptionTextView.setText(gameDescription);

        // Mettre à jour la note du jeu
        gameRatingTextView = findViewById(R.id.game_rating);
        gameRatingTextView.setText("Rating : " + gameRating + "/5");

        // Mettre à jour le temps de jeu du jeu
        gamePlaytimeTextView = findViewById(R.id.game_playtime);
        gamePlaytimeTextView.setText("Average playtime : " + gamePlaytime + " hours");

        // Mettre à jour la date de sortie du jeu
        gameReleaseDateTextView = findViewById(R.id.game_releaseDate);
        gameReleaseDateTextView.setText("Release Date : " + gameReleaseDate);


    }

    private void postCurrentUserSpeedrunScore() {
        postSpeedrunScoreButton = findViewById(R.id.post_score_button);
        postSpeedrunScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un EditText pour que le joueur entre son score
                AlertDialog.Builder builder = new AlertDialog.Builder(GameInformationsActivity.this);
                builder.setTitle("Enter your score ! (hours format)");

                final EditText input = new EditText(GameInformationsActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth = FirebaseAuth.getInstance();
                        myFirebaseUser = mAuth.getCurrentUser();
                        String playerScore = input.getText().toString();

                        if (myFirebaseUser != null) {
                            String userID = myFirebaseUser.getUid();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User userProfile = snapshot.getValue(User.class);
                                    if (userProfile != null) {
                                        String playerName = userProfile.getUsername();
                                        String playerCountry = userProfile.getCountry();

                                        // Créer un objet Map pour stocker le nom du joueur et son score
                                        Map<String, String> scoreMap = new HashMap<>();
                                        scoreMap.put("name", playerName);
                                        scoreMap.put("score", playerScore);
                                        scoreMap.put("country", playerCountry);

                                        // Ajouter le score du joueur dans la section "score" du jeu dans Firebase
                                        String gameName = gameNameTextView.getText().toString();
                                        DatabaseReference gameScoreRef = FirebaseDatabase.getInstance().getReference("GamesName").child(gameName).child("score").child(userID);
                                        gameScoreRef.setValue(scoreMap);

                                        // Afficher un toast pour confirmer que le score a été posté avec succès
                                        Toast.makeText(GameInformationsActivity.this, "Score posted successfully !", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.w(TAG, "loadUser:onCancelled", error.toException());
                                }
                            });
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        if (gameNameTextView.getText() == null) {
            Log.e(TAG, "gameName is null");
            return;
        }
    }

    private void displayNameAndScore() {
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("GamesName");

        scoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder stringBuilder = new StringBuilder();

                for (DataSnapshot gameSnapshot : dataSnapshot.getChildren()) {
                    String gameName = gameSnapshot.getKey();
                    if (gameName != null) {
                        DatabaseReference gameScoreRef = FirebaseDatabase.getInstance().getReference("GamesName").child(gameName).child("score");

                        gameScoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                stringBuilder.setLength(0);  // Clear the StringBuilder

                                for (DataSnapshot scoreSnapshot : snapshot.getChildren()) {
                                    String country = scoreSnapshot.child("country").getValue(String.class);
                                    String name = scoreSnapshot.child("name").getValue(String.class);
                                    String score = scoreSnapshot.child("score").getValue(String.class);

                                    String playerInfo = name + " : " + score + "h - " + country;
                                    stringBuilder.append(playerInfo).append("\n");
                                }

                                String allPlayerInfo = stringBuilder.toString();

                                // Afficher les informations dans le TextView
                                textViewScore = findViewById(R.id.score_infos);
                                textViewScore.setText(allPlayerInfo);
                                Log.d(TAG, "onDataChange: " + allPlayerInfo);

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Gérer les erreurs Firebase ici
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs Firebase ici
            }
        });
    }
}


