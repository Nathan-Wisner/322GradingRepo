package com.hike.wa.adapters;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// An adapter used for direction time
public class RouteFinding {

    public static String KEY = "AIzaSyArhvbJr_JFTK4f3J0clYbU_pgbJUSPYqc";


    JSONObject routeData;
    String TravelTime;

    public int getTravelValue() {
        return TravelValue;
    }

    public void setTravelValue(int travelValue) {
        TravelValue = travelValue;
    }

    int TravelValue;

    public String getRouteData(double userLat, double userLon, double hikeLat, double hikeLon) {
        try {

            String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + userLat + "," + userLon +
                    "&destinations=" + hikeLat + "," + hikeLon +"&key=";

            String newURL = URL + KEY; //Creates the full URL for the current Lat and Lon
            java.net.URL url = new URL(newURL);      //Sets the URL for calling

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    //Creates a connection from the url and the internet

           // Log.i("URL", newURL);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));    //Reads in the text from the URL

            //Log.i("Reader",reader.toString());
            StringBuffer json = new StringBuffer(1024);
            String tmp;

            while ((tmp = reader.readLine()) != null)   //Reads in all the information into one string, line by line
                json.append(tmp).append("\n");
            reader.close();

            routeData = new JSONObject(json.toString());      //Turns the string into a JSON object and sets it to routeData
            JSONArray pageData = routeData.getJSONArray("rows");
            JSONObject rowArray = pageData.getJSONObject(0);
            JSONArray elementArray = rowArray.getJSONArray("elements");
            JSONObject element = elementArray.getJSONObject(0);
            JSONObject duration = element.getJSONObject("duration");
            TravelTime = duration.getString("text");
            TravelValue = duration.getInt("value");

            //Log.i("rows", duration.toString());
            //Log.d("data", routeData.toString());
            //Log.i("Duration", TravelTime);

            return TravelTime;

            // This value will be 404 if the request was not successful
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error In Calculating";

    }

}
