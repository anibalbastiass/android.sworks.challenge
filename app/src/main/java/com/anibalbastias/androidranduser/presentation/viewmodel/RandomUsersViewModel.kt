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

    companion object {
        const val PAGE_SIZE = "50"
        const val NAT = "US"
    }

    val usersLiveResult = LiveResult<List<DomainUserResult>>()

    fun getUsers(page: Int) {
        val params = DomainUserRequest(
            page = "$page",
            results = PAGE_SIZE,
            nat = NAT
        )
        getRandomUsersUseCase.execute(liveData = usersLiveResult, params = params)
    }

}