package com.example.user.jobby.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mathieu Virsolvy on 19/04/2017.
 */

public class Route {

    public enum Mode {
        UNSPECIFIED,
        TOURISTIC,
        SPORTY;
    }
    private String id, title, description;
    private int difficulty;
    private double rating;
    private Mode mode;
    private Date published, lastUpdated;
    private ArrayList<Marker> markers;

    public Route(String id, String title, String description, int difficulty, double rating, Mode mode, Date published, Date lastUpdated, ArrayList<Marker> markers) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.rating = rating;
        this.mode = mode;
        this.published = published;
        this.lastUpdated = lastUpdated;
        this.markers=markers;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getRating() {
        return rating;
    }

    public Mode getMode() {
        return mode;
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public Date getPublished() {
        return published;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
