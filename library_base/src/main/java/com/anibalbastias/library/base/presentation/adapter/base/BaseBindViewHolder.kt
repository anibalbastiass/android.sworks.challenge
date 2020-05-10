package com.anibalbastias.library.base.presentation.adapter.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.library.base.BR

class BaseBindViewHolder<T>(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, clickHandler: BaseBindClickHandler<T>? = null) {

        binding.setVariable(BR.item, item)

        if (clickHandler != null)
            binding.setVariable(BR.clickHandler, clickHandler)

        binding.executePendingBindings()
    }
}