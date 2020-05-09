package com.anibalbastias.androidranduser.domain.mapper

import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.model.database.EntityUser

class BdRandomUsersMapper {

    fun EntityUser.fromDatabaseToDomain() = DomainUserResult(
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
        pageSize = pageSize,
        isFavorite = isFavorite
    )

    fun DomainUserResult.fromDomainToDatabase() = EntityUser(
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
        pageSize = pageSize,
        isFavorite = isFavorite
    )
}