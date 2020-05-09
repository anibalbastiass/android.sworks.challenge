package com.anibalbastias.androidranduser.presentation.mapper

import com.anibalbastias.androidranduser.R
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.presentation.model.UiUserResult
import com.anibalbastias.library.base.presentation.extensions.getFlagUrlByBase

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
        pageSize = pageSize
    )

    fun UiUserResult.fromUiToDomain() = DomainUserResult(
        userId = userId,
        fullName = fullName,
        gender = gender,
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
        pageSize = pageSize
    )

    private fun String.getImageGender(): Int {
        return when (this) {
            MALE -> R.drawable.ic_male
            FEMALE -> R.drawable.ic_female
            else -> R.drawable.ic_lgbt
        }
    }
}