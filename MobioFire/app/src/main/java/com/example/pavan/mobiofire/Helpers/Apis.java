package com.example.pavan.mobiofire.Helpers;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;


public interface Apis {




    @GET("/media.json?print=pretty")
    Call<JsonElement> getdesc();

}
