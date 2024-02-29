package com.mit.offroader.ui.fragment.chatbot

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mit.offroader.databinding.ItemChatbotBinding

class ChatAdapter(private val viewModel: ChatBotViewModel): ListAdapter<Chat, RecyclerView.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemChatbotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        (holder as ChatItemViewHolder).apply {


            // 타입이 ai인지 유저인지에 따라 리사이클러뷰의 버블을 하나씩 지워주는 로직
            when(item.type) {
                "ai" -> {
                    userChat.visibility = View.INVISIBLE
                    userChatBox.visibility = View.INVISIBLE
                    aiChat.text = item.text
                }
                "user" -> {
                    aiChat.visibility = View.INVISIBLE
                    aiChatBox.visibility = View.INVISIBLE
                    userChat.text = item.text
                }
            }
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                Log.d("Happy_TagRv","^^Items theSame?")
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                Log.d("Happy_TagRv","^^ContentmentSame?")
                return oldItem == newItem
            }
        }
    }

}