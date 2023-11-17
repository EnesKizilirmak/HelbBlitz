package com.example.helbblitz;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserprofileFragment extends BaseFragment {

    // -------------- Declaration des elements --------------------------- //
    private static final int PICK_IMAGE_REQUEST = 1;

    private CircleImageView profileImage;
    private Button chooseImageButton, logout;
    private FirebaseAuth mAuth;
    private FirebaseUser myFirebaseUser;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String userID;
    private Uri imageUri;
    private TextView gameNameTextView;
    private List<ImageView> myImageViewList = new ArrayList<>();


    public UserprofileFragment() {
        // Required empty public constructor
    }

    public static UserprofileFragment newInstance() {
        return new UserprofileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);
        getCurrentUserFavoriteGames();

        //TEST
        // uploadFavoriteGames();

        // -------------- Initialisation des elements --------------------------- //
        logout = (Button) view.findViewById(R.id.signOut);
        mAuth = FirebaseAuth.getInstance();
        myFirebaseUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        // -------------- Partie Afficher image user --------------------------- //
        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        chooseImageButton = (Button) view.findViewById(R.id.choose_image_button);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        // -------------- Firebase (récupérer le nom de l'utilisateur actif) --------------------------- //
        final TextView myEmailTextView = (TextView) view.findViewById(R.id.mailTextView);
        TextView myUsernameTextView = (TextView) view.findViewById(R.id.usernameTextView);
        TextView myCountryTextView = (TextView) view.findViewById(R.id.textView_country_);

        final String guestString = "Guest";

        if (myFirebaseUser != null) {
            userID = myFirebaseUser.getUid();

            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userProfile = snapshot.getValue(User.class);

                    if (userProfile != null) {
                        String email = userProfile.email;
                        String username = userProfile.username;
                        String country = userProfile.country;

                        myEmailTextView.setText(email);
                        myUsernameTextView.setText(username);
                        myCountryTextView.setText(country);


                        // Chargement de l'image de profil de l'utilisateur actif depuis Firebase Storage
                        storageReference.child("images/" + userID + "/profile.jpg").getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        if (isAdded()) {
                                            Glide.with(getContext())
                                                    .load(uri)
                                                    .placeholder(R.drawable.default_profile_image)
                                                    .error(R.drawable.default_profile_image)
                                                    .into(profileImage);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(getContext(), "You can add your own profile image ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Something wrong happened !", Toast.LENGTH_LONG).show();
                }
            });
        } else { // User est null on affiche le String "Guest"
            myEmailTextView.setText(guestString);
            myUsernameTextView.setText(guestString);
        }

        // -------------- Partie Sign Out --------------------------- //
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création d'une boîte de dialogue de confirmation
                new AlertDialog.Builder(getContext())
                        .setTitle("Log out")
                        .setMessage("Do you really want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // L'utilisateur a cliqué sur "Oui"
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getContext(), WelcomeActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        return view;
    }

    // Methode pour récupérer l'image sélectionnée
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Afficher l'image sélectionnée dans l'ImageView
            Glide.with(getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.default_profile_image)
                    .error(R.drawable.default_profile_image)
                    .into(profileImage);
        }


        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child("images/" + userID + "/profile.jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Mettre à jour l'URL de l'image dans la base de données Firebase Realtime
                                    reference.child(userID).child("profileImageUrl").setValue(uri.toString());
                                    Toast.makeText(getContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getCurrentUserFavoriteGames() {
        DatabaseReference currentUserFavoritesRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorites");
        currentUserFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    String gameName = gameSnapshot.child("gameName").getValue(String.class);
                    String gameImageUrl = gameSnapshot.child("gameImageUrl").getValue(String.class);

                    // Afficher l'image du jeu correspondant dans la bonne ImageView
                    ImageView imageView = null;
                    switch (i) {
                        case 1:
                            imageView = getView().findViewById(R.id.fav_image_view_number_1);
                            break;
                        case 2:
                            imageView = getView().findViewById(R.id.fav_image_view_number_2);
                            break;
                        case 3:
                            imageView = getView().findViewById(R.id.fav_image_view_number_3);
                            break;
                        case 4:
                            imageView = getView().findViewById(R.id.fav_image_view_number_4);
                            break;
                        case 5:
                            imageView = getView().findViewById(R.id.fav_image_view_number_5);
                            break;
                        case 6:
                            imageView = getView().findViewById(R.id.fav_image_view_number_6);
                            break;
                        case 7:
                            imageView = getView().findViewById(R.id.fav_image_view_number_7);
                            break;
                        case 8:
                            imageView = getView().findViewById(R.id.fav_image_view_number_8);
                            break;
                        case 9:
                            imageView = getView().findViewById(R.id.fav_image_view_number_9);
                            break;
                    }

                    if (imageView != null) {
                        Picasso.get().load(gameImageUrl).into(imageView);
                    }

                    i++;
                    int nombreDeJeuxFavorisMaximum=9;
                    if(i > nombreDeJeuxFavorisMaximum) {
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadFavorites:onCancelled", error.toException());
            }
        });
    }



}