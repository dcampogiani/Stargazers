package com.danielecampogiani.demo.ui.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.danielecampogiani.demo.R
import com.danielecampogiani.demo.ui.Stargazer

class StargazerAdapter(private var data: MutableList<Stargazer> = mutableListOf()) : RecyclerView.Adapter<ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_stargazer, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemId(position: Int) = data[position].hashCode().toLong()

    fun addItems(items: List<Stargazer>) {

        val diffCallback = StargazerDiffCallback(data, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        data.clear()
        data.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}