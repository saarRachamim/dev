package com.example.saar.locationalert.objects;

/**
 * Created by Saar on 30/07/2016.
 */
public class MetaData {
    int id;
    String cell;
    String message;
    String address;
    double latitude;
    double longitude;

    public void setId(int id){
        this.id = id;
    }

    public void setCell(String cell){
        this.cell = cell;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setLatitude(double latitude){ this.latitude = latitude; }

    public void setLongitude(double longitude){ this.longitude = longitude; }

    public int getId() {return this.id; }

    public String getCell() { return cell; }

    public String getMessage() { return message; }

    public String getAddress(){ return address; }

    public double getLatitude(){ return latitude; }

    public double getLongitude(){ return longitude; }
}
