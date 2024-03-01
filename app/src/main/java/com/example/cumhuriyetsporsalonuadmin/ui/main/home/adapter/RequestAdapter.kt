package com.example.cumhuriyetsporsalonuadmin.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemRequestBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.User


class RequestAdapter(
    val answerRequestOnClick: (user: User, isAccepted: Boolean) -> Unit
) : ListAdapter<User, RequestAdapter.RequestViewHolder>(UserDiffCallback) {
    class RequestViewHolder(val binding: ItemRequestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currentUser = getItem(position)
        holder.binding.tvName.text = currentUser.email
        holder.binding.btnAccept.setOnClickListener { answerRequestOnClick(currentUser, true) }
        holder.binding.btnDeny.setOnClickListener { answerRequestOnClick(currentUser, false) }
    }

    object UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }
    }

}