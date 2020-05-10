package com.anibalbastias.androidranduser.data.datasource.remote.model.result.location

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.LATITUDE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.LONGITUDE
import com.google.gson.annotations.SerializedName

data class RemoteCoordinates(
    @field:SerializedName(LATITUDE) val latitude: String?,
    @field:SerializedName(LONGITUDE) val longitude: String?
)