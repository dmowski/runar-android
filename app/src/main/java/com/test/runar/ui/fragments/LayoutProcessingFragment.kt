package com.test.runar.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel


class LayoutProcessingFragment : Fragment() {

    private var progressLoading:ProgressBar?=null
    private var currentValue = 0

    private var layoutNameTextView:TextView? = null
    private lateinit var model: MainViewModel
    private var layoutId:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_layout_processing, container, false)
        progressLoading = view.findViewById(R.id.progress)

        /*
        val runnable = Runnable {
            currentValue = 0
            while (currentValue <= 100) {
                try {
                    progressLoading?.setProgress(currentValue)
                    Thread.sleep(100) //speed
                } catch (e: InterruptedException) {
                }
                currentValue++
                val navController = findNavController()
                when(currentValue){

                    100 -> navController.navigate(R.id.favFragment)

                }
            }
        }
        val thread = Thread(runnable)
        thread.start()
        */


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        layoutNameTextView = view.findViewById(R.id.name_layout)
        model.selectedLayout.observe(viewLifecycleOwner) {
            if (it != null) {
                layoutNameTextView?.text = it.layoutName
            }
        }

        view.findViewById<FrameLayout>(R.id.description_button_frame).setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_layoutProcessingFragment4_to_layoutInterpretationFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}

