package com.anibalbastias.androidranduser.ui.list

import android.view.View
import com.anibalbastias.androidranduser.presentation.model.UiUserResult

interface FavoriteUserItemListener {
    fun onUserFavoriteClick(view: View, item: UiUserResult)
}