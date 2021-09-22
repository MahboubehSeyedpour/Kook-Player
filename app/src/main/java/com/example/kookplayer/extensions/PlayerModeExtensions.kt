package com.example.kookplayer.extensions

import com.example.kookplayer.player.PlayerStateRepository

fun PlayerStateRepository.PlayerModes.isShuffle(): Boolean
{
    return PlayerStateRepository.currentPlayerMode == PlayerStateRepository.PlayerModes.SHUFFLE
}

fun PlayerStateRepository.PlayerModes.isRepeatAll(): Boolean
{
    return PlayerStateRepository.currentPlayerMode == PlayerStateRepository.PlayerModes.REPEAT_ALL
}

fun PlayerStateRepository.PlayerModes.isRepeatOne(): Boolean
{
    return PlayerStateRepository.currentPlayerMode == PlayerStateRepository.PlayerModes.REPEAT_ONE
}
