package com.example.travelapp.Domain;

public class Location {
    private int Id;
    private String Loc;

    public Location() {
        // Required empty constructor for Firebase
    }

    @Override
    public String toString() {
        return Loc;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }
}
