package com.anibalbastias.androidranduser.data.datasource.remote.model.result

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.CITY
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.COORDINATES
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.COUNTRY
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.POSTCODE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.STATE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.STREET
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.TIMEZONE
import com.anibalbastias.androidranduser.data.datasource.remote.model.result.location.RemoteCoordinates
import com.anibalbastias.androidranduser.data.datasource.remote.model.result.location.RemoteStreet
import com.anibalbastias.androidranduser.data.datasource.remote.model.result.location.RemoteTimezone
import com.google.gson.annotations.SerializedName

data class RemoteLocation(
    @field:SerializedName(COUNTRY) val country: String?,
    @field:SerializedName(CITY) val city: String?,
    @field:SerializedName(STREET) val street: RemoteStreet?,
    @field:SerializedName(TIMEZONE) val remoteTimezone: RemoteTimezone?,
    @field:SerializedName(POSTCODE) val postcode: Int?,
    @field:SerializedName(COORDINATES) val coordinates: RemoteCoordinates?,
    @field:SerializedName(STATE) val state: String?
)