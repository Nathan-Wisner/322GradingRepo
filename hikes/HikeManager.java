package com.hike.wa.hikes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/* Singleton class that holds a list of all available hikeList */
public class HikeManager {

    // Shows a list of hiking places
    private static HikeManager _instance;
    private ArrayList<Hike> hikeList;

    private HikeManager() {
        hikeList = new ArrayList<>();
    }

    public boolean isEmpty(){
        return _instance == null;
    }


    public static HikeManager getInstance() {
        if (_instance == null) {
            _instance = new HikeManager();
        }
        return _instance;
    }

    // Used to write new hikeList instead of manually entering them on Firebase
    public void writeHike(Hike hike) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("hikeList");
        ref.child(hike.getTitle()).setValue(hike);
    }

    public void addHikes(ArrayList<Hike> hikes) {
        this.hikeList.addAll(hikes);
    }

    /* Getters */
    public ArrayList<Hike> getHikeList() { return hikeList; }

    /* Setters */

}
