package com.hike.wa.hikes;


public class Hike {

    private double latitude, longitude, length;
    private int milesAway;

    public int getDifficultyScore() {
        return difficultyScore;
    }

    private int difficultyScore;
    private int id;
    private String distanceToHike;
    private String title;
    private String difficulty;
    private String city;
    private String wta;
    private String pass;
    boolean mountainView, summit, lake, waterfall;

    public Hike(double latitude, double longitude, String distanceToHike, int milesAway, double length,
                String title, String difficulty, String city, boolean mountainView, boolean summit, boolean lake, boolean waterfall, String wta, String pass,int id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceToHike = distanceToHike;
        this.length = length;
        this.title = title;
        this.difficulty = difficulty;
        this.city = city;
        this.milesAway = milesAway;
        this.mountainView = mountainView;
        this.summit = summit;
        this.lake = lake;
        this.waterfall = waterfall;
        this.wta = wta;
        this.pass = pass;
        this.id = id;

        if(difficulty.equals("Easy")){this.difficultyScore = 1;}
        if(difficulty.equals("Mild")){this.difficultyScore = 2;}
        if(difficulty.equals("Hard")){this.difficultyScore = 3;}
    }

    /* Getters */

    public String getWta() { return wta; }

    public String getPass() { return pass; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public String getDistanceToHike() { return distanceToHike; }

    public double getLength() { return length; }

    public String getTitle() { return title; }

    public String getDifficulty() { return difficulty; }

    public String getCity() { return city; }

    public int getMilesAway(){ return milesAway; }

    public boolean isMountainView() { return mountainView; }

    public void setMountainView(boolean mountainView) { this.mountainView = mountainView;  }

    public boolean isSummit() { return summit; }

    public void setSummit(boolean summit) { this.summit = summit; }

    public boolean isLake() { return lake; }

    public void setLake(boolean lake) { this.lake = lake; }

    public boolean isWaterfall() { return waterfall; }

    public void setWaterfall(boolean waterfall) { this.waterfall = waterfall; }


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }


}
