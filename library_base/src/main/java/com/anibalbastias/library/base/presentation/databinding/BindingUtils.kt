package com.anibalbastias.library.base.presentation.databinding

import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.library.base.R
import com.anibalbastias.library.base.presentation.adapter.base.BaseBindClickHandler
import com.anibalbastias.library.base.presentation.adapter.base.SingleLayoutBindRecyclerAdapter
import com.anibalbastias.library.base.presentation.adapter.filter.FilterWordListener
import com.squareup.picasso.Picasso
import java.util.concurrent.atomic.AtomicBoolean

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    imageUrl?.let { image ->
        Picasso.get().load(image).into(this)
    }
}

@BindingAdapter(
    value = ["loadAdapterData", "loadAdapterLayout", "loadAdapterListener", "filter"],
    requireAll = false
)
fun <T> RecyclerView.loadAdapterData(
    list: MutableList<T>?,
    layout: Int?,
    callback: BaseBindClickHandler<T>? = null,
    filter: FilterWordListener<T>? = null
) {
    context?.run {
        layout?.let { layoutId ->
            layoutManager = GridLayoutManager(context, 3)
            val singleAdapter = SingleLayoutBindRecyclerAdapter(layoutId, list, callback, filter)
            adapter = singleAdapter
            runLayoutAnimation()
        }
    }
}

fun RecyclerView.runLayoutAnimation() {
    layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
    adapter?.notifyDataSetChanged()
    scheduleLayoutAnimation()
}

fun RecyclerView.paginationForRecyclerScroll(
    itemPosition: ObservableInt,
    lastPosition: ObservableInt,
    offset: ObservableInt,
    listSize: Int,
    pageSize: Int,
    isLoadingMorePages: AtomicBoolean,
    bodyLoadMore: (() -> Unit)?
) {
    addOnScrollListener(object :
        RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            // Set Item position
            itemPosition.set((layoutManager as GridLayoutManager).findFirstVisibleItemPosition())

            // Refresh Offset and add pagination
            val visibleItemCount = layoutManager?.childCount
            val totalItemCount = layoutManager?.itemCount
            val firstVisibleItemPosition =
                (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

            if (!isLoadingMorePages.get()) {
                if (visibleItemCount!! + firstVisibleItemPosition >= totalItemCount!! && firstVisibleItemPosition >= 0) {
                    lastPosition.set(listSize)

                    if (lastPosition.get() >= pageSize) {
                        isLoadingMorePages.set(true)
                        offset.set(offset.get() + pageSize)
                        bodyLoadMore?.invoke()
                    }
                }
            }
        }
    })
    scrollToPosition(itemPosition.get())
}