package com.example.gl62.vietnamtravelplace.Retrofit;

import com.example.gl62.vietnamtravelplace.object.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by GL62 on 3/30/2017.
 */

public class ListCategory {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Category> categories = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> data) {
        this.categories = data;
    }

}
