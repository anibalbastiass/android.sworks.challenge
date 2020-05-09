package com.anibalbastias.androidranduser.domain.usecase

import com.anibalbastias.androidranduser.domain.model.Constants.NAT
import com.anibalbastias.androidranduser.domain.model.Constants.PAGE
import com.anibalbastias.androidranduser.domain.model.Constants.RESULTS
import com.anibalbastias.androidranduser.domain.model.DomainUserRequest
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.repository.RemoteRepository
import com.anibalbastias.library.base.data.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class GetRandomUsersUseCase(private val remoteRepository: RemoteRepository) :
    ResultUseCase<DomainUserRequest, List<DomainUserResult>>(
        backgroundContext = Dispatchers.IO,
        foregroundContext = Dispatchers.Main
    ) {
    override suspend fun executeOnBackground(params: DomainUserRequest): List<DomainUserResult>? {
        val map = hashMapOf<String, String>()
        map[PAGE] = params.page
        map[RESULTS] = params.results
        map[NAT] = params.nat
        return remoteRepository.getRandomUsers(params = map)
    }
}