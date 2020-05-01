package com.hike.wa.hikes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Helper functions for hikes
public class HikingProjectHelper {

    public int getAscent() {
        return ascent;
    }

    public void setAscent(int ascent) {
        this.ascent = ascent;
    }

    public int getDescent() {
        return descent;
    }

    public void setDescent(int descent) {
        this.descent = descent;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getMedImage() {
        return medImage;
    }

    public void setMedImage(String medImage) {
        this.medImage = medImage;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public String getConDetails() {
        return conDetails;
    }

    public void setConDetails(String conDetails) {
        this.conDetails = conDetails;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getHikingProjectURL() {
        return hikingProjectURL;
    }

    public void setHikingProjectURL(String hikingProjectURL) {
        this.hikingProjectURL = hikingProjectURL;
    }

    private int ascent, descent, high, low;
    private String smallImage, medImage, summary, difficulty, name, cond, conDetails, lastUpdated, hikingProjectURL;


    public HikingProjectHelper(String hikingProjectURL) {
        this.hikingProjectURL = hikingProjectURL;
        getHikingProjectDetails();
    }

        private void getHikingProjectDetails(){


            try {
                String newURL = hikingProjectURL; //Creates the full URL for the current Lat and Lon
                URL url = new URL(newURL);      //Sets the URL for calling

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();    //Creates a connection from the url and the internet
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));    //Reads in the text from the URL

                StringBuffer json = new StringBuffer(1024);
                String tmp;

                while ((tmp = reader.readLine()) != null)   //Reads in all the information into one string, line by line
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject hikingProjectJSON = new JSONObject(json.toString());      //Turns the string into a JSON object and sets it to weatherData
                Log.d("data", hikingProjectURL);

                JSONArray trails = hikingProjectJSON.getJSONArray("trails");
                JSONObject hike = trails.getJSONObject(0);
                name = hike.getString("name");
               // Log.i("Project Name: ", name);
                summary = hike.getString("summary");
              //  Log.i("Project summary: ", summary);
                difficulty = hike.getString("difficulty");
                //Log.i("Project difficulty: ", difficulty);
                smallImage = hike.getString("imgSmall");
                medImage = hike.getString("imgMedium");
                ascent = hike.getInt("ascent");
                //Log.i("Ascent: ", Integer.toString(ascent));
                descent = hike.getInt("descent");
               // Log.i("Descent: ", Integer.toString(descent));
                high = hike.getInt("high");
               // Log.i("high: ", Integer.toString(high));
                low  = hike.getInt("low");
               // Log.i("low: ", Integer.toString(low));
                cond = hike.getString("conditionStatus");
                //Log.i("cond: ", cond);


                if(cond !=  "Unknown") {
                    conDetails = hike.getString("conditionDetails");
                  //  Log.i("conDetails: ",conDetails);

                    lastUpdated = hike.getString("conditionDate");
                   // Log.i("updatedLAst", lastUpdated);
                }

                else{
                    conDetails = "Unknown";
                    lastUpdated = "Unknown";
                }








            } catch (Exception e) {
                e.printStackTrace();

            }
    }
}
