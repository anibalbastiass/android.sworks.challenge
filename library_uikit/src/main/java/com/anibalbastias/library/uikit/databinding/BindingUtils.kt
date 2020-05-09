package com.anibalbastias.library.uikit.databinding

import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.library.uikit.R
import com.anibalbastias.library.uikit.adapter.base.BaseBindClickHandler
import com.anibalbastias.library.uikit.adapter.base.SingleLayoutBindRecyclerAdapter
import com.anibalbastias.library.uikit.adapter.filter.FilterWordListener
import com.squareup.picasso.Picasso

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

fun RecyclerView.paginationForRecyclerScroll(itemPosition: ObservableInt) {
    addOnScrollListener(object :
        RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            itemPosition.set(
                (layoutManager as
                        GridLayoutManager).findFirstVisibleItemPosition()
            )
        }
    })
    scrollToPosition(itemPosition.get())
}