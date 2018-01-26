package com.danielecampogiani.demo.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.danielecampogiani.demo.R
import com.danielecampogiani.demo.ui.Stargazer

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView = itemView.findViewById(R.id.avatar)
    private val userName: TextView = itemView.findViewById(R.id.user_name)

    fun bind(model: Stargazer) {
        Glide.with(itemView.context).load(model.avatarUrl).into(image)
        userName.text = model.userName
    }
}