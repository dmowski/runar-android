package com.tnco.runar.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tnco.runar.R
import com.tnco.runar.controllers.AnalyticsHelper
import com.tnco.runar.controllers.GENERATOR_PATTERN_CREATED
import com.tnco.runar.controllers.GENERATOR_PATTERN_NEW_TYPE
import com.tnco.runar.presentation.viewmodel.MainViewModel
import com.tnco.runar.ui.activity.MainActivity

class RunePatternGenerator : Fragment() {
    private lateinit var viewModel: MainViewModel
    lateinit var sendBtn: TextView
    lateinit var nextType: TextView
    lateinit var imgRune: ImageView
    private var firstImageWasReady = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.rune_pattern_generator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.sendEvent(GENERATOR_PATTERN_CREATED)
        (activity as MainActivity).hideBottomBar()

        imgRune = view.findViewById(R.id.imageRune)
        nextType = view.findViewById(R.id.next_type)

        viewModel.runeImagesReady.observe(viewLifecycleOwner, Observer {
            if (it && !firstImageWasReady && viewModel.runesImages.size > 0) {
                imgRune.setImageBitmap(viewModel.runesImages[0])
                nextType.visibility = View.VISIBLE
            }
        })

        if (viewModel.runesImages.size > 0) {
            imgRune.setImageBitmap(viewModel.runesImages[0])
            nextType.visibility = View.VISIBLE
            firstImageWasReady = true
        } else {
            firstImageWasReady = false
            nextType.visibility = View.INVISIBLE
            imgRune.setImageResource(R.drawable.generator_hourglass)
        }


        nextType.setOnClickListener {
            AnalyticsHelper.sendEvent(GENERATOR_PATTERN_NEW_TYPE)
            viewModel.selectedRuneIndex += 1
            if (viewModel.selectedRuneIndex > viewModel.runesImages.size - 1) {
                viewModel.selectedRuneIndex = 0
            }
            imgRune.setImageBitmap(viewModel.runesImages[viewModel.selectedRuneIndex])
        }

        sendBtn = view.findViewById(R.id.button_select)
        sendBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, GeneratorBackground())
                ?.commit()
        }
    }
}