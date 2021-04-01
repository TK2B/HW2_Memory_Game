package com.spaceintruders.h2_Memory_Game;
import java.io.Serializable;

public class Player implements Serializable {

    private int id;
    private String name;

    private int result;
    private double latitude;
    private double longitude;

    public Player(int id, String name, int result) {
        this.id = id;
        this.name = name;

        this.result = result;
    }

    public Player(int id, String name, int result, double latitude, double longitude) {
        this(id,name,result);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setResult(int result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public int getResult() {
        return result;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
