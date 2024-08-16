package com.example.cumhuriyetsporsalonuadmin.ui.main.home.verify_request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cumhuriyetsporsalonuadmin.databinding.ItemVerifyRequestBinding
import com.example.cumhuriyetsporsalonuadmin.domain.model.User


class VerifyRequestAdapter(
    val answerRequestOnClick: (user: User, isAccepted: Boolean) -> Unit
) : ListAdapter<User, VerifyRequestAdapter.RequestViewHolder>(UserDiffCallback) {
    class RequestViewHolder(val binding: ItemVerifyRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding =
            ItemVerifyRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currentUser = getItem(position)
        "${currentUser.name} ${currentUser.surname}".also { holder.binding.tvName.text = it }
        holder.binding.tvEmail.text = currentUser.email
        holder.binding.btnAccept.setOnClickListener {
            answerRequestOnClick(currentUser, true)
        }
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