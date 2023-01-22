package com.example.mainverte.api

import com.example.mainverte.models.ListBalises
import com.example.mainverte.models.BalisesData
import com.example.mainverte.models.ListData
import com.example.mainverte.models.OneBaliseData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.*

interface ApiService {
    @GET("balises")
    fun getBalises(): Call<ListBalises>

    @GET("balises-data/{id}/last")
    fun getLastBaliseDataById(@Path("id") id: Long): Call<OneBaliseData>

    @GET("balises-data/{id}")
    fun getBaliseDataById(@Path("id") id: Long): Call<ListData>

    @POST("balises-data")
    fun createBaliseData(@Body balisesData: BalisesData): Call<BalisesData>

}