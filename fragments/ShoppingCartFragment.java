package com.hike.wa.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hike.wa.R;
import com.hike.wa.hikes.Hike;
import com.hike.wa.hikes.HikeDetails;
import com.hike.wa.hikes.HikeManager;
import com.hike.wa.weather.MyLocationManager;
import com.hike.wa.weather.Weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Mostly unused fragment
public class ShoppingCartFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    private MyRecyclerViewAdapter adapter;

    private HikeManager hikeManager = HikeManager.getInstance();
    private RecyclerView hikeListView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private OnFragmentInteractionListener mListener;
    ArrayList<Hike> hikeList = new ArrayList<>();
    private MyLocationManager myLocationManager = null;
    Weather weather = new Weather();


    // Required empty constructor
    public ShoppingCartFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        hikeListView = view.findViewById(R.id.hike_list_recyclerview);
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(hikeListView.getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(hikeListView.getContext().getResources().getDrawable(R.drawable.border));
        hikeListView.addItemDecoration(verticalDecoration);
        ArrayList<String> hikeTitles = new ArrayList<>();
        ArrayList<String> difficultyList = new ArrayList<>();
        ArrayList<String> drivingDistance = new ArrayList<>();
        myLocationManager = new MyLocationManager(getContext(),getActivity());


        for (Hike hike : HikeManager.getInstance().getHikeList()) {
            if(!hikeList.contains(hike)) {      //Ensures no duplicates in the hikeList
                hikeList.add(hike);
            }
        }

        Collections.sort(hikeList, new Comparator<Hike>() {
            @Override
            public int compare(Hike hike, Hike t1) {
                return  hike.getTitle().compareTo(t1.getTitle());
            }
        });


        for (Hike hike : hikeList) {
            if(!hikeTitles.contains(hike.getTitle())) {
                hikeTitles.add(hike.getTitle());    //If statement prevents duplicates from being added
                difficultyList.add(hike.getDifficulty());
                drivingDistance.add(hike.getDistanceToHike());
            }
        }

        // set up the RecyclerView
        hikeListView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), hikeTitles, difficultyList,drivingDistance, hikeList);
        adapter.setClickListener(this);
        hikeListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent newIntent = new Intent(getActivity(), HikeDetails.class);
        Hike clickedHike = hikeList.get(position);
        Bundle hikingBundle = new Bundle();

        Log.i("Distance", clickedHike.getDistanceToHike());

        weather.setWeatherData(clickedHike.getLatitude(),clickedHike.getLongitude());

        hikingBundle.putString("difficulty", clickedHike.getDifficulty());
        hikingBundle.putString("title", clickedHike.getTitle());
        hikingBundle.putString("distance", clickedHike.getDistanceToHike());
        hikingBundle.putString("city", clickedHike.getCity());
        hikingBundle.putDouble("latitude", clickedHike.getLatitude());
        hikingBundle.putDouble("longitude", clickedHike.getLongitude());
        hikingBundle.putDouble("length", clickedHike.getLength());
        hikingBundle.putInt("milesAway", clickedHike.getMilesAway());
        hikingBundle.putDouble("temperature", weather.getTemperature());
        hikingBundle.putInt("visibility", weather.getVisibility());
        hikingBundle.putString("description", weather.getDescription());
        hikingBundle.putString("wta", clickedHike.getWta());
        hikingBundle.putString("pass", clickedHike.getPass());
        hikingBundle.putInt("id", clickedHike.getId());


        newIntent.putExtra("HikeDetails", hikingBundle);
        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getContext(), R.anim.fast_fade_in, R.anim.fast_fade_out);
        getContext().startActivity(newIntent,activityOptions.toBundle());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MobileAds.initialize(getContext(),"ca-app-pub-8922333974706677~9766061920");

        AdView adView = view.findViewById(R.id.shoppingAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
