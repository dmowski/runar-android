package com.test.runar.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel
import com.test.runar.ui.dialogs.CancelDialog
import com.test.runar.ui.dialogs.DescriptionDialog

class LayoutInitFragment : Fragment(R.layout.fragment_layout_init),
    View.OnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var header: TextView
    private lateinit var headerText: String
    private lateinit var descriptionText: String
    private var fontSize: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fontSize = arguments?.getFloat("descriptionFontSize")!!

        view.findViewById<FrameLayout>(R.id.description_button_frame).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.exit_button).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.info_button).setOnClickListener(this)
        view.findViewById<TextView>(R.id.text_info).setOnClickListener(this)
        header =
            view.findViewById<FrameLayout>(R.id.description_header_frame).getChildAt(0) as TextView
        model.selectedLayout.observe(viewLifecycleOwner) {
            if (it != null) {
                header.text = it.layoutName
                headerText = it.layoutName.toString()
                descriptionText = it.layoutDescription.toString()
                var layoutFrame = view.findViewById<ConstraintLayout>(R.id.layoutFrame)
                ((layoutFrame.getChildAt(0) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot1.toString()
                ((layoutFrame.getChildAt(1) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot2.toString()
                ((layoutFrame.getChildAt(2) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot3.toString()
                ((layoutFrame.getChildAt(3) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot4.toString()
                ((layoutFrame.getChildAt(4) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot5.toString()
                ((layoutFrame.getChildAt(5) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot6.toString()
                ((layoutFrame.getChildAt(6) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot7.toString()
                for(i in 0..6){
                    if((((layoutFrame.getChildAt(i) as ConstraintLayout).getChildAt(0) as TextView).text as String).toInt()==0){
                        (layoutFrame.getChildAt(i) as ConstraintLayout).visibility=View.INVISIBLE
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        model.clearLayoutData()
    }

    override fun onClick(v: View?) {

        val navController = findNavController()
        when (v?.id) {
            R.id.exit_button -> {
                activity?.let { CancelDialog(navController, it) }?.showDialog()
            }
            R.id.description_button_frame -> {
                navController.navigate(R.id.emptyFragment)
            }
            R.id.info_button, R.id.text_info -> {
                val info = DescriptionDialog(descriptionText, headerText, fontSize)
                activity?.let { info.showDialog(it) }
            }
        }
    }

}