package com.example.cassette.views.Fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.cassette.R
import com.example.cassette.databinding.FragmentPlayerPanelBinding
import com.example.cassette.extensions.isShuffle
import com.example.cassette.player.Coordinator
import com.example.cassette.player.PlayerStateRepository
import com.example.cassette.utlis.TimeUtils


class PlayerPanelFragment : Fragment() {

    lateinit var binding: FragmentPlayerPanelBinding
    var likeState: Boolean = false
    var playingState: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_player_panel, container, false)

        binding = FragmentPlayerPanelBinding.bind(view)

        return view
    }

    override fun onResume() {
        super.onResume()

        setVisibilities()

        context?.let {
            Coordinator.setupMediaPlayerAgent(
                it,
                binding.musicAlbumImage,
                binding.musicTitleTv
            )
        }

        binding.likeIv.setOnClickListener {
            likeState = when (likeState) {
                true -> {
                    binding.likeIv.setImageResource(R.drawable.ic_heart)
                    false
                }
                false -> {
                    binding.likeIv.setImageResource(R.drawable.ic_filled_heart)
                    true
                }
            }
        }

//        binding.playerRemote.waveformSeekBar.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener,
//            WaveformSeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                Toast.makeText(
//                    context,
//                    "onProgressChanged",
//                    Toast.LENGTH_SHORT
//                ).show()
////                waveform_seek_bar.setProgressInPercentage(0.25F)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                Toast.makeText(
//                    context,
//                    "onStartTrackingTouch",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                Toast.makeText(
//                    context,
//                    "onStopTrackingTouch",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onProgressInPercentageChanged(
//                seekBar: WaveformSeekBar?,
//                percent: Float,
//                fromUser: Boolean
//            ) {
//                if (Coordinator.playerIsPlaying()) {
//
//                    Toast.makeText(
//                        context,
//                        "onProgressInPercentageChanged",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    binding.playerRemote.musicMin.text = TimeUtils.milliSecToDuration(
//                        (percent * TimeUtils.getDurationOfCurrentMusic().toLong()).toLong()
//                    )
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: WaveformSeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: WaveformSeekBar?) {
//                Toast.makeText(
//                    context,
//                    "Tracked: percent=" + waveform_seek_bar.progressPercent,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        })
//        waveform_seek_bar.setWaveform(createWaveform(), true)


        val mHandler = Handler()
        activity?.runOnUiThread(object : Runnable {
            override fun run() {
                if (Coordinator != null) {
                    val mCurrentPosition = Coordinator.getCurrentMediaPlayerPosition() / 1000
                    binding.playerRemote.seekBar.setProgress(mCurrentPosition)
                    binding.playerRemote.seekBar.max = Coordinator.getMediaPlayerDuration() / 1000
                    if (Coordinator.playerIsPlaying()) {
                        updateProgress(mCurrentPosition)
                    }
                }
                mHandler.postDelayed(this, 1000)
            }
        })


        binding.playerRemote.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {

//                Toast.makeText(
//                    context,
//                    "onProgressChanged , progress is:$progress",
//                    Toast.LENGTH_SHORT
//                ).show()

                seekBar.setProgress(progress)

                if (seekBar.max - progress <= 0) {
                    Coordinator.playNextSong()
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

        })


        binding.playerRemote.nextBtn.setOnClickListener() {
            Coordinator.playNextSong()
        }


        binding.playerRemote.prevBtn.setOnClickListener {
            Coordinator.playPrevSong()
        }

        binding.playerRemote.playOrPauseLayout.setOnClickListener {

            if (!Coordinator.playerIsPlaying()) {
                binding.playerRemote.pauseBtn.visibility = View.GONE
                binding.playerRemote.playBtn.visibility = View.VISIBLE
                Coordinator.resumePlaying()
            } else {
                binding.playerRemote.pauseBtn.visibility = View.VISIBLE
                binding.playerRemote.playBtn.visibility = View.GONE
                Coordinator.pauseSong()
            }
            updateUI()

        }

        binding.playerRemote.shuffleBtn.setOnClickListener {
            val i = Coordinator.getCurrentPlayerMode().isShuffle()
            when (Coordinator.getCurrentPlayerMode()) {
                PlayerStateRepository.PlayerModes.REPEAT_ALL -> {
                    Coordinator.changePlayerMode(PlayerStateRepository.PlayerModes.SHUFFLE)
                }

                PlayerStateRepository.PlayerModes.SHUFFLE -> {
                    Coordinator.changePlayerMode(PlayerStateRepository.PlayerModes.REPEAT_ALL)
                }
            }
        }

        binding.playerRemote.repeatLayout.setOnClickListener {
            if (PlayerStateRepository.currentPlayerMode == PlayerStateRepository.PlayerModes.REPEAT_ALL) {
                changePlayerMode(PlayerStateRepository.PlayerModes.REPEAT_ONE)
            } else {
                changePlayerMode(PlayerStateRepository.PlayerModes.REPEAT_ONE)
            }
        }
    }

    fun changePlayerMode(newMode: PlayerStateRepository.PlayerModes) {

        Coordinator.changePlayerMode(newMode)

        if (binding.playerRemote.playOneSongLabelTv.isVisible) {
            binding.playerRemote.playOneSongLabelTv.visibility = View.GONE
        } else {
            binding.playerRemote.playOneSongLabelTv.visibility = View.VISIBLE
        }
    }

//    private fun createWaveform(): IntArray? {
//        val random = Random(System.currentTimeMillis())
//        val length: Int = 50 + random.nextInt(50)
//        val values = IntArray(length)
//        var maxValue = 0
//        for (i in 0 until length) {
//            val newValue: Int = 5 + random.nextInt(50)
//            if (newValue > maxValue) {
//                maxValue = newValue
//            }
//            values[i] = newValue
//        }
//        return values
//    }

    private fun updateUI() {
        binding.playerRemote.musicMax.text =
            TimeUtils.milliSecToDuration(TimeUtils.getDurationOfCurrentMusic().toLong())
    }

    fun setVisibilities() {
        binding.playerRemote.pauseBtn.visibility = View.GONE
        binding.playerRemote.playOneSongLabelTv.visibility = View.GONE
    }

    fun updateProgress(progressInPercentage: Int) {
        binding.playerRemote.musicMin.text =
            TimeUtils.milliSecToDuration((progressInPercentage * 1000).toLong())
    }
}