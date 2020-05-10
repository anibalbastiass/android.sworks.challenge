package com.anibalbastias.androidranduser.data.datasource.remote.model

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.PAGE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.RESULTS
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.SEED
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.VERSION
import com.google.gson.annotations.SerializedName

data class RemoteInfo(
    @field:SerializedName(SEED) val seed: String?,
    @field:SerializedName(PAGE) val page: Int?,
    @field:SerializedName(RESULTS) val results: Int?,
    @field:SerializedName(VERSION) val version: String?
)