package com.anibalbastias.androidranduser.presentation.model

import com.anibalbastias.androidranduser.R


data class UiWrapperUserResult(
    val items: List<UiUserResult>,
    val layoutId: Int? =  R.layout.view_cell_user_item
)