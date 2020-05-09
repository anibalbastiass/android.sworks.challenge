package com.anibalbastias.androidranduser.data.datasource.remote.api

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.API
import com.anibalbastias.androidranduser.data.datasource.remote.model.RemoteUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface RandUserApi {

    @GET(API)
    fun getRandomUsers(@QueryMap params: Map<String, String>): Call<RemoteUsers>

}