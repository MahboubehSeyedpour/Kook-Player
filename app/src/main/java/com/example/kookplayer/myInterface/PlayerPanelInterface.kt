package com.example.kookplayer.myInterface

import com.example.kookplayer.player.Enums

interface PlayerPanelInterface {
    fun setDefaultVisibilities()
    fun setSongImage()
    fun setSongTitle()

    //    for slidingup panel
    fun getPanelState()
    fun setPanelState()
    fun updateHeader() //if panl is collapsed or expanded


    //    for seekbar
    fun seekTo(mCurrentPosition: Int)
    fun seekbarHandler()
    fun setRemainingTime(remainingTime: Int)


    fun switchPlayPauseButton()  //change play btn to pause and vice versa

    fun updatePanelBasedOnState(newState: Enums.PanelState)


}