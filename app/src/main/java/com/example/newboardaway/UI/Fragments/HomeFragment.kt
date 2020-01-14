package com.example.newboardaway.UI.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.newboardaway.R
import com.example.newboardaway.UI.home.EventFragment
import kotlinx.android.synthetic.main.home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false)

        eventBtn.setOnClickListener {
            TODO()
        }
    }



}
