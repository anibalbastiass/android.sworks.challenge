package com.anibalbastias.androidranduser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.usecase.DeleteFavoriteUserUseCase
import com.anibalbastias.androidranduser.domain.usecase.GetFavoriteUserByIdUseCase
import com.anibalbastias.androidranduser.domain.usecase.GetFavoriteUsersUseCase
import com.anibalbastias.androidranduser.domain.usecase.SaveFavoriteUserUseCase
import com.anibalbastias.library.base.presentation.extensions.LiveResult

open class FavoriteUsersViewModel(
    private val getFavoriteUserByIdUseCase: GetFavoriteUserByIdUseCase,
    private val getFavoriteUsersUseCase: GetFavoriteUsersUseCase,
    private val saveFavoriteUserUseCase: SaveFavoriteUserUseCase,
    private val deleteFavoriteUserUseCase: DeleteFavoriteUserUseCase
) : ViewModel() {

    companion object {
        const val DEFAULT_EMPTY = ""
    }

    val getFavoriteUserByIdLiveResult = LiveResult<DomainUserResult>()
    val getFavoriteUsersLiveResult = LiveResult<List<DomainUserResult>>()
    val saveFavoriteUserLiveResult = LiveResult<Boolean>()
    val deleteFavoriteUserLiveResult = LiveResult<Boolean>()

    fun getFavoriteUserById(userId: String) {
        getFavoriteUserByIdUseCase.execute(
            liveData = getFavoriteUserByIdLiveResult,
            params = userId
        )
    }

    fun getFavoriteUsers() {
        getFavoriteUsersUseCase.execute(
            liveData = getFavoriteUsersLiveResult,
            params = DEFAULT_EMPTY
        )
    }

    fun saveFavoriteUser(user: DomainUserResult) {
        saveFavoriteUserUseCase.execute(liveData = saveFavoriteUserLiveResult, params = user)
    }

    fun deleteFavoriteUser(user: DomainUserResult) {
        deleteFavoriteUserUseCase.execute(liveData = deleteFavoriteUserLiveResult, params = user)
    }
}