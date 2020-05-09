package com.anibalbastias.androidranduser.domain.repository

import com.anibalbastias.androidranduser.domain.model.DomainUserResult


interface DatabaseRepository {
    suspend fun getFavoriteUserById(userId: String): DomainUserResult
    suspend fun getFavoriteUsers(): List<DomainUserResult>
    suspend fun saveFavoriteUser(user: DomainUserResult)
    suspend fun deleteFavoriteUser(user: DomainUserResult)
}
