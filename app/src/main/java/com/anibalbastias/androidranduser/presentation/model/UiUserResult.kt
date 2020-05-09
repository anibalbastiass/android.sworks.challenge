package com.anibalbastias.androidranduser.presentation.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class UiUserResult(
    val userId: String,
    val fullName: String,
    val gender: String,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val email: String,
    val age: String,
    val phone: String,
    val cell: String,
    val thumbImageUrl: String,
    val largeImageUrl: String,
    val nationality: String
) : Parcelable