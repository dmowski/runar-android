package com.test.runar.ui.fragments

import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel
import com.test.runar.ui.dialogs.CancelDialog

class LayoutInitFragment : Fragment(R.layout.fragment_layout_init),
        View.OnClickListener{
    private lateinit var model: MainViewModel
    private lateinit var header: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FrameLayout>(R.id.description_button_frame).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.exit_button).setOnClickListener(this)
        header =
            view.findViewById<FrameLayout>(R.id.description_header_frame).getChildAt(0) as TextView
        model.selectedLayout.observe(viewLifecycleOwner) {
            if (it != null) {
                header.text = it.layoutName
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
                navController.navigate(R.id.layoutFragment)
                //val alert = CancelDialog()
                //activity?.let { alert.showDialog(it) }
            }
            R.id.description_button_frame -> {
                navController.navigate(R.id.emptyFragment)
            }
        }
    }
}