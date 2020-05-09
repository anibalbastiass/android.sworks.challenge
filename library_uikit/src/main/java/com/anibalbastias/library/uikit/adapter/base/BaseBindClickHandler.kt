package com.anibalbastias.library.uikit.adapter.base

import android.view.View

/**
 * In case it's necessary to implement
 * a click handler in BaseBindRecyclerAdapter
 * this interface is needed to be implemented
 * in the class so the click action can successfully work
 */
interface BaseBindClickHandler<T> {

    fun onClickView(view: View, item : T)

}