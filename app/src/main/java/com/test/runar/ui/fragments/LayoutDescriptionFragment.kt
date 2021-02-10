package com.test.runar.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutDescriptionFragment : Fragment(R.layout.fragment_layout_description),
    View.OnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var checkBox: CheckBox
    private lateinit var header: TextView
    private lateinit var text: TextView
    private var fontSize: Float =0f
    var layoutId: Int = 0

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

        checkBox = view.findViewById(R.id.checkbox)

        text = view.findViewById(R.id.description_text_view)
        header =
            view.findViewById<FrameLayout>(R.id.description_header_frame).getChildAt(0) as TextView

        model.selectedLayout.observe(viewLifecycleOwner) {
            if (it != null) {
                header.text = it.layoutName
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                text.text = it.layoutDescription
                layoutId = it.layoutId!!
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onClick(v: View?) {
        val navController = findNavController()
        when (v?.id) {
            R.id.exit_button -> {
                if (checkBox.isChecked) model.notShowSelectedLayout(requireContext(), layoutId)
                navController.navigate(R.id.layoutFragment)
            }
            R.id.description_button_frame -> {
                if (checkBox.isChecked) model.notShowSelectedLayout(requireContext(), layoutId)
                val bundle = bundleOf("descriptionFontSize" to fontSize)
                navController.navigate(R.id.layoutInitFragment,bundle)
            }
        }
    }
}
