package com.anibalbastias.androidranduser.domain.repository

import com.anibalbastias.androidranduser.domain.model.DomainUserRequest
import com.anibalbastias.androidranduser.domain.model.DomainUserResult


interface RemoteRepository {

    suspend fun getRandomUsers(params: DomainUserRequest): List<DomainUserResult>

}