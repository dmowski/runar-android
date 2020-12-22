package com.test.runar.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutFragment : Fragment(R.layout.fragment_layouts), View.OnClickListener {
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ConstraintLayout>(R.id.first_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.second_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.third_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.fourth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.fifth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.sixth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.seventh_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.eight_layout).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        model.changeFragment(RunesFragment())
    }
}