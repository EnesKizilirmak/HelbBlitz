package com.example.helbblitz;

public class User {

    // -------------- Declaration des elements --------------------------- //
    protected String username;
    protected String email;
    protected String profileImageURL;
    protected String country;

    public User(){}

    public User(String _username, String _email, String _profileImageURL, String _country) {
        this.username = _username;
        this.email = _email;
        this.profileImageURL = _profileImageURL;
        this.country = _country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String _email) {
        this.email = _email;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String _profileImageURL) {
        this.profileImageURL = _profileImageURL;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String _country) {
        this.country = _country;
    }
}
