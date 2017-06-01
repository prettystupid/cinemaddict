package com.prestu.gambler.domain;

import com.vaadin.server.ThemeResource;

public class User {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean male;
    private String email;
    private String city;
    private String website;
    private String bio;
    private ThemeResource logo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public void setLogo(ThemeResource logo) {
        this.logo = logo;
    }

    public ThemeResource getLogo() {
        return logo;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof User) && (((User) o).getUsername().equals(username));
    }
}
