package com.hike.wa.activities;

import android.app.ActivityOptions;
import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hike.wa.R;
import com.hike.wa.fragments.MyRecyclerViewAdapter;
import com.hike.wa.hikes.Hike;
import com.hike.wa.hikes.HikeDetails;
import com.hike.wa.hikes.HikeManager;
import com.hike.wa.weather.Weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Gets hikes near user
public class NearMeActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    private MyRecyclerViewAdapter adapter;
    Toolbar myToolbar;

    private HikeManager hikeManager = HikeManager.getInstance();
    private RecyclerView hikeListView;
    ArrayList<Hike> hikeList = new ArrayList<>();
    Weather weather = new Weather();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);
        hikeListView = findViewById(R.id.hike_list_recyclerview);
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(hikeListView.getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(hikeListView.getContext().getResources().getDrawable(R.drawable.border));
        hikeListView.addItemDecoration(verticalDecoration);
        ArrayList<String> hikeTitles = new ArrayList<>();
        ArrayList<String> difficultyList = new ArrayList<>();
        ArrayList<String> drivingDistance = new ArrayList<>();
        myToolbar = findViewById(R.id.my_toolbar);




        for (Hike hike : HikeManager.getInstance().getHikeList()) {
            if(!hikeList.contains(hike)) {      //Ensures no duplicates in the hikeList
                hikeList.add(hike);
            }
        }
        //Sorts the hikeList by shortest distance to the user
        Collections.sort(hikeList, new Comparator<Hike>() {
            @Override
            public int compare(Hike hike, Hike t1) {
                return  hike.getMilesAway() - t1.getMilesAway();
            }
        });

        //Adds the titles to the arrayList for display
        for(int i = 0; i < hikeList.size(); i++){
            if(!hikeTitles.contains(hikeList.get(i).getTitle())) {
                hikeTitles.add(hikeList.get(i).getTitle());
                difficultyList.add(hikeList.get(i).getDifficulty());
                drivingDistance.add(hikeList.get(i).getDistanceToHike());
            }
        }
        
        // set up the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.hike_list_recyclerview);

        hikeListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, hikeTitles,difficultyList,drivingDistance,hikeList);
        adapter.setClickListener(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(hikeListView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
        hikeListView.setAdapter(adapter);
        setActionBar(myToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Home");


        MobileAds.initialize(this,"ca-app-pub-8922333974706677~9766061920");

        AdView adView = findViewById(R.id.nearMeAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onItemClick(View view, int position) {

        Intent newIntent = new Intent(this, HikeDetails.class); //Sets up intent for clicking a hike
        Hike clickedHike = hikeList.get(position);      //Gets the hike that corresponds to the position in the list

        Bundle hikingBundle = new Bundle();     //Put all the information into a bundle for the intent
        weather.setWeatherData(clickedHike.getLatitude(), clickedHike.getLongitude());
        hikingBundle.putString("difficulty", clickedHike.getDifficulty());
        hikingBundle.putString("title", clickedHike.getTitle());
        hikingBundle.putString("distance", clickedHike.getDistanceToHike());
        hikingBundle.putString("city", clickedHike.getCity());
        hikingBundle.putDouble("latitude", clickedHike.getLatitude());
        hikingBundle.putDouble("longitude", clickedHike.getLongitude());
        hikingBundle.putDouble("length", clickedHike.getLength());
        hikingBundle.putDouble("temperature", weather.getTemperature());
        hikingBundle.putInt("visibility", weather.getVisibility());
        hikingBundle.putString("description", weather.getDescription());
        hikingBundle.putInt("milesAway", clickedHike.getMilesAway());
        hikingBundle.putString("toolbarTitle","Locations Near Me");
        hikingBundle.putString("pass", clickedHike.getPass());
        hikingBundle.putString("wta", clickedHike.getWta());
        hikingBundle.putInt("id", clickedHike.getId());

        newIntent.putExtra("HikeDetails", hikingBundle);    //Send the bundle with the intent
        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(this, R.anim.fast_fade_in, R.anim.fast_fade_out);
        startActivity(newIntent,activityOptions.toBundle());
    }

}
