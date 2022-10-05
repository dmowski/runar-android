package com.tnco.runar.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.feature.MusicController
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.AnalyticsConstants
import kotlinx.coroutines.delay

class GeneratorMagicRune : Fragment() {
    private lateinit var viewModel: MainViewModel
    lateinit var progressBar: ProgressBar
    private var link = ""
    private lateinit var textGroupName: TextView
    private lateinit var textSongName: TextView
    private lateinit var imageGroup: ImageView
    private lateinit var sendLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            CancelDialog(
                requireContext(),
                viewModel.fontSize.value!!,
                "generator_processing",
                getString(R.string.description_generator_popup)
            ) {
                val direction = GeneratorMagicRuneDirections.actionGlobalGeneratorFragment()
                findNavController().navigate(direction)
            }
                .showDialog()
        }
    }

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
        progressBar = view.findViewById(R.id.generator_progress_of_loading_view)
        textGroupName = view.findViewById(R.id.generator_text_group_name)
        textSongName = view.findViewById(R.id.generator_text_song_name)
        imageGroup = view.findViewById(R.id.generator_image_group)
        sendLink = view.findViewById(R.id.generator_description_button_frame)
        progressBarAction()
        sendLink.setOnClickListener {
            AnalyticsHelper.sendEvent(
                AnalyticsEvent.MUSIC_LINK_OPENED,
                Pair(AnalyticsConstants.GROUP_NAME, textGroupName.text.toString()),
                Pair(AnalyticsConstants.TRACK_NAME, textSongName.text.toString())
            )
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
                        link = "https://lyod1.bandcamp.com/releases"
                        textGroupName.text = getString(R.string.group1)
                        textSongName.text = getString(R.string.track1_1)
                        imageGroup.setImageResource(R.drawable.led_image)
                    }
                    3 -> {
                        link = "https://lyod1.bandcamp.com/releases"
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

            val direction = GeneratorMagicRuneDirections
                .actionGeneratorMagicRuneToRunePatternGenerator()
            findNavController().navigate(direction)
        }
    }
}