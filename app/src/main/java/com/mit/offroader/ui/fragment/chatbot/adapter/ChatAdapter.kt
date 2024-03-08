package com.mit.offroader.ui.fragment.chatbot.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mit.offroader.data.model.ai.Message
import com.mit.offroader.databinding.ItemChatbotBinding
import com.mit.offroader.ui.fragment.chatbot.viewmodel.ChatBotViewModel

class ChatAdapter(private val viewModel: ChatBotViewModel) :
    ListAdapter<Message, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemChatbotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("chatadapter", "^^ 1. 바인딩 잘됨")
        return ChatItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        Log.d("chatadapter", "^^ 2. 바인드 뷰홀더 실행")

        val item = getItem(position)
//        Log.d("chatadapter", "^^ 3. 겟 아이템 $item")


        (holder as ChatItemViewHolder).apply {


            // 타입이 ai인지 유저인지에 따라 리사이클러뷰의 버블을 하나씩 지워주는 로직
            when (item.role) {
                "user" -> {
                    userChat.visibility = View.VISIBLE
                    userChatBox.visibility = View.VISIBLE
                    aiChat.isVisible = false
                    aiChatBox.isVisible = false
                    userChat.text = item.content
                }

                "assistant" -> {
                    userChat.isVisible = false
                    userChatBox.isVisible = false
                    aiChat.visibility = View.VISIBLE
                    aiChatBox.visibility = View.VISIBLE
                    aiChat.text = item.content
                }

                else -> {
//                    Log.d("error","^^ 대화 role이 잘못 설정되어있다 오타 확인 !!")
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
//                Log.d("chatadapter","^^Items theSame?")
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
//                Log.d("chatadapter","^^ContentmentSame?")
                return oldItem == newItem
            }
        }
    }

}