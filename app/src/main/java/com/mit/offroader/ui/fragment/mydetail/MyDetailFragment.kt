package com.mit.offroader.ui.fragment.mydetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mit.offroader.R

class MyDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MyDetailFragment()
    }

    private lateinit var viewModel: MyDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}