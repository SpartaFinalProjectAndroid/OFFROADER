package com.mit.offroader.ui.fragment.chatbot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mit.offroader.R

class ChatBotFragment : Fragment() {

    companion object {
        fun newInstance() = ChatBotFragment()
    }

    private lateinit var viewModel: ChatBotViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_bot, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatBotViewModel::class.java)
        // TODO: Use the ViewModel
    }

}