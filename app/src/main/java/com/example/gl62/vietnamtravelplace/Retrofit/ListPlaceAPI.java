package com.example.gl62.vietnamtravelplace.Retrofit;

import com.example.gl62.vietnamtravelplace.object.Place;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by GL62 on 3/30/2017.
 */

public class ListPlaceAPI {
    @SerializedName("data")
    @Expose
    private List<Place> places = null;

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> data) {
        this.places = data;
    }
}
