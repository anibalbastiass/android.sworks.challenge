package com.anibalbastias.androidranduser.domain.usecase

import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.repository.DatabaseRepository
import com.anibalbastias.library.base.data.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class GetFavoriteUsersUseCase(private val databaseRepository: DatabaseRepository) :
    ResultUseCase<String, List<DomainUserResult>>(
        backgroundContext = Dispatchers.IO,
        foregroundContext = Dispatchers.Main
    ) {
    override suspend fun executeOnBackground(params: String): List<DomainUserResult>? {
        return databaseRepository.getFavoriteUsers()
    }
}