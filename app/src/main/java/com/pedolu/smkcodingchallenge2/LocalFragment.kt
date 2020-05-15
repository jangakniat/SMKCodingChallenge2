package com.pedolu.smkcodingchallenge2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LocalFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    companion object {
        fun newInstance(): LocalFragment{
            val fragment = LocalFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}