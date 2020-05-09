package com.anibalbastias.androidranduser.presentation.viewmodel

import com.anibalbastias.androidranduser.domain.model.DomainUserRequest
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.usecase.GetRandomUsersUseCase
import com.anibalbastias.androidranduser.presentation.state.SearchState
import com.anibalbastias.library.base.presentation.extensions.LiveResult
import com.anibalbastias.library.base.presentation.viewmodel.BaseViewModel

open class RandomUsersViewModel(
    private val getRandomUsersUseCase: GetRandomUsersUseCase
) : BaseViewModel<SearchState>(initState = SearchState()) {

    val usersLiveResult = LiveResult<List<DomainUserResult>>()

    fun getUsers(page: Int) {
        val params = DomainUserRequest(
            page = page
        )
        getRandomUsersUseCase.execute(liveData = usersLiveResult, params = params)
    }

}