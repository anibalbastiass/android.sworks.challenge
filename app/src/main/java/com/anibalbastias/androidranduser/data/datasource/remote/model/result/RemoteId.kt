package com.anibalbastias.androidranduser.data.datasource.remote.model.result

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.NAME
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.VALUE
import com.google.gson.annotations.SerializedName

data class RemoteId(
	@field:SerializedName(NAME) val name: String?,
	@field:SerializedName(VALUE) val value: String?
)