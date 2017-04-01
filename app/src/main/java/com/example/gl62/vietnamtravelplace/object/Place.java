package com.example.gl62.vietnamtravelplace.object;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GL62 on 3/29/2017.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("name_vi")
    @Expose
    private String nameVi;
    @SerializedName("short_description_vi")
    @Expose
    private String shortDescriptionVi;
    @SerializedName("description_vi")
    @Expose
    private String descriptionVi;
    @SerializedName("how_to_go_vi")
    @Expose
    private String howToGoVi;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("address_vi")
    @Expose
    private String addressVi;
    @SerializedName("images_count")
    @Expose
    private Integer imagesCount;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("cover")
    @Expose
    private List<Cover> cover = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNameVi() {
        return nameVi;
    }

    public void setNameVi(String nameVi) {
        this.nameVi = nameVi;
    }

    public String getShortDescriptionVi() {
        return shortDescriptionVi;
    }

    public void setShortDescriptionVi(String shortDescriptionVi) {
        this.shortDescriptionVi = shortDescriptionVi;
    }

    public String getDescriptionVi() {
        return descriptionVi;
    }

    public void setDescriptionVi(String descriptionVi) {
        this.descriptionVi = descriptionVi;
    }

    public String getHowToGoVi() {
        return howToGoVi;
    }

    public void setHowToGoVi(String howToGoVi) {
        this.howToGoVi = howToGoVi;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getAddressVi() {
        return addressVi;
    }

    public void setAddressVi(String addressVi) {
        this.addressVi = addressVi;
    }

    public Integer getImagesCount() {
        return imagesCount;
    }

    public void setImagesCount(Integer imagesCount) {
        this.imagesCount = imagesCount;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Cover> getCover() {
        return cover;
    }

    public void setCover(List<Cover> cover) {
        this.cover = cover;
    }

}