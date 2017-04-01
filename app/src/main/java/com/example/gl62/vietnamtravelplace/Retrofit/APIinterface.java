package com.example.gl62.vietnamtravelplace.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by GL62 on 3/30/2017.
 */

public interface APIinterface {


    @GET("/api/4.2/place")
    Call<ListPlaceAPI> loadListP();

    @GET("/api/4.2/category")
    Call<ListCategoryAPI>loadListC();


}

