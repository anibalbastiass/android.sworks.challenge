package com.anibalbastias.androidranduser.data.datasource.remote.model.result

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.LARGE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.MEDIUM
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.THUMBNAIL
import com.google.gson.annotations.SerializedName

data class RemotePicture(
    @field:SerializedName(THUMBNAIL) val thumbnail: String?,
    @field:SerializedName(LARGE) val large: String?,
    @field:SerializedName(MEDIUM) val medium: String?
)