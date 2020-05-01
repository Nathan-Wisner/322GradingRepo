package com.hike.wa.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;
import com.hike.wa.PermissionManager;
import com.hike.wa.R;
import com.hike.wa.activities.CustomSearchActivity;
import com.hike.wa.activities.FAQ;
import com.hike.wa.activities.NearMeActivity;
import com.hike.wa.weather.MyLocationManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


// The main page of the application
public class MainFragment extends Fragment {

    private MyLocationManager locationManager;
    private PermissionManager permissionManager;
    private AdView adView;

    /* Buttons */
    private Button nearMeButton, customSearchButton;

    // Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param", ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1, mParam2;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }


    // Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager = new MyLocationManager(getContext(),getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        nearMeButton = view.findViewById(R.id.near_me_btn);
        customSearchButton = view.findViewById(R.id.custom_search_btn);

        // Creates button to go from main fragment to NearMeActivity
        nearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startNearMe = new Intent(getActivity(), NearMeActivity.class);
                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                getContext().startActivity(startNearMe,activityOptions.toBundle());

            }
        });

        // Creates buttons to go from main fragment to CustomSearchActivity
        customSearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent startCustomSearch = new Intent(getActivity(), CustomSearchActivity.class);
                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                getContext().startActivity(startCustomSearch,activityOptions.toBundle());
            }
        });

        Button faqButton = view.findViewById(R.id.faqButton);

        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent faqIntent = new Intent(getActivity(), FAQ.class);
                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(faqIntent, activityOptions.toBundle());
            }
        });



        MobileAds.initialize(getContext(),"ca-app-pub-8922333974706677~9766061920");

        adView = view.findViewById(R.id.mainAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        MyLocationManager myLocationManager = new MyLocationManager(getContext(),getActivity());
        myLocationManager.startLocationReceiving();
        Log.i("Created Main", " for location");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
