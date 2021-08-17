package com.example.cassette.`interface`

import com.example.cassette.player.Enums

interface PlayerPanelInterface {
    fun setDefaultVisibilities()
    fun setSongImage()
    fun setSongTitle()

    //    for slidingup panel
    fun getPanelState()
    fun setPanelState()
    fun updateHeader() //if panl is collapsed or expanded


    //    for seekbar
    fun seekTo()
    fun seekbarHandler()
    fun setRemainingTime(remainingTime: Int)


    fun switchPlayPauseButton()  //change play btn to pause and vice versa

    fun updatePanelBasedOnState(newState: Enums.PanelState)


}