package com.hike.wa.weather;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// JSON fetcher
public class WeatherFetcher {     //The real worker class that gets the weather JSON

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    double Latitude, Longitude;

    public JSONObject getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(JSONObject weatherData) {
        this.weatherData = weatherData;
    }

    JSONObject weatherData;

    boolean isDone = false;

    WeatherFetcher(double lon, double lat){     //Constructor that requires a non-null lat and lon for getting weather data
        this.Latitude = lat;
        this.Longitude = lon;
    }



        public JSONObject getWeather(){    //Gets weather JSON

        isDone = false;     //Flag for checking if the weatherData JSON has been retrieved


        String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=" + getLatitude() + "&lon=" + getLongitude() + "&units=imperial&appid=";
        String OPEN_WEATHER_MAP_API = "75d42cf579d66be3fda8ee90c9cba6e3";   //Unique API code that allows for weather fetching and the URL gets the correct URL for the lat and lon
                                                                            //Each URL is different and needs to be implemented for every call
        try {
            String newURL = OPEN_WEATHER_MAP_URL + OPEN_WEATHER_MAP_API; //Creates the full URL for the current Lat and Lon
            URL url = new URL(newURL);      //Sets the URL for calling

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    //Creates a connection from the url and the internet

            Log.i("URL", newURL);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));    //Reads in the text from the URL

            StringBuffer json = new StringBuffer(1024);
            String tmp;

            while ((tmp = reader.readLine()) != null)   //Reads in all the information into one string, line by line
                json.append(tmp).append("\n");
            reader.close();

            weatherData = new JSONObject(json.toString());      //Turns the string into a JSON object and sets it to weatherData
            Log.d("data", weatherData.toString());

            // This value will be 404 if the request was not successful
            if (weatherData.getInt("cod") != 200) {

            } else {    //Set flag as finished
                return weatherData;
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
