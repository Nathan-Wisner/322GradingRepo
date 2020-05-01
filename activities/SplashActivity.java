package com.hike.wa.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hike.wa.MainActivity;
import com.hike.wa.PermissionManager;
import com.hike.wa.adapters.RouteFinding;
import com.hike.wa.hikes.Hike;
import com.hike.wa.hikes.HikeManager;
import com.hike.wa.weather.MyLocationManager;


import java.util.ArrayList;

/* An outside class can access the hike data from 'SplashActivity.hikes' */

//Used for getting data before the app opens
public class SplashActivity extends AppCompatActivity {

    // Used as temp ArrayList by readHikes
    private ArrayList<Hike> hikes;
    double lat, lon;
    public static Location userLocation = null;
    RouteFinding routeFinding = null;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hikes = new ArrayList<>();
        routeFinding = new RouteFinding();
        getLocation();      //Gets location of user, stores in userLocation


            readHikes(new FirebaseCallback() {
                @Override
                public void onCallback(ArrayList<Hike> hikes) {
                    // Add hikes to HikeManager and start the MainActivity
                    HikeManager.getInstance().addHikes(hikes);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            });





    }

    /* Reads in hikes from Firebase */
    private void readHikes(final FirebaseCallback firebaseCallback) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("hikes");
        db.orderByValue().limitToFirst(27).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot hikeSnapshot : dataSnapshot.getChildren()) {
                    if(!hikes.contains(hikeSnapshot)) {
                        try {
                            // Log all data
                            //Log.d("DatabaseTesting", dataSnapshot.getValue().toString());

                            // Get data from snapshot
                            double latitude = Double.valueOf(hikeSnapshot.child("latitude").getValue()
                                    .toString());
                            double longitude = Double.valueOf(hikeSnapshot.child("longitude").getValue()
                                    .toString());
                            String distanceToHike = routeFinding.getRouteData(userLocation.getLatitude(), userLocation.getLongitude(), latitude, longitude);
                            int milesAway = routeFinding.getTravelValue();
                            double length = Double.valueOf(hikeSnapshot.child("length").getValue().
                                    toString());
                             title = hikeSnapshot.child("title").getValue().toString();
                            String difficulty = hikeSnapshot.child("difficulty").getValue().toString();
                            String city = hikeSnapshot.child("city").getValue().toString();
                            boolean mountainView = hikeSnapshot.child("mountainView").getValue().toString().equals("yes");
                            boolean summit = hikeSnapshot.child("summit").getValue().toString().toLowerCase().equals("yes");
                            boolean lakes = hikeSnapshot.child("lake").getValue().toString().equals("yes");
                            boolean waterfall = hikeSnapshot.child("waterfall").getValue().toString().equals("yes");
                            String wta = hikeSnapshot.child("wta").getValue().toString();
                            String pass = hikeSnapshot.child("pass").getValue().toString();
                            int id = Integer.valueOf(hikeSnapshot.child("id").getValue().toString());

                            // Add new hike
                            hikes.add(new Hike(latitude, longitude, distanceToHike, milesAway, length,
                                    title, difficulty, city, mountainView, summit, lakes, waterfall,wta,pass,id));
                            Log.d("title", title);


                        } catch (Exception e) {
                            Log.d("DatabaseTesting", e.toString());
                            Log.d("title", title);
                        }
                    }
                }
                firebaseCallback.onCallback(hikes);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseTesting", "Error reading hikes from database");
                Log.d("title", title);
            }
        });
    }

    private interface FirebaseCallback {
        void onCallback(ArrayList<Hike> hikes);
    }

    public int distFrom(double lat1, double lng1) {     //Distance from lat and lon to user

        double lng2 = userLocation.getLongitude();
        double lat2 = userLocation.getLatitude();

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return (int) dist / 1609;       //Convert to miles


    }

    private void getLocation(){     //Gets last known location for using

        PermissionManager permissionManager = new PermissionManager(getApplicationContext(),this);

        final MyLocationManager myLocationManager = new MyLocationManager(getApplicationContext(),this);
        myLocationManager.startLocationReceiving();
        Log.i("Start Splash", "on create");

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if(permissionManager.checkLocationPermission()) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                myLocationManager.positionReceived(location);
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                Log.i("Lat, lon", lat +" , " + lon);
                                userLocation = location;
                            }
                        }
                    });

        }
    }
}