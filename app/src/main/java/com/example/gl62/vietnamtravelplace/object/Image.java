package com.example.gl62.vietnamtravelplace.object;

/**
 * Created by GL62 on 3/29/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("o_height")
    @Expose
    private Integer oHeight;
    @SerializedName("o_width")
    @Expose
    private Integer oWidth;
    @SerializedName("o_size")
    @Expose
    private String oSize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOHeight() {
        return oHeight;
    }

    public void setOHeight(Integer oHeight) {
        this.oHeight = oHeight;
    }

    public Integer getOWidth() {
        return oWidth;
    }

    public void setOWidth(Integer oWidth) {
        this.oWidth = oWidth;
    }

    public String getOSize() {
        return oSize;
    }

    public void setOSize(String oSize) {
        this.oSize = oSize;
    }

}
