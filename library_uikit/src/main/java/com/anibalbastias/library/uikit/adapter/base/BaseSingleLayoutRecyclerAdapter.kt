package com.anibalbastias.library.uikit.adapter.base

/**
 *
 * Base recycler view adapter with
 * data binding that uses only one layout
 */
abstract class BaseSingleLayoutBindRecyclerAdapter<T>(private val layoutId : Int, private val list: List<T>, clickHandler: BaseBindClickHandler<T>? = null) : BaseBindRecyclerAdapter<T>(clickHandler) {

    override fun getLayoutIfForPosition(position: Int): Int {
        return layoutId
    }

    override fun getItemForPosition(position: Int): T {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}