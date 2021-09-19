package com.example.cassette.views.Fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.cassette.R
import com.example.cassette.databinding.FragmentPlayerPanelBinding
import com.example.cassette.manager.Coordinator
import com.example.cassette.myInterface.PlayerPanelInterface
import com.example.cassette.player.Enums.PanelState
import com.example.cassette.player.Enums.PanelState.COLLAPSED
import com.example.cassette.player.Enums.PanelState.EXPANDED
import com.example.cassette.repositories.appdatabase.roomdb.DatabaseRepository
import com.example.cassette.repositories.appdatabase.roomdb.DatabaseRepository.songIsAlreadyLiked
import com.example.cassette.utlis.ImageUtils
import com.example.cassette.utlis.ScreenSizeUtils
import com.example.cassette.utlis.TimeUtils
import com.frolo.waveformseekbar.WaveformSeekBar
import kotlinx.android.synthetic.main.fragment_player_panel.view.*
import kotlinx.android.synthetic.main.panel_header_on_collapsed.view.*
import kotlinx.android.synthetic.main.panel_header_on_expanded.view.*
import kotlinx.android.synthetic.main.player_remote.*
import kotlinx.android.synthetic.main.player_remote.view.*
import kotlin.random.Random


class PlayerPanelFragment : Fragment(), PlayerPanelInterface, View.OnClickListener,
    WaveformSeekBar.OnSeekBarChangeListener {

    lateinit var binding: FragmentPlayerPanelBinding
    var previouslyLiked: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_panel, container, false)

        initBinding(view)

        updatePanelBasedOnState(COLLAPSED)

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


//        binding.onExpand.likeBtn.setOnLikeListener(object : OnLikeListener {
//            override fun liked(likeButton: LikeButton) {
////                TODO(add song to favorites)
//            }
//
//            override fun unLiked(likeButton: LikeButton) {
////                TODO(remove song from favorites)
//            }
//        })


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
            (ScreenSizeUtils.getScreenHeight() * 0.4 / 10).toInt()
        binding.header.requestLayout()


        binding.playerRemote.constraintLayout3.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 2 / 10)
        binding.header.requestLayout()

        binding.playerRemote.shuffleRepeatLayout.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 1 / 10).toInt()
        binding.header.requestLayout()

        binding.header.onCollapse.song_image_on_header.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 3 / 10).toInt()
        binding.header.onCollapse.song_image_on_header.layoutParams.width =
            (ScreenSizeUtils.getScreenWidth() * 1.1 / 10).toInt()
        binding.header.onCollapse.song_image_on_header.requestLayout()

        binding.header.onCollapse.wheelprogress.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 3 / 10).toInt()
        binding.header.onCollapse.wheelprogress.layoutParams.width =
            (ScreenSizeUtils.getScreenWidth() * 1.1 / 10).toInt()
        binding.header.onCollapse.wheelprogress.requestLayout()


//        binding.header.onExpand.back_btn.layoutParams.height = (ScreenSizeUtils.getScreenHeight()*0.6/10).toInt()
//        binding.header.onExpand.back_btn.layoutParams.width = (ScreenSizeUtils.getScreenWidth()*0.6/10).toInt()
//        binding.header.onExpand.back_btn.requestLayout()

        binding.onExpand.likeIv.layoutParams.height =
            (ScreenSizeUtils.getScreenHeight() * 0.7 / 10).toInt()
        binding.onExpand.likeIv.layoutParams.width =
            (ScreenSizeUtils.getScreenWidth() * 0.7 / 10).toInt()
        binding.onExpand.likeIv.requestLayout()

    }

    override fun setDefaultVisibilities() {

        binding.header.onCollapse.pause_btn_on_header.visibility = View.GONE
    }

    fun setOnEventListeners() {
        binding.header.onCollapse.play_btn_on_header.setOnClickListener(this)
        binding.header.onCollapse.pause_btn_on_header.setOnClickListener(this)
        binding.playerRemote.nextBtn?.setOnClickListener(this)
        binding.playerRemote.prevBtn?.setOnClickListener(this)
        binding.playerRemote.playOrPauseLayout?.setOnClickListener(this)
        binding.playerRemote.shuffleContainer?.setOnClickListener(this)
        binding.playerRemote.repeatContainer?.setOnClickListener(this)
        binding.playerRemote.waveformSeekBar?.setOnSeekBarChangeListener(this)
        binding.onExpand.likeIv?.setOnClickListener(this)
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

        if (songIsAlreadyLiked(Coordinator.currentPlayingSong!!)) {
            binding.header.onExpand.like_iv.setImageResource(R.drawable.ic_filled_heart)
        } else {
            binding.header.onExpand.like_iv.setImageResource(R.drawable.ic_heart)
        }

        binding.playerRemote.musicMax?.text =
            Coordinator.currentPlayingSong?.duration?.let {
                TimeUtils.milliSecToDuration(
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

        binding.playerRemote.waveformSeekBar?.setProgressInPercentage(
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
                if (Coordinator != null && Coordinator.isPlaying()) {

                    val mCurrentPosition = Coordinator.getPositionInPlayer() / 1000
                    val duration = Coordinator.currentPlayingSong?.duration?.div(1000)

                    seekTo(mCurrentPosition)
                    setRemainingTime(mCurrentPosition)

                    if (mCurrentPosition == duration?.toInt()?.minus(1) ?: 0) {
                        Coordinator.playNextSong()
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v) {
            binding.onExpand.likeIv -> {
                if (songIsAlreadyLiked(Coordinator.currentPlayingSong!!)) {

                    binding.onExpand.likeIv.setImageResource(R.drawable.ic_heart)
                    DatabaseRepository.deleteSongFromFav(Coordinator.currentPlayingSong!!)


                } else {
                    binding.onExpand.likeIv.setImageResource(R.drawable.ic_filled_heart)

                    DatabaseRepository.addSongAsFav(Coordinator.currentPlayingSong!!.id ?: -1)

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
                if (Coordinator.repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE) {

                    Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL

                    binding.playerPanel.repeatContainer.displayedChild = 1

                    Coordinator.updateNowPlayingQueue()

                } else if (Coordinator.repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL) {

                    Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_ONE

                    binding.playerPanel.repeatContainer.displayedChild = 2

                    Coordinator.updateNowPlayingQueue()

                } else if (Coordinator.repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE) {

                    Coordinator.repeatMode = PlaybackStateCompat.REPEAT_MODE_NONE

                    binding.playerPanel.repeatContainer.displayedChild = 3

                    Coordinator.updateNowPlayingQueue()
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
        binding.playerRemote.musicMin?.text =
            TimeUtils.milliSecToDuration((remainingTimeInPercentage * 1000).toLong())
    }

    fun updateWheelProgress(progressInPercentage: Int) {
        binding.header.onCollapse.wheelprogress.setPercentage(progressInPercentage)
    }

    fun initBinding(view: View) {
        binding = FragmentPlayerPanelBinding.bind(view)
    }

    override fun onProgressInPercentageChanged(
        seekBar: WaveformSeekBar?,
        percent: Float,
        fromUser: Boolean
    ) {

        if (Coordinator.isPlaying()) {

            binding.playerRemote.musicMin?.text = TimeUtils.milliSecToDuration(
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