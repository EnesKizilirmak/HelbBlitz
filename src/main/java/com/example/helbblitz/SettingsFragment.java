package com.example.helbblitz;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SettingsFragment extends BaseFragment {

    private Button testNotif;
    private Switch switchNotification;

    private List<String> gameName;

    public SettingsFragment() {
        // Constructeur public vide requis
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Récupérer les références des boutons de thème
        testNotif = view.findViewById(R.id.buttonTestNotif);
        switchNotification = view.findViewById(R.id.switchNotification);

        testNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Bouton test notif cliqué ");
                if (switchNotification.isChecked()) {
                    sendTestNotification();
                } else {
                    // Le switch est désactivé, ne rien faire
                }
            }
        });

        gameName = Arrays.asList(
                "Assassin's Creed Odyssey",
                "Borderlands 3",
                "Bloodborne",
                "Death Stranding",
                "Far Cry 3",
                "Far Cry 5",
                "Forza Horizon 5",
                "Horizon Zero Dawn",
                "Mafia II",
                "Saints Row IV",
                "The Elder Scrolls V: Skyrim",
                "The Witcher 3: Wild Hunt",
                "Watch Dogs"
        );
        return view;
    }


    @SuppressLint("MissingPermission")
    private void sendTestNotification() {
        // -------------- Partie notification --------------------------- //
        try {

            Random random = new Random();
            String randomGame = gameName.get(random.nextInt(gameName.size()));

            // Créer un Intent pour ouvrir la HomeFragment lorsque la notification est cliquée
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("OPEN_FRAGMENT", "HOME"); // Ajouter des données supplémentaires si nécessaire
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "channel_id")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle("Helb Blitz !")
                    .setContentText("Découvrez le jeu du jour : " + randomGame)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent) // Définir l'Intent pour ouvrir la HomeFragment
                    .setAutoCancel(true); // Fermer la notification lorsque l'utilisateur clique dessus


            // Afficher la notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
            notificationManager.notify(1, builder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
