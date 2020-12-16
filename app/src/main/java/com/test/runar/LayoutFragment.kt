package com.test.runar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

class LayoutFragment: Fragment(),View.OnClickListener {
    private lateinit var model: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_layouts,container,false)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        view.findViewById<ConstraintLayout>(R.id.first_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.second_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.third_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.fourth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.fifth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.sixth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.seventh_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.eight_layout).setOnClickListener(this)
        return view
    }
    override fun onClick(v: View?) { model.changeFragment(RunesFragment()) }
}