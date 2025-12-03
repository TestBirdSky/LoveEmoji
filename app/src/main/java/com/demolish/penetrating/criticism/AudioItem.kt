package com.demolish.penetrating.criticism

data class AudioItem(
    val name: String,
    val imageResId: Int,
    val audioResId: Int,
    val type: AudioType
)

enum class AudioType {
    GHOST, BOMB, GUN, AIR_HORN, FART
}
