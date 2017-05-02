package com.example.user.jobby.model;


import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mathieu Virsolvy on 19/04/2017.
 */

public class Marker {
    private String id, title, description, fullDescription;
    private LatLng  location;
    private float zoneRadius;

    public Marker(String id, String title, String description, String fullDescription, LatLng location, float zoneRadius) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fullDescription = fullDescription;
        this.location = location;
        this.zoneRadius = zoneRadius;
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

    public String getFullDescription() {
        return fullDescription;
    }

    public LatLng getLocation() {
        return location;
    }

    public float getZoneRadius() {
        return zoneRadius;
    }

    public Marker(String id) {
        this.id = id;
    }
}
