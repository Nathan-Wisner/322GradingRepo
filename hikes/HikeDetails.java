package com.hike.wa.hikes;


import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hike.wa.PermissionManager;
import com.hike.wa.R;
import com.hike.wa.adapters.MyListAdapter;
import com.hike.wa.weather.MyLocationManager;

import java.util.ArrayList;


//This class handles the intent data and automatically displays it on the click of a hike in the RecyclerView
public class HikeDetails extends AppCompatActivity {

    // Hikes contain various front end data
    Location userLocation = null;
    Toolbar myToolbar;
    String hikingProjectURL;
    TextView testTitle;
    TextView testCondition;
    Bundle hikeBundle;
    HikingProjectHelper hikingProjectHelper;

    TextView difficulty;
    TextView distance;
    TextView temperature;
    TextView eta;
    TextView moreInfo;
    TextView pass, incline, summary, visibility, description;
    ImageView imageView;
    Button MapsButton;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hiking_detail);         //Setup textViews and such

        setupViews();
        addToViews();
        getLocation();
        setupButton();
        getConditoions();
        setupCondition();
        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter();


    }

    public void setupViews(){

         difficulty = findViewById(R.id.difficultyText);
         distance = findViewById(R.id.lengthText);
         temperature = findViewById(R.id.temperatureText);
         eta = findViewById(R.id.etaText);
         moreInfo = findViewById(R.id.wtaText);
         pass = findViewById(R.id.passText);
        testTitle = findViewById(R.id.testTitle);
        testCondition = findViewById(R.id.testCondition);
         imageView = findViewById(R.id.imageView2);
         MapsButton = findViewById(R.id.button);
        myToolbar = findViewById(R.id.detailToolbar);
        incline = findViewById(R.id.inclineText);
        summary = findViewById(R.id.summaryText);
        visibility = findViewById(R.id.visibility);
        description = findViewById(R.id.description);
        setActionBar(myToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

    }

    public void addToViews(){

        hikeBundle = getIntent().getBundleExtra("HikeDetails");
        Glide.with(HikeDetails.this).load("https://firebasestorage.googleapis.com/v0/b/hikewa-d6eda.appspot.com/o/" + hikeBundle.getString("title").toLowerCase().replaceAll(" ", "") + ".png?alt=media&token=f8ba60d1-180f-4108-846a-8dba94adc4f5").into(imageView);
        difficulty.setText(hikeBundle.getString("difficulty"));
        distance.setText(Double.toString(hikeBundle.getDouble("length")));
        distance.append(" Miles");
        eta.setText(hikeBundle.getString("distance"));
        String wtaLink = hikeBundle.getString("wta");
        moreInfo.setText(Html.fromHtml("<a href=\"" + wtaLink + "\">" + "HERE" + "</a>"));
        moreInfo.setClickable(true);
        moreInfo.setLinkTextColor(Color.BLUE);
        moreInfo.setMovementMethod(LinkMovementMethod.getInstance());
        description.setText(hikeBundle.getString("description"));
        visibility.setText(hikeBundle.getInt("visibility") + "%");

        pass.setText(hikeBundle.getString("pass"));

        setActionBar(myToolbar);
        getActionBar().setTitle(hikeBundle.getString("title"));

        temperature.setText(Double.toString(hikeBundle.getDouble("temperature")));//Unused temperature data
        temperature.append(" F");

    }

    public void setupButton(){

        MapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mapsUri = "http://maps.google.com/maps?saddr="+ userLocation.getLatitude() + "," +userLocation.getLongitude()  + "&daddr=" + (hikeBundle.getDouble("latitude")) + "," +(hikeBundle.getDouble("longitude"));
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(mapsUri));
                mapsIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapsIntent);


            }
        });

    }

    public void setupCondition(){
        testCondition.setText(hikingProjectHelper.getCond());
        testTitle.setText(hikingProjectHelper.getHigh() + "ft");
        incline.setText(hikingProjectHelper.getAscent() + "ft");
        summary.setText(hikingProjectHelper.getSummary());
        Glide.with(this).load(hikingProjectHelper.getMedImage());
    }

    public void getConditoions(){
        String hikingProjectKey = "200488302-7418c56b11b7881556df46647e2a11ff";
        hikingProjectURL = "https://www.hikingproject.com/data/get-trails-by-id?ids=" + hikeBundle.get("id") +"&maxDistance=10&key=" + hikingProjectKey;
        hikingProjectHelper = new HikingProjectHelper(hikingProjectURL);
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
                                userLocation = location;
                            }
                        }
                    });

        }
    }


    public class ExpandableListAdapter extends ExpandableListActivity implements ExpandableListView.OnChildClickListener {

        ArrayList<String> groupItems = new ArrayList<>();
        ArrayList<String> childItems = new ArrayList<>();
        ArrayList<Object> child = new ArrayList<>();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ExpandableListView expandableListView = getExpandableListView();
            expandableListView.setDividerHeight(5);
            expandableListView.setGroupIndicator(null);
            expandableListView.setClickable(true);
            setGroupData();
            setChildData();


            MyListAdapter myListAdapter = new MyListAdapter(groupItems,child);
            myListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
            getExpandableListView().setAdapter(myListAdapter);
            expandableListView.setOnChildClickListener(this);

        }

        public void setGroupData(){
            groupItems.add("Hike Details");
            groupItems.add("Hike Conditions");
            groupItems.add("Weather");
        }

        public void setChildData(){
            childItems.add(hikeBundle.getString("difficulty"));
            child.add(childItems);

            childItems = new ArrayList<>();
            childItems.add(Double.toString(hikeBundle.getDouble("temperature")));
            child.add(childItems);

            childItems = new ArrayList<>();
            childItems.add(hikingProjectHelper.getCond());
            child.add(childItems);
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Toast.makeText(this,"Clicked On Child", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


}
