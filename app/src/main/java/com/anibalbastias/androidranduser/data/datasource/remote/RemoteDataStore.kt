package com.anibalbastias.androidranduser.data.datasource.remote

import com.anibalbastias.androidranduser.data.datasource.remote.api.RandUserApi
import com.anibalbastias.androidranduser.domain.mapper.RandomUsersMapper
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.repository.RemoteRepository
import com.anibalbastias.library.base.presentation.extensions.await


open class RemoteDataStore(
    private val randUserApi: RandUserApi,
    private val mapper: RandomUsersMapper
) : RemoteRepository {

    override suspend fun getRandomUsers(params: Map<String, String>): List<DomainUserResult> {
        val response = randUserApi.getRandomUsers(params).await()
        with(mapper) {
            return (response?.results?.map { item ->
                item?.fromRemoteToDomain()
            } ?: listOf()) as List<DomainUserResult>
        }
    }

}