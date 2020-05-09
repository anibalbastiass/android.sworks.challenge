package com.anibalbastias.androidranduser.presentation.viewmodel

import com.anibalbastias.androidranduser.domain.model.DomainUserRequest
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.usecase.*
import com.anibalbastias.androidranduser.presentation.state.SearchState
import com.anibalbastias.library.base.presentation.extensions.LiveResult
import com.anibalbastias.library.base.presentation.viewmodel.BaseViewModel

open class RandomUsersViewModel(
    private val getRandomUsersUseCase: GetRandomUsersUseCase,
    private val getFavoriteUserByIdUseCase: GetFavoriteUserByIdUseCase,
    private val getFavoriteUsersUseCase: GetFavoriteUsersUseCase,
    private val saveFavoriteUserUseCase: SaveFavoriteUserUseCase,
    private val deleteFavoriteUserUseCase: DeleteFavoriteUserUseCase
) : BaseViewModel<SearchState>(initState = SearchState()) {

    companion object {
        const val DEFAULT_EMPTY = ""
    }

    val usersLiveResult = LiveResult<List<DomainUserResult>>()
    val getFavoriteUserByIdLiveResult = LiveResult<DomainUserResult>()
    val getFavoriteUsersLiveResult = LiveResult<List<DomainUserResult>>()
    val saveFavoriteUserLiveResult = LiveResult<Boolean>()
    val deleteFavoriteUserLiveResult = LiveResult<Boolean>()

    fun getUsers(page: Int) {
        val params = DomainUserRequest(
            page = page
        )
        getRandomUsersUseCase.execute(liveData = usersLiveResult, params = params)
    }

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