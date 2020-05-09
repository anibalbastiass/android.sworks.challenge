package com.anibalbastias.androidranduser.data.datasource.remote

import com.anibalbastias.androidranduser.data.datasource.remote.Constants.PAGE_SIZE
import com.anibalbastias.androidranduser.data.datasource.remote.Constants.URL_FORMAT
import com.anibalbastias.androidranduser.data.datasource.remote.api.RandUserApi
import com.anibalbastias.androidranduser.domain.mapper.RandomUsersMapper
import com.anibalbastias.androidranduser.domain.model.DomainUserRequest
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.repository.RemoteRepository
import com.anibalbastias.library.base.presentation.extensions.await


open class RemoteDataStore(
    private val randUserApi: RandUserApi,
    private val mapper: RandomUsersMapper
) : RemoteRepository {

    override suspend fun getRandomUsers(params: DomainUserRequest): List<DomainUserResult> {
        val query = String.format(URL_FORMAT, params.page, PAGE_SIZE)
        val response = randUserApi.getUsersByPage(query).await()

        with(mapper) {
            return (response?.results?.map { item ->
                item?.fromRemoteToDomain(PAGE_SIZE)
            } ?: listOf()) as List<DomainUserResult>
        }
    }

}