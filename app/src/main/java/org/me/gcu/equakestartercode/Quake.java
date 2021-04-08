package org.me.gcu.equakestartercode;

import android.util.Log;

import java.io.Serializable;

public class Quake implements Comparable<Quake>, Serializable {


    private String title;
    private String description;
    private String pubDate;
    private String category;
    private String geolat;
    private String geolong;
    private String location;
    private String depth;
    private String magnitude;
    private String link;

    public Quake(String title, String description, String link, String pubDate, String category, String geolat, String geolong) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.category = category;
        this.geolat = geolat;
        this.geolong = geolong;
    }

    public Quake() {

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getCategory() {
        return category;
    }

    public String getGeolat() {
        return geolat;
    }

    public String getGeolong() {
        return geolong;
    }

    public String getLink(){
        return link;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public String getDepth() {
        return depth;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
        String[] result = description.split(";");
        for(int i =0; i<result.length;i++) {
            if(i == 1){
                setLocation(result[i]);
            } else if(i == 3){
                setDepth(result[i]);
            } else if(i == 4) {
                setMagnitude(result[i]);
            }
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setGeolat(String geolat) {
        this.geolat = geolat;
    }

    public void setGeolong(String geolong) {
        this.geolong = geolong;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public int compareTo(Quake o) {
        String[] result1 = o.getMagnitude().split(":");
        String magnitude = result1[1];
        String[] result2 = this.getMagnitude().split(":");
        String magnitude2 = result2[1];
        return magnitude2.compareTo(magnitude);
    }
}
