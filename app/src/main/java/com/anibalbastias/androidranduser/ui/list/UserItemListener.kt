package com.anibalbastias.androidranduser.ui.list

import com.anibalbastias.androidranduser.presentation.model.UiUserResult

interface UserItemListener {
    fun onFavoriteClick(item: UiUserResult)
}