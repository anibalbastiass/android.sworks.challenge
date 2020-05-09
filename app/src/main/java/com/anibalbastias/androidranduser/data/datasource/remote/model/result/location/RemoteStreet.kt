package com.anibalbastias.androidranduser.data.datasource.remote.model.result.location

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.NAME
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.NUMBER
import com.google.gson.annotations.SerializedName

data class RemoteStreet(
    @field:SerializedName(NUMBER) val number: Int?,
    @field:SerializedName(NAME) val name: String?
)