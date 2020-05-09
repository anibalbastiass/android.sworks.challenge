package com.anibalbastias.androidranduser.data.datasource.remote.model.result

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.FIRST
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.LAST
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.TITLE
import com.google.gson.annotations.SerializedName

data class RemoteName(
    @field:SerializedName(LAST) val last: String?,
    @field:SerializedName(TITLE) val title: String?,
    @field:SerializedName(FIRST) val first: String?
)