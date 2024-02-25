package com.mit.offroader.ui.fragment.sanlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mit.offroader.R
import com.mit.offroader.ui.fragment.mydetail.MyDetailViewModel

class SanListFragment : Fragment() {

    companion object {
        fun newInstance() = SanListFragment()
    }

    private val sanListViewModel by viewModels<SanListViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_san_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}