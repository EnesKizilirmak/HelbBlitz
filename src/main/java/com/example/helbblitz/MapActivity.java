package com.example.helbblitz;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;


import java.util.ArrayList;

public class MapActivity extends BaseActivity {

    // -------------- Declaration des elements --------------------------- //
    private MapView map;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_map);

        // Bouton de retour
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Demande d'autorisation de localisation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        // Parametrage de la map
        map = findViewById(R.id.map_view);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        GeoPoint startPoint = new GeoPoint(50.8179100, 4.3969300);
        IMapController mapController = map.getController();
        mapController.setZoom(6);
        mapController.setCenter(startPoint);

        ArrayList<OverlayItem> myItemsList = new ArrayList<>();


        String[][] myListOfOverlay = {
                {"Alice", "24h32m", "Belgium", "50.8179100", "4.3969300", "flag_belgium"},
                {"John", "37h15m", "United States", "37.0902", "-95.7129", "flag_usa"},
                {"Dimitri", "16h45m", "Russia", "55.7527", "37.6144", "flag_russia"},
                {"Lucas", "/", "Australia", "-25.2744", "133.7751", "flag_australia"},
                {"Julia", "8h50m", "Brazil", "-14.2350", "-51.9253", "flag_brazil"},
                {"Sophie", "19h20m", "Canada", "56.1304", "-106.3468", "flag_canada"},
                {"Chen", "/", "China", "35.8617", "104.1954", "flag_china"},
                {"Nikolaj", "12h40m", "Denmark", "56.2639", "9.5018", "flag_denmark"},
                {"William", "28h55m", "England", "52.3555", "-1.1743", "flag_england"},
                {"Anna", "7h05m", "Finland", "61.9241", "25.7482", "flag_finland"},
                {"Antoine", "13h30m", "France", "46.2276", "2.2137", "flag_france"},
                {"Lena", "/", "Germany", "51.1657", "10.4515", "flag_germany"},
                {"Giannis", "9h15m", "Greece", "39.0742", "21.8243", "flag_greece"},
                {"Raj", "32h20m", "India", "20.5937", "78.9629", "flag_india"},
                {"Yudhistira", "6h55m", "Indonesia", "-0.7893", "113.9213", "flag_indonesia"},
                {"Seán", "18h30m", "Ireland", "53.1424", "-7.6921", "flag_ireland"},
                {"Mario", "25h40m", "Italy", "41.9028", "12.4964", "flag_italy"},
                {"Takashi", "/", "Japan", "36.2048", "138.2529", "flag_japan"},
                {"Luis", "/", "Mexico", "23.6345", "-102.5528", "flag_mexico"},
                {"Hassan", "10h10m", "Morocco", "31.7917", "-7.0926", "flag_morocco"},
                {"Sofie", "23h20m", "Norway", "60.4720", "8.4689", "flag_norway"},
                {"Dimitri", "16h45m", "Russia", "55.7527", "37.6144", "flag_russia"},
                {"Thabo", "/", "South Africa", "-30.5595", "22.9375", "flag_southafrica"},
                {"Pablo", "20h15m", "Spain", "40.4637", "-3.7492", "flag_spain"},
                {"Berkay", "9h30m", "Turkey", "38.9637", "35.2433", "flag_turkey"}

        };

        // Taille en dp de l'image redimensionnée
        int imageSizeInDp = 24;

        // Redimensionner chaque marqueur et les ajouter à la liste
        for (String[] index : myListOfOverlay) {
            String username = index[0];
            String score = index[1];
            String countryName = index[2];
            double latitude = Double.parseDouble(index[3]);
            double longitude = Double.parseDouble(index[4]);
            String flag_of_country = index[5];

            // Charger l'image depuis les ressources de l'application
            Drawable originalMarker = getResources().getDrawable(getResources().getIdentifier(flag_of_country, "drawable", getPackageName()));

            // Calculer la taille en pixels correspondant à imageSizeInDp dp
            float scale = getResources().getDisplayMetrics().density;
            int imageSizeInPixels = (int) (imageSizeInDp * scale + 0.5f);

            // Redimensionner l'image à la taille voulue
            Bitmap bitmap = ((BitmapDrawable) originalMarker).getBitmap();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, imageSizeInPixels, imageSizeInPixels, false);

            // Créer un drawable à partir de l'image redimensionnée et l'ajouter à la liste
            Drawable marker = new BitmapDrawable(getResources(), resizedBitmap);
            OverlayItem item = new OverlayItem(username, score , new GeoPoint(latitude, longitude));
            item.setMarker(marker);
            myItemsList.add(item);
        }


        // Association de map et des overlays
        ItemizedOverlayWithFocus<OverlayItem> myOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(), myItemsList, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;    // Réaction lorsque quand je clique l'overlay
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return true;
            }
        });
        myOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(myOverlay);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
            } else {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
