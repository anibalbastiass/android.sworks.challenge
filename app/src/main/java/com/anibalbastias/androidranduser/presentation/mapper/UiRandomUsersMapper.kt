package com.anibalbastias.androidranduser.presentation.mapper

import androidx.databinding.ObservableBoolean
import com.anibalbastias.androidranduser.R
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.presentation.model.UiUserResult
import com.anibalbastias.library.base.presentation.extensions.getFlagUrlByBase
import java.util.*

class UiRandomUsersMapper {

    companion object {
        const val MALE = "male"
        const val FEMALE = "female"
    }

    fun DomainUserResult.fromDomainToUi() = UiUserResult(
        userId = userId,
        fullName = fullName,
        gender = gender.capitalize(),
        imageGender = gender.getImageGender(),
        fullAddress = "$address\n$state\n$country",
        city = city,
        state = state,
        country = country,
        imageUrlCountry = getFlagUrlByBase(nationality),
        email = email,
        age = age,
        phone = phone,
        cell = cell,
        thumbImageUrl = thumbImageUrl,
        largeImageUrl = largeImageUrl,
        nationality = nationality,
        pageSize = pageSize,
        isFavorite = ObservableBoolean(isFavorite),
        dateFavoriteAdded = dateFavoriteAdded
    )

    fun UiUserResult.fromUiToDomain() = DomainUserResult(
        userId = userId,
        fullName = fullName,
        gender = gender.toLowerCase(Locale.getDefault()),
        address = fullAddress,
        city = city,
        state = state,
        country = country,
        email = email,
        age = age,
        phone = phone,
        cell = cell,
        thumbImageUrl = thumbImageUrl,
        largeImageUrl = largeImageUrl,
        nationality = nationality,
        pageSize = pageSize,
        isFavorite = isFavorite.get(),
        dateFavoriteAdded = dateFavoriteAdded
    )

    private fun String.getImageGender(): Int {
        return when (this) {
            MALE -> R.drawable.ic_male
            FEMALE -> R.drawable.ic_female
            else -> R.drawable.ic_lgbt
        }
    }
}