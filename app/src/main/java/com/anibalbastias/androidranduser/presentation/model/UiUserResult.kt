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
    val nationality: String,
    val pageSize: Int
) : Parcelable {

    companion object {
        private const val DEFAULT_EMPTY = ""
        private const val DEFAULT_SIZE = 0

        fun create() = UiUserResult(
            userId = DEFAULT_EMPTY,
            fullName = DEFAULT_EMPTY,
            gender = DEFAULT_EMPTY,
            address = DEFAULT_EMPTY,
            city = DEFAULT_EMPTY,
            state = DEFAULT_EMPTY,
            country = DEFAULT_EMPTY,
            email = DEFAULT_EMPTY,
            age = DEFAULT_EMPTY,
            phone = DEFAULT_EMPTY,
            cell = DEFAULT_EMPTY,
            thumbImageUrl = DEFAULT_EMPTY,
            largeImageUrl = DEFAULT_EMPTY,
            nationality = DEFAULT_EMPTY,
            pageSize = DEFAULT_SIZE
        )
    }
}