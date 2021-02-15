package com.test.runar.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.ProcessingViewModel
import kotlinx.coroutines.delay


class LayoutProcessingFragment : Fragment() {

    private var progressLoading: ProgressBar? = null
    private var currentValue = 0
    private var layoutId: Int = 0

    private var layoutNameTextView: TextView? = null
    private lateinit var viewModel: ProcessingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[ProcessingViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        layoutId = arguments?.getInt("layoutId")!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_layout_processing, container, false)
        progressLoading = view.findViewById(R.id.progress)
        progressBarAction()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        layoutNameTextView = view.findViewById(R.id.name_layout)
        viewModel.getLayoutName(layoutId)
        viewModel.layoutName.observe(viewLifecycleOwner) {name->
            layoutNameTextView?.text = name
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun progressBarAction() {
        lifecycleScope.launchWhenResumed {
            currentValue = 0
            while (currentValue <= 100) {
                progressLoading?.progress = currentValue
                delay(15) //inc to 90
                currentValue++
                val navController = findNavController()
                when (currentValue) {

                    100 -> navController.navigate(R.id.action_layoutProcessingFragment4_to_layoutInterpretationFragment)

                }
            }
        }
    }

}

