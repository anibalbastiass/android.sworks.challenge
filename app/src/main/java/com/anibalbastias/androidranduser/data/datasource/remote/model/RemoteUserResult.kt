package com.anibalbastias.androidranduser.data.datasource.remote.model

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.CELL
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.DOB
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.EMAIL
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.GENDER
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.ID
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.LOCATION
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.LOGIN
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.NAME
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.NAT
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.PHONE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.PICTURE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.REGISTERED
import com.anibalbastias.androidranduser.data.datasource.remote.model.result.*
import com.google.gson.annotations.SerializedName

data class RemoteUserResult(
    @field:SerializedName(NAT) val nat: String?,
    @field:SerializedName(GENDER) val gender: String?,
    @field:SerializedName(PHONE) val phone: String?,
    @field:SerializedName(DOB) val dob: RemoteDob?,
    @field:SerializedName(NAME) val name: RemoteName?,
    @field:SerializedName(REGISTERED) val registered: RemoteRegistered?,
    @field:SerializedName(LOCATION) val location: RemoteLocation?,
    @field:SerializedName(ID) val id: RemoteId?,
    @field:SerializedName(LOGIN) val login: RemoteLogin?,
    @field:SerializedName(CELL) val cell: String?,
    @field:SerializedName(EMAIL) val email: String?,
    @field:SerializedName(PICTURE) val picture: RemotePicture?
)