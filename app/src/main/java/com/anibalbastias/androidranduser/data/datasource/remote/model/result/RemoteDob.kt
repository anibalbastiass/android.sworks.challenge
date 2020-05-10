package com.anibalbastias.androidranduser.data.datasource.remote.model.result

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.AGE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.DATE
import com.google.gson.annotations.SerializedName

data class RemoteDob(
	@field:SerializedName(DATE) val date: String?,
	@field:SerializedName(AGE) val age: Int?
)