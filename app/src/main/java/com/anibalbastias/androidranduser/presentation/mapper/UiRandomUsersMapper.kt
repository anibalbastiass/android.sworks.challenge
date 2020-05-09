package com.anibalbastias.androidranduser.presentation.mapper

import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.presentation.model.UiUserResult

class UiRandomUsersMapper {

    fun DomainUserResult.fromDomainToUi() = UiUserResult(
        userId = userId,
        fullName = fullName,
        gender = gender,
        address = address,
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

    fun UiUserResult.fromUiToDomain() = DomainUserResult(
        userId = userId,
        fullName = fullName,
        gender = gender,
        address = address,
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
}