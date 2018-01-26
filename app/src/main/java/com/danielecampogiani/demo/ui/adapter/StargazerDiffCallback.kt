package com.danielecampogiani.demo.ui.adapter

import android.support.v7.util.DiffUtil
import com.danielecampogiani.demo.ui.Stargazer

class StargazerDiffCallback(
    private val oldList: List<Stargazer>,
    private val newList: List<Stargazer>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].userName == newList[newItemPosition].userName

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}