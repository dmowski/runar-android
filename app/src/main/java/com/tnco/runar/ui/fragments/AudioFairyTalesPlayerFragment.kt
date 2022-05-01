package com.tnco.runar.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentAudioFairyTalesPlayerBinding
import java.util.*

class AudioFairyTalesPlayerFragment : Fragment() {

    private var _binding: FragmentAudioFairyTalesPlayerBinding? = null
    private val binding get() = _binding!!

    private val TAG = "PlayFragment"
    private var player = MediaPlayer()
    private var seekBarUpdateHandler = Handler()
    private var position = 1

    private lateinit var updateSeekBar: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioFairyTalesPlayerBinding.inflate(inflater, container, false)

        val toolbar = binding.toolbarPlayer
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = ""
        }


        binding.cardViewInfoPlayer.setOnClickListener {
            with(binding) {
                if (imageViewPlayer.visibility == View.VISIBLE) {
                    imageViewPlayer.visibility = View.GONE
                    constLayInfo.visibility = View.VISIBLE
                } else {
                    constLayInfo.visibility = View.GONE
                    imageViewPlayer.visibility = View.VISIBLE
                }
            }
        }
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.setting_player_tool_bar, menu)


        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, GeneratorFragment())
                ?.commit()}
        }
        return true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createLocalViews()

    }

    private fun createLocalViews() {
        createMediaPlayer()

        updateSeekBar = Runnable {
            with(binding) {
                val cal = Calendar.getInstance(Locale.ENGLISH)
                cal.timeInMillis = player.currentPosition.toLong()
                currentTime.text = DateFormat.format("mm:ss", cal).toString()

                seekBar.progress = player.currentPosition
                seekBarUpdateHandler.postDelayed(updateSeekBar, 50)
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress)
                    val cal = Calendar.getInstance(Locale.ENGLISH)
                    cal.timeInMillis = player.currentPosition.toLong()
                    binding.currentTime.text = DateFormat.format("mm:ss", cal).toString()
                }
            }
        })

        binding.imBtPlayAndStop.setOnClickListener {
            with(binding) {
                if (!player.isPlaying) {
                    imBtPlayAndStop.setImageResource(R.drawable.ic_player_pause)
                    seekBarUpdateHandler.postDelayed(updateSeekBar, 0)
                    with(player) {
                        seekTo(currentPosition)
                        start()
                    }
                } else {
                    imBtPlayAndStop.setImageResource(R.drawable.ic_player_play)
                    seekBarUpdateHandler.removeCallbacks(updateSeekBar)
                    player.pause()
                }
            }
        }

        binding.imBtPrevious.setOnClickListener {
            if (position > 0) {
                position--
                binding.imBtPlayAndStop.setImageResource(R.drawable.ic_player_play)
                player.pause()
                createMediaPlayer()
            }
        }

        binding.imBtNext.setOnClickListener {
            if (position < 7) {
                position++
                binding.imBtPlayAndStop.setImageResource(R.drawable.ic_player_play)
                player.pause()
                createMediaPlayer()
            }
        }

        binding.imBtFlashBack.setOnClickListener {
            if (player.isPlaying)player.seekTo(player.currentPosition - 30000)

        }

        binding.imBtFlashForward.setOnClickListener {
            if (player.isPlaying)player.seekTo(player.currentPosition + 30000)

        }
    }

    private fun createMediaPlayer() {
        val resId = when (position) {
            0 -> R.raw.danheim_kala
            1 -> R.raw.danheim_runar
            2 -> R.raw.led_mat_moya_skazala
            3 -> R.raw.led_chernaya_ladya
            else -> R.raw.led_chernaya_ladya
        }
        player = MediaPlayer.create(activity, resId)
        player.start()
        player.pause()

        binding.seekBar.max = player.duration

        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = player.duration.toLong()
        binding.totalTime.text = DateFormat.format("mm:ss", cal).toString()
        binding.currentTime.text = "00:00"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        seekBarUpdateHandler.removeCallbacks((updateSeekBar))
        Log.i(TAG, "onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }
}
