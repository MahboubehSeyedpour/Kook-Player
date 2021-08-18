package com.example.cassette.views.Fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cassette.R
import com.example.cassette.`interface`.PlayerPanelInterface
import com.example.cassette.databinding.FragmentPlayerPanelBinding
import com.example.cassette.player.Coordinator
import com.example.cassette.player.Enums
import com.example.cassette.player.Enums.PanelState
import com.example.cassette.player.Enums.PanelState.COLLAPSED
import com.example.cassette.player.Enums.PanelState.EXPANDED
import com.example.cassette.player.PlayerStateRepository
import com.example.cassette.utlis.ImageUtils
import com.example.cassette.utlis.TimeUtils
import com.frolo.waveformseekbar.WaveformSeekBar
import kotlinx.android.synthetic.main.fragment_player_panel.view.*
import kotlinx.android.synthetic.main.panel_header_on_collapsed.view.*
import kotlinx.android.synthetic.main.panel_header_on_expanded.view.*
import kotlinx.android.synthetic.main.player_remote.*
import kotlin.random.Random

class PlayerPanelFragment : Fragment(), PlayerPanelInterface, View.OnClickListener {

    lateinit var binding: FragmentPlayerPanelBinding
    var previouslyLiked: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_panel, container, false)

        setBinding(view)

        updatePanelBasedOnState(COLLAPSED)

        return view
    }

    override fun onResume() {
        super.onResume()

        context?.let {
            Coordinator.setup(
                it
            )
        }

        binding.header.onExpand.like_iv.setOnClickListener(this)
        binding.header.onCollapse.play_btn_on_header.setOnClickListener(this)
        binding.playerRemote.nextBtn.setOnClickListener(this)
        binding.playerRemote.prevBtn.setOnClickListener(this)
        binding.playerRemote.playOrPauseLayout.setOnClickListener(this)
        binding.playerRemote.shuffleBtn.setOnClickListener(this)
        binding.playerRemote.repeatLayout.setOnClickListener(this)

        seekbarHandler()

        binding.playerRemote.waveformSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener,
            WaveformSeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                Toast.makeText(
//                    context,
//                    "onProgressChanged",
//                    Toast.LENGTH_SHORT
//                ).show()
//                waveform_seek_bar.setProgressInPercentage(0.25F)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                Toast.makeText(
//                    context,
//                    "onStartTrackingTouch",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                Toast.makeText(
//                    context,
//                    "onStopTrackingTouch",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onProgressInPercentageChanged(
                seekBar: WaveformSeekBar?,
                percent: Float,
                fromUser: Boolean
            ) {
                if (Coordinator.isPlaying()) {

                    setSongTitle()
                    setSongImage()

//                    binding.musicTitleTv.startAnimation(inFromRightAnimation())

                    binding.playerRemote.musicMin.text = TimeUtils.milliSecToDuration(
                        (percent * TimeUtils.getDurationOfCurrentMusic().toLong()).toLong()
                    )
                    binding.playerRemote.musicMax.text =
                        TimeUtils.milliSecToDuration(TimeUtils.getDurationOfCurrentMusic().toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: WaveformSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: WaveformSeekBar?) {
//                Toast.makeText(
//                    context,
//                    "Tracked: percent=" + waveform_seek_bar.progressPercent,
//                    Toast.LENGTH_SHORT
//                ).show()

                Coordinator.seekTo((waveform_seek_bar.progressPercent * Coordinator.getCurrentPlayingSong().duration).toInt())
            }

        })
        waveform_seek_bar.setWaveform(createWaveform(), true)


//        binding.playerRemote.seekBar.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//
//            override fun onProgressChanged(
//                seekBar: SeekBar,
//                progress: Int,
//                fromUser: Boolean
//            ) {
//                seekBar.progress = progress
//                if (seekBar.max - progress <= 0) {
//                    Coordinator.playNextSong()
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {}
//
//        })

    }


    override fun setDefaultVisibilities() {
        TODO("Not yet implemented")
    }

    override fun setSongImage() {
        context?.let {
            ImageUtils.loadImageToImageView(
                it,
                binding.musicAlbumImage,
                Coordinator.getCurrentPlayingSong().image!!
            )
        }
    }

    override fun setSongTitle() {
        binding.musicTitleTv.text = Coordinator.getCurrentPlayingSong().title
    }

    override fun getPanelState() {
        TODO("Not yet implemented")
    }

    override fun setPanelState() {
        TODO("Not yet implemented")
    }

    override fun updateHeader() {
        TODO("Not yet implemented")
    }

    override fun seekTo() {
        TODO("Not yet implemented")
    }

    override fun seekbarHandler() {
        val mHandler = Handler()
        activity?.runOnUiThread(object : Runnable {
            override fun run() {
                if (Coordinator != null && Coordinator.isPlaying()) {

                    val mCurrentPosition = Coordinator.getPositionInPlayer() / 1000

//                    binding.playerRemote.seekBar.max =
//                        (Coordinator.getCurrentPlayingSong().duration / 1000).toInt()

//                    binding.playerRemote.seekBar.progress = mCurrentPosition

                    binding.playerRemote.waveformSeekBar.setProgressInPercentage(
                        mCurrentPosition / (Coordinator.getCurrentPlayingSong().duration / 1000F)
                    )
                    setRemainingTime(mCurrentPosition)
                    if (binding.header.onCollapse.visibility == View.VISIBLE) {

                        updateWheelProgress((mCurrentPosition * 360) / (Coordinator.getCurrentPlayingSong().duration / 1000).toInt())

                        binding.header.onCollapse.song_title_on_header.text =
                            if (Coordinator.isPlaying()) Coordinator.getCurrentPlayingSong().title else ""
                    }
                }
                mHandler.postDelayed(this, 1000)
            }
        })
    }

    override fun switchPlayPauseButton() {
        if (Coordinator.isPlaying()) {
            binding.playerRemote.pauseBtn.visibility = View.VISIBLE
            binding.playerRemote.playBtn.visibility = View.GONE
        } else {
            binding.playerRemote.pauseBtn.visibility = View.GONE
            binding.playerRemote.playBtn.visibility = View.VISIBLE
        }
    }

    override fun updatePanelBasedOnState(newState: PanelState) {
        when (newState) {
            EXPANDED -> {

                binding.header.onExpand.visibility = View.VISIBLE
                binding.header.onCollapse.visibility = View.GONE
                switchPlayPauseButton()
            }
            COLLAPSED -> {

                binding.header.onCollapse.visibility = View.VISIBLE
                binding.header.onExpand.visibility = View.GONE
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.header.onExpand.like_iv -> {
                if (previouslyLiked) {
                    binding.header.onExpand.like_iv.setImageResource(R.drawable.ic_heart)
                } else {
                    binding.header.onExpand.like_iv.setImageResource(R.drawable.ic_filled_heart)
                }
                previouslyLiked = !previouslyLiked
            }

            binding.playerRemote.nextBtn -> Coordinator.playNextSong()

            binding.playerRemote.prevBtn -> Coordinator.playPrevSong()

            binding.playerRemote.playOrPauseLayout -> {
                if (!Coordinator.isPlaying()) Coordinator.resume() else Coordinator.pause()
                switchPlayPauseButton()
            }

            binding.playerRemote.shuffleBtn -> {
                Coordinator.changePlayingMode(Enums.PlayingOrder.SHUFFLE)
                Toast.makeText(context, "shuffle is ON", Toast.LENGTH_SHORT).show()
            }

            binding.playerRemote.repeatLayout -> {
                if (PlayerStateRepository.currentPlayerMode == PlayerStateRepository.PlayerModes.REPEAT_ALL) {
                    Coordinator.changePlayingMode(Enums.PlayingOrder.REPEAT_ONE)
                } else {
                    Coordinator.changePlayingMode(Enums.PlayingOrder.REPEAT_ALL)
                }
            }

            binding.header.onCollapse.play_btn_on_header -> {
                if (Coordinator.isPlaying()) {
                    Coordinator.pause()
                } else {
                    Coordinator.resume()
                }

//                binding.header.onCollapse.song_title_on_header.text =
//                    if (Coordinator.isPlaying()) Coordinator.getCurrentPlayingSong().title else ""
            }
        }
    }

    private fun createWaveform(): IntArray? {
        val random = Random(System.currentTimeMillis())
        val length: Int = 50 + random.nextInt(50)
        val values = IntArray(length)
        var maxValue = 0
        for (i in 0 until length) {
            val newValue: Int = 5 + random.nextInt(50)
            if (newValue > maxValue) {
                maxValue = newValue
            }
            values[i] = newValue
        }
        return values
    }

    override fun setRemainingTime(remainingTimeInPercentage: Int) {
        binding.playerRemote.musicMin.text =
            TimeUtils.milliSecToDuration((remainingTimeInPercentage * 1000).toLong())
    }

    fun updateWheelProgress(progressInPercentage: Int) {
        binding.header.onCollapse.wheelprogress.setPercentage(progressInPercentage)
    }

    fun setBinding(view: View) {
        binding = FragmentPlayerPanelBinding.bind(view)
    }

//    private fun inFromRightAnimation() : Animation
//    {
//        val inFromRight: Animation = TranslateAnimation(
//            Animation.RELATIVE_TO_PARENT,
//            +1.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f
//        )
//        inFromRight.duration = 4000
//        inFromRight.interpolator = AccelerateInterpolator()
//        return inFromRight
//    }
//
//    private fun outToLeftAnimation(): Animation
//    {
//        val outtoLeft = TranslateAnimation(
//            Animation.RELATIVE_TO_PARENT,
//            0.0f,
//            Animation.RELATIVE_TO_PARENT,
//            -1.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f
//        )
//        outtoLeft.duration = 8000
//        outtoLeft.interpolator = AccelerateInterpolator()
//        return outtoLeft
//    }
//
//    private fun  inFromLeftAnimation(): Animation
//    {
//        val inFromLeft = TranslateAnimation(
//            Animation.RELATIVE_TO_PARENT,
//            -1.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f
//        )
//        inFromLeft.duration = 2000
//        inFromLeft.interpolator = AccelerateInterpolator()
//        return inFromLeft
//    }
//
//    private fun  outToRightAnimation() : Animation
//    {
//        val outtoRight = TranslateAnimation(
//            Animation.RELATIVE_TO_PARENT,
//            0.0f,
//            Animation.RELATIVE_TO_PARENT,
//            +1.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f,
//            Animation.RELATIVE_TO_PARENT,
//            0.0f
//        )
//        outtoRight.duration = 2000
//        outtoRight.interpolator = AccelerateInterpolator ()
//        return outtoRight
//    }

}