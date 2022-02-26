package com.tnco.runar.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tnco.runar.R
import com.tnco.runar.controllers.AnalyticsHelper
import com.tnco.runar.controllers.MusicController
import com.tnco.runar.presentation.viewmodel.MainViewModel
import com.tnco.runar.retrofit.RetrofitClient
import com.tnco.runar.ui.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GeneratorMagicRune : Fragment() {
    private lateinit var viewModel: MainViewModel
    lateinit var progressBar: ProgressBar
    private var link = ""
    private lateinit var textGroupName: TextView
    private lateinit var textSongName: TextView
    private lateinit var imageGroup: ImageView
    private lateinit var sendLink: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_generator_processing, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomBar()
        progressBar = view.findViewById(R.id.generator_progress_of_loading_view)
        textGroupName = view.findViewById(R.id.generator_text_group_name)
        textSongName = view.findViewById(R.id.generator_text_song_name)
        imageGroup = view.findViewById(R.id.generator_image_group)
        sendLink = view.findViewById(R.id.generator_description_button_frame)
        progressBarAction()
        sendLink.setOnClickListener {
            AnalyticsHelper.musicLinkOpened(textGroupName.text.toString(), textSongName.text.toString())
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun progressBarAction() {
        viewModel.getRunePattern()
        lifecycleScope.launchWhenResumed {
            for (i in 0..100) {
                progressBar.progress = i
                delay(70)
                when (MusicController.currentSongPos) {
                    2 -> {
                        link ="https://lyod1.bandcamp.com/releases"
                        textGroupName.text = getString(R.string.group1)
                        textSongName.text = getString(R.string.track1_1)
                        imageGroup.setImageResource(R.drawable.led_image)
                    }
                    3 -> {
                        link ="https://lyod1.bandcamp.com/releases"
                        textGroupName.text = getString(R.string.group1)
                        textSongName.text = getString(R.string.track1_2)
                        imageGroup.setImageResource(R.drawable.led_image)
                    }
                    0 -> {
                        link = "https://danheimmusic.com/"
                        textGroupName.text = getString(R.string.group2)
                        textSongName.text = getString(R.string.track2_1)
                        imageGroup.setImageResource(R.drawable.danheim_image)
                    }
                    1 -> {
                        link = "https://danheimmusic.com/"
                        textGroupName.text = getString(R.string.group2)
                        textSongName.text = getString(R.string.track2_2)
                        imageGroup.setImageResource(R.drawable.danheim_image)
                    }
                }
            }
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, RunePatternGenerator())
                ?.commit()

        }
    }
}