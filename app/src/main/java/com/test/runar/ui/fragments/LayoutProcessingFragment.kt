package com.test.runar.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel
import com.test.runar.ui.activity.MainActivity
import kotlinx.coroutines.launch


class LayoutProcessingFragment : Fragment() {

    private var progresLoading:ProgressBar?=null
    private var currentValue = 0

    private var nameLayuot:TextView? = null
    private var model: MainViewModel? = null
    private var layoutId:Int=0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_layout_processing, container, false)
        progresLoading = view.findViewById(R.id.progress)

        val runnable = Runnable {
            currentValue = 0
            while (currentValue <= 100) {
                try {
                    progresLoading?.setProgress(currentValue)
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

        nameLayuot = view.findViewById(R.id.name_layout)
        model = ViewModelProvider(this)[MainViewModel::class.java]
        // layoutId = arguments?.getInt("id")!!
        layoutId = 5  //TODO
        model?.getLayoutDescription(requireContext(), layoutId)
        model?.selectedLayout?.observe(viewLifecycleOwner) {
            if (it != null) {
                nameLayuot?.text = it.layoutName
            }
        }
        return view
    }
}

