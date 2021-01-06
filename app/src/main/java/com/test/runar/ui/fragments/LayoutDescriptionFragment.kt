package com.test.runar.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutDescriptionFragment : Fragment(R.layout.fragment_layout_description), View.OnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var checkBox : CheckBox
    private lateinit var header: TextView
    private lateinit var text: TextView
    var layoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutId = arguments?.getInt("id")!!
        view.findViewById<FrameLayout>(R.id.description_button_frame).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.exit_button).setOnClickListener(this)
        checkBox = view.findViewById(R.id.checkbox)
        val headerFrame = view.findViewById<FrameLayout>(R.id.description_header_frame)
        text = view.findViewById<TextView>(R.id.description_text_view)
        header = headerFrame.getChildAt(0) as TextView
        model.getLayoutDescription(requireContext(),layoutId)
        model.selectedLayout.observe(viewLifecycleOwner){
            if(it!=null) {
                header.text = it.layoutName
                text.text = it.layoutDescription
                text.maxLines = it.maxLines!!
            }
        }
    }



    override fun onStop() {
        super.onStop()
        model.clearLayoutData()
    }

    override fun onClick(v: View?) {
        val navController = findNavController()
        when (v?.id){
            R.id.exit_button -> {
                navController.navigate(R.id.layoutFragment)
            }
            R.id.description_button_frame->{
                if(checkBox.isChecked){
                    model.notShowSelectedLayout(requireContext(),layoutId)
                    navController.navigate(R.id.runesFragment)
                }
                else{
                    navController.navigate(R.id.runesFragment)
                }
            }
        }
    }
}