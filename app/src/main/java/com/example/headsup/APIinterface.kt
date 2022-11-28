package com.example.headsup

import retrofit2.Call
import retrofit2.http.*

interface APIinterface {
    @GET("celebrities/")
    fun getCelebrites(): Call<Celebrity>

    @GET("celebrities/{id}")
    fun getCelebrity(@Path("id") id : Int): Call<CelebrityItem>

    //Add celebrity
    @POST("celebrities/")
    fun AddUsers(@Body CelebrityInfo: CelebrityItem): Call<CelebrityItem>

    //update the celebrity information
    @PUT("celebrities/{id}")
    fun UpdateItems(@Path("id") id:Int, @Body CelebrityInfo: CelebrityItem): Call<CelebrityItem>

    //Delete the celebrity information
    @DELETE("celebrities/{id}")
    fun DeleteItem(@Path("id") id:Int): Call<CelebrityItem>
}