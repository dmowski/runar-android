package com.test.runar.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutInterpretationFragment : Fragment(R.layout.fragment_layout_interpretation),
        View.OnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var header: TextView
    private lateinit var headerText: String
    private var runeHeight: Int =0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        header =
                ((view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(1) as FrameLayout).getChildAt(0) as TextView

        model.selectedLayout.observe(viewLifecycleOwner){
            if(it!=null){
                header.text = it.layoutName
            }
        }
        model.runeHeight.observe(viewLifecycleOwner){
            if(it!=null){
                runeHeight = it
                var runeView = (view.findViewById<android.widget.ScrollView>(com.test.runar.R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(2)
                var layoutParams = runeView.layoutParams
                layoutParams.height = 223
                layoutParams.width = (223/1.23).toInt()
                runeView.layoutParams = layoutParams
            }
        }
    }

    override fun onStop() {
        super.onStop()
        model.clearLayoutData()
    }
    override fun onClick(v: View?) {
        val navController = findNavController()
    }
}