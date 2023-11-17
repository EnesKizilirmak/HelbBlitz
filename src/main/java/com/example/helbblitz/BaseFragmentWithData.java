package com.example.helbblitz;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseFragmentWithData extends BaseFragment {

    // -------------- Déclaration des éléments --------------------------- //
    ArrayList<String> myUrlList = new ArrayList<String>();
    ArrayList<ImageView> myImageViewList = new ArrayList<ImageView>();

    protected List<TextView> myTextViewList = new ArrayList<>();

    protected Map<Integer, Game> myGameHashMap = new HashMap<>();


    // Mes Informations récupérées depuis la requête JSON sur RAWG.IO
//    private String name_fromJson;
//    private String description_fromJson;
//    private float rating_fromJson;
//    private int playtime_fromJson;
//    private String releaseDate_fromJson;
//    private String imageUrl_fromJson;

    protected void loadGameImages() {

        myUrlList.add("https://api.rawg.io/api/games/far-cry-5?key=");
        myUrlList.add("https://api.rawg.io/api/games/forza-horizon-5?key=");
        myUrlList.add("https://api.rawg.io/api/games/the-elder-scrolls-v-skyrim?key=");

        myUrlList.add("https://api.rawg.io/api/games/borderlands-3?key=");
        myUrlList.add("https://api.rawg.io/api/games/mafia-ii?key=");
        myUrlList.add("https://api.rawg.io/api/games/death-stranding?key=");

        myUrlList.add("https://api.rawg.io/api/games/saints-row-iv?key=");
        myUrlList.add("https://api.rawg.io/api/games/far-cry-3?key=");

        myUrlList.add("https://api.rawg.io/api/games/watch-dogs?key=");

        myUrlList.add("https://api.rawg.io/api/games/the-witcher-3-wild-hunt?key=");
        myUrlList.add("https://api.rawg.io/api/games/horizon-zero-dawn?key=");
        myUrlList.add("https://api.rawg.io/api/games/bloodborne?key=");
        myUrlList.add("https://api.rawg.io/api/games/assassins-creed-odyssey?key=");

        myUrlList.add("https://api.rawg.io/api/games/gran-turismo-sport?key=");
        myUrlList.add("https://api.rawg.io/api/games/resident-evil-village?key=");
        myUrlList.add("https://api.rawg.io/api/games/tekken-7?key=");
        myUrlList.add("https://api.rawg.io/api/games/red-dead-redemption-2?key=");


        // Ajouter toutes les Id de mes ImageView à ma liste (exemple : image_view_number_1, image_view_number_2, etc...)
        for (int i = 1; i <= myUrlList.size(); i++) {
            int imageViewId = getResources().getIdentifier("image_view_number_" + i, "id", requireContext().getPackageName());
            myImageViewList.add(requireView().findViewById(imageViewId));
        }

        // Ajouter tous les IDs de mes TextView à ma liste (exemple : text_view_number_1, text_view_number_2, etc...)
        for (int i = 1; i <= myUrlList.size(); i++) {
            int textViewId = getResources().getIdentifier("text_view_number_" + i, "id", requireContext().getPackageName());
            myTextViewList.add(requireView().findViewById(textViewId));
        }

        // Faire les requêtes API sur RAWG.IO pour chaque URL, ImageView et TextView
        for (int i = 0; i < myUrlList.size(); i++) {
            String url = myUrlList.get(i); // URL courante
            ImageView imageView = myImageViewList.get(i); // ImageView correspondante
            TextView textView = myTextViewList.get(i); // TextView correspondante

            // Faire la requête API pour l'URL courante
            makeGameApiRequest(url, imageView, textView, i);
        }
    }


    private void makeGameApiRequest(String url, ImageView imageView, TextView textView, int i) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        final String apiKey = "98776ecfc2c246a6ad04e8a86aac0257";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + apiKey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);

                            // Récupérer les informations du jeu depuis la réponse JSON
                            String name_fromJson = jObject.getString("name");
                            String description_fromJson = jObject.getString("description");
                            float rating_fromJson = (float) jObject.optDouble("rating");
                            int playtime_fromJson = jObject.getInt("playtime");
                            String releaseDate_fromJson = jObject.getString("released");
                            String imageUrl_fromJson = jObject.getString("background_image");
                            int gameId_fromJson = jObject.getInt("id");

                            // Ajouter un nouvel objet Game à la liste myGameList
                            Game newGame = new Game(name_fromJson, description_fromJson, rating_fromJson, playtime_fromJson, releaseDate_fromJson, imageUrl_fromJson, gameId_fromJson);
                            addGameToList(newGame);

                            // Afficher le nom du jeu en dessous de l'image (jeu)
                            textView.setText(name_fromJson);

                            // Afficher l'image du jeu dans l'ImageView correspondante
                            Picasso.get()
                                    .load(imageUrl_fromJson)
                                    .fit()
                                    .centerCrop()
                                    .placeholder(R.drawable.sub_image_foreground)
                                    .error(R.drawable.error_loading_api_image_foreground)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                                    .into(imageView);

                            setImageViewListener(newGame, imageView); // Ajouter le Listener pour lancer l'activité GameInformations

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    private void addGameToList(Game game) {
        myGameHashMap.put(game.getId(), game);
    }

    public void setImageViewListener(Game game, ImageView imageView) {
        // Ajouter le Listener pour lancer l'activité GameInformations
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Démarrer l'activité GameInformations avec les informations du jeu correspondant

                Intent intent = new Intent(requireContext(), GameInformationsActivity.class);
                intent.putExtra("game", game);
                intent.putExtra("gameImageResId", game.getImageUrl());
                intent.putExtra("gameName", game.getName());
                intent.putExtra("gameDescription", game.getDescription());
                intent.putExtra("gameRating", game.getRating());
                intent.putExtra("gamePlaytime", game.getPlaytime());
                intent.putExtra("gameReleaseDate", game.getReleaseDate());
                startActivity(intent);
            }
        });
    }
}