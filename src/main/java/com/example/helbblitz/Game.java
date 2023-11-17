package com.example.helbblitz;

import java.io.Serializable;

public class Game implements Serializable { // Serializable permet de passer l'object game dans un intant (pour notamment permettre d'aller dans GameInformations avec un putextra)
    // -------------- Declaration des elements --------------------------- //
    private String name;
    private String description;
    private float rating;
    private int playtime;
    private String releaseDate;
    private String imageUrl;
    private int id;

    public Game() {}
    public Game(String name, String description, float rating, int playtime, String releaseDate, String imageUrl, int id) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.playtime = playtime;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public int getPlaytime() {
        return playtime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }
}
