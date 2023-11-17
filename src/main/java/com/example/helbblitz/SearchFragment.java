package com.example.helbblitz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends BaseFragmentWithData {
    private ArrayAdapter<String> adapter;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        List<String> games = new ArrayList<>(Arrays.asList(
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
        ));


        ListView listView = view.findViewById(R.id.list_view);
        SearchView searchView = view.findViewById(R.id.search_view);

        // Ouvrir la barre de recherche par défaut
        searchView.setIconifiedByDefault(false);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, games);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedGame = adapter.getItem(position);

            if (selectedGame.equals("Mafia II")) {
                Intent intent = new Intent(requireContext(), GameInformationsSearchActivity.class);
                startActivity(intent);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }
}