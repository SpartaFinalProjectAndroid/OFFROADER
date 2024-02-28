package com.mit.offroader.ui.fragment.chatbot

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mit.offroader.databinding.ItemChatbotBinding

class ChatAdapter(private val viewModel: ChatBotViewModel): ListAdapter<Conversation, RecyclerView.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemChatbotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        (holder as ChatItemViewHolder).apply {

        }

    }


    // 챗봇 아이템 xml 아이디 가져오기
    inner class ChatItemViewHolder(binding: ItemChatbotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val userChat = binding.tvChatUser
        val userChatBox = binding.llChatUser
        val aiChat = binding.tvChatAi
        val aiChatBox = binding.llChatAi


    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Conversation>() {
            override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
                Log.d("Happy_TagRv","^^Items theSame?")
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
                Log.d("Happy_TagRv","^^ContentmentSame?")
                return oldItem == newItem
            }
        }
    }

}