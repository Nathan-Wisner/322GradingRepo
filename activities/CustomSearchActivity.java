package com.hike.wa.activities;

import android.app.ActivityOptions;
import android.content.Intent;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hike.wa.R;
import com.hike.wa.fragments.MyRecyclerViewAdapter;
import com.hike.wa.hikes.Hike;
import com.hike.wa.weather.Weather;

import java.util.ArrayList;

//Used to search hikes
public class CustomSearchActivity extends AppCompatActivity {


    boolean mountainViews = false;
    boolean summits = false;
    boolean waterfalls = false;
    boolean lakes = false;
    CheckBox lakeCheck;
    CheckBox mountainViewCheck;
    CheckBox summitCheck;
    CheckBox waterfallCheck;
    String pass;
    Spinner selectionSpinner;
    Spinner difficultySpinner;
    Spinner passSpinner;
    Button searchButton;
    private MyRecyclerViewAdapter adapter;
    ArrayList<Hike> hikeList = new ArrayList<>();
    ArrayList<String> hikeTitles = new ArrayList<>();
    ArrayList<String> difficultyList = new ArrayList<>();
    ArrayList<String> drivingDistance = new ArrayList<>();
    Weather weather = new Weather();
    String sortingOption = "Driving Distance";
    RecyclerView customRecycler;
    LinearLayoutManager layoutManager;
    ConstraintLayout searchView;
    boolean isDown = false;
    Intent search;

    ActivityOptions activityOptions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_search);
        setIds();       //Sets up IDs for each layout item
        setCheckBoxes();    //Initializes the onCheck listener for the check boxes
        setLayout();
        setAds();       //Initializes the ads for the bottom of customSearch
    }

    public void setCheckBoxes(){

        lakeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                lakes = b;
            }
        });

        mountainViewCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mountainViews = b;
            }
        });

        waterfallCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                waterfalls = b;
            }
        });

        summitCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                summits = b;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle searchBundle = new Bundle();
                searchBundle.putBoolean("lake",lakes);
                searchBundle.putBoolean("waterfall", waterfalls);
                searchBundle.putBoolean("summit", summits);
                searchBundle.putBoolean("mountainView", mountainViews);
                searchBundle.putString("pass", pass);
                searchBundle.putString("sorting", sortingOption);

                search.putExtra("searchResults", searchBundle);
                startActivity(search,activityOptions.toBundle());
            }
        });

        selectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortingOption = selectionSpinner.getSelectedItem().toString();
                Log.i("Sorting option", sortingOption);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sortingOption = "Driving Distance";
            }
        });

        passSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pass = passSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pass = "Not Important";
            }
        });



    }

    public void setIds(){

        lakeCheck = findViewById(R.id.lakeCheck);
        mountainViewCheck = findViewById(R.id.mountainCheck);
        summitCheck = findViewById(R.id.summitCheck);
        waterfallCheck = findViewById(R.id.waterfallCheck);
        selectionSpinner = findViewById(R.id.sortingSpinner);
        searchButton = findViewById(R.id.searchButton);
        passSpinner = findViewById(R.id.passSpinner);

        search = new Intent(this,SearchResults.class);
        activityOptions = ActivityOptions.makeCustomAnimation(this,R.anim.slide_in_right,R.anim.slide_out_left);

    }

    public void setAds(){

        AdView adView = findViewById(R.id.customAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        MobileAds.initialize(this,"ca-app-pub-8922333974706677~9766061920");

    }

    public void setLayout(){
        String[] options = new String[]{"Driving Distance","Difficulty","Mileage"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item,options);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        selectionSpinner.setAdapter(arrayAdapter);

        String[] passOptions = new String[]{"Not Important","Northwest Forest Pass","Discover Pass","None"};
        ArrayAdapter<String> passArray = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item,passOptions);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        passSpinner.setAdapter(passArray);
    }

}
