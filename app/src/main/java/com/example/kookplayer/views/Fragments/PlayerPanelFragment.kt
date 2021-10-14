package com.example.kookplayer.views.Fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.kookplayer.R
import com.example.kookplayer.databinding.FragmentPlayerPanelBinding
import com.example.kookplayer.extensions.isFavorite
import com.example.kookplayer.helper.Coordinator
import com.example.kookplayer.myInterface.PlayerPanelInterface
import com.example.kookplayer.repositories.RoomRepository
import com.example.kookplayer.utlis.ImageUtils
import com.example.kookplayer.utlis.ScreenSizeUtils
import com.example.kookplayer.utlis.TimeUtils
import com.example.kookplayer.views.activities.MainActivity
import com.frolo.waveformseekbar.WaveformSeekBar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_player_panel.view.*
import kotlinx.android.synthetic.main.panel_header_on_collapsed.view.*
import kotlinx.android.synthetic.main.panel_header_on_expanded.view.*
import kotlinx.android.synthetic.main.player_remote.*
import kotlinx.android.synthetic.main.player_remote.view.*
import kotlin.random.Random


class PlayerPanelFragment : Fragment(), PlayerPanelInterface, View.OnClickListener,
    WaveformSeekBar.OnSeekBarChangeListener {

    lateinit var binding: FragmentPlayerPanelBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_panel, container, false)

        initBinding(view)

        updatePanelBasedOnState(SlidingUpPanelLayout.PanelState.COLLAPSED)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.baseContext?.let {
            Coordinator.setup(
                it
            )
        }

        setDefaultVisibilities()

        setOnEventListeners()

        seekbarHandler()

        waveform_seek_bar.setWaveform(createWaveform(), true)

        setupLayoutParams()

    }

    private fun setupLayoutParams() {

        binding.musicAlbumImage.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 5.1 / 10).toInt()
        binding.musicAlbumImage.layoutParams.width = ScreenSizeUtils.getScreenWidth() * 10 / 10
        binding.musicAlbumImage.requestLayout()


        binding.header.layoutParams.height = (ScreenSizeUtils.getScreenHeight() * 0.8 / 10).toInt()
        binding.header.requestLayout()


        binding.musicTitleTv.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 0.4 / 10).toInt()
        binding.header.requestLayout()


        binding.playerRemote.constraintLayout2.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 0.5 / 10).toInt()
        binding.header.requestLayout()


        binding.playerRemote.constraintLayout3.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 1.4 / 10).toInt()
        binding.header.requestLayout()


        binding.playerRemote.shuffleRepeatLayout.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 1 / 10)
        binding.header.requestLayout()


        binding.header.onCollapse.song_image_on_header.layoutParams.height =
            ((ScreenSizeUtils.getScreenHeight() * 1 / 10) * 8 / 10)
        binding.header.onCollapse.song_image_on_header.layoutParams.width =
            (ScreenSizeUtils.getScreenWidth() * 2 / 10)
        binding.header.onCollapse.song_image_on_header.requestLayout()


        binding.header.onCollapse.wheelprogress.layoutParams.height =
            ((ScreenSizeUtils.getScreenHeight() * 1 / 10) * 7 / 10)
        binding.header.onCollapse.wheelprogress.layoutParams.width =
            (ScreenSizeUtils.getScreenWidth() * 1.1 / 10).toInt()
        binding.header.onCollapse.wheelprogress.requestLayout()


        binding.onExpand.likeIv.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 0.6 / 10).toInt()
        binding.onExpand.likeIv.layoutParams.width =
            (ScreenSizeUtils.getScreenWidth() * 0.6 / 10).toInt()
        binding.onExpand.likeIv.requestLayout()
    }

    override fun setDefaultVisibilities() {
        binding.header.onCollapse.play_btn_on_header.visibility = View.GONE
    }

    fun setOnEventListeners() {
        binding.header.onCollapse.play_btn_on_header.setOnClickListener(this)
        binding.header.onCollapse.pause_btn_on_header.setOnClickListener(this)
        binding.playerRemote.nextBtn.setOnClickListener(this)
        binding.playerRemote.prevBtn.setOnClickListener(this)
        binding.playerRemote.playOrPauseLayout.setOnClickListener(this)
        binding.playerRemote.shuffleContainer.setOnClickListener(this)
        binding.playerRemote.repeatContainer.setOnClickListener(this)
        binding.playerRemote.waveformSeekBar.setOnSeekBarChangeListener(this)
        binding.onExpand.likeIv.setOnClickListener(this)
    }

    override fun setSongImage() {

        context?.let {
            ImageUtils.loadImageToImageView(
                it,
                binding.musicAlbumImage,
                Coordinator.currentPlayingSong?.image!!
            )
        }
    }

    fun updatePanel() {
        setSongTitle()
        setSongImage()

        if (Coordinator.currentPlayingSong!!.isFavorite()) {
            binding.header.onExpand.like_iv.setImageResource(R.drawable.ic_favored)
        } else {
            binding.header.onExpand.like_iv.setImageResource(R.drawable.ic_unfavored)
        }

        binding.playerRemote.musicMax.text =
            Coordinator.currentPlayingSong?.duration?.let {
                TimeUtils.getReadableDuration(
                    it
                )
            }
    }

    override fun setSongTitle() {
        binding.musicTitleTv.text = Coordinator.currentPlayingSong?.title
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

    override fun seekTo(mCurrentPosition: Int) {

        binding.playerRemote.waveformSeekBar.setProgressInPercentage(
            mCurrentPosition / (Coordinator.currentPlayingSong?.duration?.div(
                1000F
            )!!)
        )


        if (binding.header.onCollapse.visibility == View.VISIBLE) {

            updateWheelProgress(
                (mCurrentPosition * 360) / ((Coordinator.currentPlayingSong?.duration?.div(
                    1000
                ))?.toInt() ?: 0)
            )

            binding.header.onCollapse.song_title_on_header.text =
                if (Coordinator.isPlaying()) Coordinator.currentPlayingSong?.title else ""

            context?.let {
                ImageUtils.loadImageToImageView(
                    it,
                    binding.header.onCollapse.song_image_on_header,
                    Coordinator.currentPlayingSong?.image!!
                )
            }
        }

    }

    override fun seekbarHandler() {
        val mHandler = Handler()
        activity?.runOnUiThread(object : Runnable {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if (Coordinator.isPlaying()) {

                    val mCurrentPosition = Coordinator.getPositionInPlayer() / 1000
                    val duration = Coordinator.currentPlayingSong?.duration?.div(1000)

                    seekTo(mCurrentPosition)
                    setRemainingTime(mCurrentPosition)

                    if (mCurrentPosition == duration?.toInt()?.minus(3) ?: 0) {
                        Coordinator.takeActionBasedOnRepeatMode(
                            MainActivity.activity.getString(R.string.onSongCompletion),
                            MainActivity.activity.getString(R.string.play_next)
                        )
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

    override fun updatePanelBasedOnState(newState: SlidingUpPanelLayout.PanelState) {

        when (newState) {
            SlidingUpPanelLayout.PanelState.EXPANDED -> {

                binding.header.onExpand.visibility = View.VISIBLE
                binding.header.onCollapse.visibility = View.GONE
                switchPlayPauseButton()
            }
            SlidingUpPanelLayout.PanelState.COLLAPSED -> {

                binding.header.onCollapse.visibility = View.VISIBLE
                binding.header.onExpand.visibility = View.GONE

                try {
                    if(Coordinator.isPlaying())
                    {
                        binding.header.onCollapse.play_btn_on_header.visibility = View.GONE
                        binding.header.onCollapse.pause_btn_on_header.visibility = View.VISIBLE
                    }
                    else
                    {
                        binding.header.onCollapse.play_btn_on_header.visibility = View.VISIBLE
                        binding.header.onCollapse.pause_btn_on_header.visibility = View.GONE
                    }
                }
                catch (e :Exception)
                {

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v) {
            binding.onExpand.likeIv -> {
                if (Coordinator.currentPlayingSong!!.isFavorite()) {

                    binding.onExpand.likeIv.setImageResource(R.drawable.ic_unfavored)
                    RoomRepository.removeSongFromFavorites(Coordinator.currentPlayingSong!!)


                } else {
                    binding.onExpand.likeIv.setImageResource(R.drawable.ic_favored)

                    RoomRepository.addSongToFavorites(Coordinator.currentPlayingSong!!.id ?: -1)

                }
            }

            binding.playerRemote.nextBtn -> Coordinator.playNextSong()

            binding.playerRemote.prevBtn -> Coordinator.playPrevSong()

            binding.playerRemote.playOrPauseLayout -> {

                if (Coordinator.isPlaying()) {
                    Coordinator.pause()
                } else {
                    Coordinator.resume()
                }
                switchPlayPauseButton()
            }

            binding.playerPanel.shuffle_container -> {
                if (Coordinator.shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE) {

                    Coordinator.shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL

                    binding.playerPanel.shuffle_container.displayedChild = 1

                    Coordinator.updateNowPlayingQueue()

                } else {

                    Coordinator.shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE

                    binding.playerPanel.shuffle_container.displayedChild = 2

                    Coordinator.updateNowPlayingQueue()
                }
            }

            binding.playerRemote.repeatContainer -> {
                when (Coordinator.repeatMode) {
                    PlaybackStateCompat.REPEAT_MODE_NONE -> {
                        Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL

                        binding.playerPanel.repeatContainer.displayedChild = 1

                        Coordinator.updateNowPlayingQueue()
                    }

                    PlaybackStateCompat.REPEAT_MODE_ALL -> {
                        Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_ONE

                        binding.playerPanel.repeatContainer.displayedChild = 2

                        Coordinator.updateNowPlayingQueue()
                    }

                    PlaybackStateCompat.REPEAT_MODE_ONE -> {
                        Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_NONE

                        binding.playerPanel.repeatContainer.displayedChild = 3

                        Coordinator.updateNowPlayingQueue()
                    }
                }
            }

            binding.header.onCollapse.play_btn_on_header -> {

                Coordinator.resume()
                binding.header.onCollapse.play_btn_on_header.visibility = View.GONE
                binding.header.onCollapse.pause_btn_on_header.visibility = View.VISIBLE
            }

            binding.header.onCollapse.pause_btn_on_header -> {
                Coordinator.pause()
                binding.header.onCollapse.play_btn_on_header.visibility = View.VISIBLE
                binding.header.onCollapse.pause_btn_on_header.visibility = View.GONE
            }
        }
    }

    private fun createWaveform(): IntArray {
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
            TimeUtils.getReadableDuration((remainingTimeInPercentage * 1000).toLong())
    }

    private fun updateWheelProgress(progressInPercentage: Int) {
        binding.header.onCollapse.wheelprogress.setPercentage(progressInPercentage)
    }

    private fun initBinding(view: View) {
        binding = FragmentPlayerPanelBinding.bind(view)
    }

    override fun onProgressInPercentageChanged(
        seekBar: WaveformSeekBar?,
        percent: Float,
        fromUser: Boolean
    ) {

        if (Coordinator.isPlaying()) {

            binding.playerRemote.musicMin?.text = TimeUtils.getReadableDuration(
                (percent * TimeUtils.getDurationOfCurrentMusic().toLong()).toLong()
            )
        }
    }

    override fun onStartTrackingTouch(seekBar: WaveformSeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: WaveformSeekBar?) {
        Coordinator.seekTo((waveform_seek_bar.progressPercent * Coordinator.currentPlayingSong?.duration!!).toInt())
    }

}