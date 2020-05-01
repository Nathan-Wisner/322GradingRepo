package com.hike.wa.weather;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;

// Gets weather data wrapper
public class Weather {   //Weather is a wrapper class, create a weather object called setWeatherData with your coords as parameters
                        //This then will allow weather.getTemperature() to get the temperature of those coords

    int ID, humidity, visibility, rainVolume;
    double lat;
    double lon;
    double temperature;
    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    double windSpeed;
    JSONObject weatherData;

    public int getID() {
        return ID;
    }       //Setters and getters

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getRainVolume() {
        return rainVolume;
    }

    public void setRainVolume(int rainVolume) {
        this.rainVolume = rainVolume;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getMain() {
        return Main;
    }

    public void setMain(String main) {
        Main = main;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String Main, Description, Icon, Name, result;

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public void setWeatherData(double newLat, double newLong) {     //When given a Lat and long, the function will set the class variable
                                                                    //of weatherData to that of the relevant data from those coords
        setLat(newLat);
        setLon(newLong);
        getWeatherData();

    }



        public void getWeatherData(){   //Takes class Latitude and Longitude and gets a weatherData JSON that is split
                                            //and distributed to the local class variables

            setTemperature(0);
            if(isOnline()) {

                WeatherFetcher newWeatherFetcher = new WeatherFetcher(getLon(), getLat());   //Sets the Lat and Longitude for the JSON code
                weatherData = newWeatherFetcher.getWeather();       //Calls the weatherFetchers doInBackground to retrieve the appropriate JSON object

                try {
                    JSONArray weather = weatherData.getJSONArray("weather");    //This block splits the weather JSON into smaller JSON objects
                    JSONObject main = weatherData.getJSONObject("main");              //This is required as they cannot be used unless they only contain strings and not more JSONs
                    JSONObject wind = weatherData.getJSONObject("wind");
                    JSONObject clouds = weatherData.getJSONObject("clouds");
                    JSONObject JSONWeather = weather.getJSONObject(0);

                    setID(JSONWeather.getInt("id"));                            //For each JSON object or array, finds relevant data and sets the class values
                    setMain(JSONWeather.getString("main"));
                    setDescription(JSONWeather.getString("description"));
                    setIcon(JSONWeather.getString("icon"));
                    setTemperature(main.getDouble("temp"));
                    setHumidity(main.getInt("humidity"));
                    setWindSpeed(wind.getDouble("speed"));
                    setVisibility(clouds.getInt("all"));
                    setName(weatherData.getString("name"));

                    Log.i("ID", Integer.toString(getID()));             //Logs all the class data for checking to ensure accuracy
                    Log.i("main", getMain());
                    Log.i("Description", getDescription());
                    Log.i("Icon", getIcon());
                    Log.i("Temperature", Double.toString(getTemperature()));
                    Log.i("Humidity", Integer.toString(getHumidity()));
                    Log.i("Wind Speed", Double.toString(getWindSpeed()));
                    Log.i("Visibility", Integer.toString(getVisibility()));

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }
    }

