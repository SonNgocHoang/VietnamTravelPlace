package com.example.gl62.vietnamtravelplace.object;

import android.widget.ArrayAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by GL62 on 3/29/2017.
 */

public class Category {

    private ArrayList<Place> lPlace = null;
    private boolean bCheck = true;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name_vi")
    @Expose
    private String nameVi;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameVi() {
        return nameVi;
    }

    public void setNameVi(String nameVi) {
        this.nameVi = nameVi;
    }

    public ArrayList<Place> getlPlace() {
        return lPlace;
    }

    public void setlPlace(ArrayList<Place> lPlace) {
        this.lPlace = lPlace;
    }

    public boolean isbCheck() {
        return bCheck;
    }

    public void setbCheck(boolean bCheck) {
        this.bCheck = bCheck;
    }
}