package com.anibalbastias.androidranduser.domain.repository

import com.anibalbastias.androidranduser.domain.model.DomainUserResult


interface RemoteRepository {

    suspend fun getRandomUsers(params: Map<String, String>): List<DomainUserResult>

}