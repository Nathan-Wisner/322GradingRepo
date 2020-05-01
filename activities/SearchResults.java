package com.hike.wa.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hike.wa.R;
import com.hike.wa.fragments.MyRecyclerViewAdapter;
import com.hike.wa.hikes.Hike;
import com.hike.wa.hikes.HikeDetails;
import com.hike.wa.hikes.HikeManager;
import com.hike.wa.weather.Weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Returns results searched for
public class SearchResults extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    boolean mountainViews = false;
    boolean summits = false;
    boolean waterfalls = false;
    boolean lakes = false;
    String passOption;
    private MyRecyclerViewAdapter adapter;
    ArrayList<Hike> hikeList = new ArrayList<>();
    ArrayList<String> hikeTitles = new ArrayList<>();
    ArrayList<String> difficultyList = new ArrayList<>();
    ArrayList<String> drivingDistance = new ArrayList<>();
    Weather weather = new Weather();
    String sorting;
    RecyclerView customRecycler;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        customRecycler = findViewById(R.id.search_recycler);
        addHikes();
        setLayoutSettings();
    }

    public void addHikes() {

        if(hikeList.size() != 0){
            hikeList.clear();
            hikeTitles.clear();
            difficultyList.clear();
            drivingDistance.clear();
        }

        int easy = 1;
        int mild = 2;
        int hard = 3;


        Bundle searchResults = getIntent().getBundleExtra("searchResults");
        waterfalls = searchResults.getBoolean("waterfall");
        Log.i("Waterfall", Boolean.toString(searchResults.getBoolean("waterfall")));
        lakes = searchResults.getBoolean("lake");
        mountainViews = searchResults.getBoolean("mountainView");
        summits = searchResults.getBoolean("summit");
        passOption = searchResults.getString("pass");
        sorting = searchResults.getString("sorting");

        Log.i("Sorting :", sorting);
        Log.i("Pass :", passOption);


        boolean isAddable = true;

        for (Hike hike : HikeManager.getInstance().getHikeList()) {
            if (!hikeList.contains(hike)) {      //Ensures no duplicates in the hikeList
                if (waterfalls == true && hike.isWaterfall() == false) {
                    isAddable = false;
                }

                if (lakes == true && hike.isLake() == false) {
                    isAddable = false;
                }

                if (summits == true && hike.isSummit() == false) {
                    isAddable = false;
                }

                if (mountainViews == true && hike.isMountainView() == false) {
                    isAddable = false;
                }

               if(passOption.equals("Not Important")){

                }

                else if(!passOption.equals(hike.getPass()) && !hike.getPass().equals("None")){
                    isAddable = false;
                    Log.i("Inable because pass", "true");
                }


                Log.i("IsAble: ", Boolean.toString(isAddable));
                if (isAddable) {
                    hikeList.add(hike);
                }

                isAddable = true;
            }
        }
        //Sorts the hikeList by shortest distance to the user
        if (sorting.equals("Driving Distance")) {
            Collections.sort(hikeList, new Comparator<Hike>() {
                @Override
                public int compare(Hike hike, Hike t1) {
                    return hike.getMilesAway() - t1.getMilesAway();
                }
            });
        } else if (sorting.equals("Mileage")) {
            Collections.sort(hikeList, new Comparator<Hike>() {
                @Override
                public int compare(Hike hike, Hike t1) {
                    return (int) hike.getLength() - (int) t1.getLength();
                }
            });
        } else if (sorting.equals("Difficulty")) {
            Collections.sort(hikeList, new Comparator<Hike>() {
                @Override
                public int compare(Hike hike, Hike t1) {
                    return hike.getDifficultyScore() - (t1.getDifficultyScore());
                }
            });
        }

        Log.i("Reached Results", "true");
        //Adds the titles to the arrayList for display
        for (int i = 0; i < hikeList.size(); i++) {
            if (!hikeTitles.contains(hikeList.get(i).getTitle())) {
                hikeTitles.add(hikeList.get(i).getTitle());
                difficultyList.add(hikeList.get(i).getDifficulty());
                drivingDistance.add(hikeList.get(i).getDistanceToHike());

            }
        }
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
            hikingBundle.putString("wta",clickedHike.getWta());
            hikingBundle.putString("pass", clickedHike.getPass());
            hikingBundle.putInt("id",clickedHike.getId());

            newIntent.putExtra("HikeDetails", hikingBundle);    //Send the bundle with the intent
            ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(this, R.anim.fast_fade_in, R.anim.fast_fade_out);
            startActivity(newIntent,activityOptions.toBundle());
        }

        public void setLayoutSettings(){

            DividerItemDecoration verticalDecoration = new DividerItemDecoration(customRecycler.getContext(), DividerItemDecoration.VERTICAL);
            verticalDecoration.setDrawable(customRecycler.getContext().getResources().getDrawable(R.drawable.border));
            customRecycler.addItemDecoration(verticalDecoration);
            customRecycler.addItemDecoration(verticalDecoration);

            layoutManager = new LinearLayoutManager(getApplicationContext());
            adapter = new MyRecyclerViewAdapter(this, hikeTitles,difficultyList,drivingDistance,hikeList);
            adapter.setClickListener(this);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(customRecycler.getContext(),layoutManager.getOrientation());

            customRecycler.addItemDecoration(dividerItemDecoration);
            customRecycler.setLayoutManager(new LinearLayoutManager(this));
            customRecycler.setAdapter(adapter);

        }

    }
