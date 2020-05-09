package com.anibalbastias.library.base.presentation.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.anibalbastias.library.base.presentation.adapter.customBase.BaseAdapter
import java.util.concurrent.atomic.AtomicBoolean

open class PaginationViewModel<T> : ViewModel() {

    var pageSize: Int = 0
    var itemPosition = ObservableInt(0)
    var lastPosition = ObservableInt(0)
    var offset = ObservableInt(0)

    var isLoading = ObservableBoolean(false)
    var isError = ObservableBoolean(false)
    var isEmpty = ObservableBoolean(false)

    var isLoadingMorePages = AtomicBoolean(false)
    var pagedList: ArrayList<T>? = arrayListOf()

    private fun addMoreItemsInProgressData(newList: ArrayList<T>) {
        val isFirstPageLoad = offset.get() == 0
        offset.set(newList.size)

        if (!isFirstPageLoad) {
            //Pagination Loaded
            val oldList = pagedList

            newList.let {
                if (!it.isNullOrEmpty()) {
                    val addSuccessful = oldList?.addAll(((it as? List<T>)!!))
                    if (addSuccessful == true) {
                        pagedList = oldList
                    }
                }
            }
        } else {
            //FirstPage Loaded
            pagedList = newList
        }
    }

    fun setPagination(
        adapter: BaseAdapter<T>,
        items: ArrayList<T>,
        bodyHasItems: ((list: ArrayList<T>?) -> Unit)?,
        bodyNoItems: ((list: ArrayList<T>?) -> Unit)?
    ) {

        isLoading.set(false)
        isError.set(false)

        if (isLoadingMorePages.compareAndSet(true, false)) {
            isEmpty.set(false)

            adapter.removeLoadingFooter()
            addMoreItemsInProgressData(items)

            // Update Next Identifier for pagination
            if (items.isNotEmpty()) {
                offset.set(items.size)
                lastPosition.set(items.size)
            } else {
                offset.set(0)
            }

        } else {
            if (items.isNotEmpty()) {
                // Set data
                pagedList = items
                isEmpty.set(false)
                bodyHasItems?.invoke(pagedList)
            } else {
                isEmpty.set(true)
                pagedList?.clear()
                bodyNoItems?.invoke(pagedList)
            }
        }
    }
}