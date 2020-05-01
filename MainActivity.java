package com.hike.wa;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.hike.wa.weather.Weather;
import com.hike.wa.fragments.MainFragment;
import com.hike.wa.fragments.ShoppingCartFragment;
import com.hike.wa.adapters.ViewPagerAdapter;


// Main front end
public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,
        ShoppingCartFragment.OnFragmentInteractionListener {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setup Activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Setup tabs */
        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new MainFragment(), "Home");
        pagerAdapter.addFragment(new ShoppingCartFragment(), "All Hikes");
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        Weather weather = new Weather();    //Creates Weather object

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
