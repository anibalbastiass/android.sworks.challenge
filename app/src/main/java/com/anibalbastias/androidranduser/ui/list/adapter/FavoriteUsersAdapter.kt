package com.anibalbastias.androidranduser.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.androidranduser.BR
import com.anibalbastias.androidranduser.R
import com.anibalbastias.androidranduser.presentation.model.UiUserResult
import com.anibalbastias.androidranduser.ui.list.interfaces.FavoriteUserItemListener
import com.anibalbastias.library.base.presentation.adapter.base.BaseBindClickHandler
import com.anibalbastias.library.base.presentation.adapter.base.BaseBindViewHolder
import com.anibalbastias.library.base.presentation.adapter.customBase.BaseAdapter


class FavoriteUsersAdapter : BaseAdapter<UiUserResult>() {

    override var items: MutableList<UiUserResult?> = arrayListOf()
    override var clickHandler: BaseBindClickHandler<UiUserResult>? = null
    var favoriteClickHandler: FavoriteUserItemListener? = null

    //region Unused methods
    override fun createHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? = null
    override fun bindHeaderViewHolder(viewHolder: RecyclerView.ViewHolder) {}
    override fun bindFooterViewHolder(viewHolder: RecyclerView.ViewHolder) {}
    override fun createLoadingViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? = null
    //endregion

    override fun createItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.view_cell_favorite_user_item, parent, false
        )
        return BaseBindViewHolder<UiUserResult>(binding)
    }

    override fun bindItemViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as BaseBindViewHolder<UiUserResult>
        items[position]?.let {
            holder.bind(it, clickHandler)
            holder.binding.setVariable(BR.favoriteClickHandler, favoriteClickHandler)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isLastPosition(position) && isLoadingAdded -> LOADING_TYPE
            else -> ITEM_TYPE
        }
    }

    override fun addLoadingFooter() {
        isLoadingAdded = true
        add(UiUserResult.create())
    }
}