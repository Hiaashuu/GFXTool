package com.hiaashuu.gfxtool.data.model

data class GfxSettings(

    val resolutionQuality: Float = 100f,
    val viewDistanceQuality: Int = 1,
    val shadowQuality: Int = 0,
    val postProcessQuality: Int = 1,
    val textureQuality: Int = 1,
    val effectsQuality: Int = 1,
    val foliageQuality: Int = 0,
    val frameRateLimit: Float = 0f,
    val resolutionX: Int = 1280,
    val resolutionY: Int = 720,

    val graphicsQuality: Int = 1,
    val fpsSetting: Int = 1
)

fun fpsValueToChipIndex(fps: Float): Int {
    return when (fps) {
        30f -> 0
        40f -> 1
        60f -> 2
        90f -> 3
        else -> 4
    }
}

fun chipIndexToFpsValue(index: Int): Float {
    return when (index) {
        0 -> 30f
        1 -> 40f
        2 -> 60f
        3 -> 90f
        else -> 0f
    }
}

object GfxPresets {

    val SMOOTH = GfxSettings(
        resolutionQuality = 70f,
        viewDistanceQuality = 0,
        shadowQuality = 0,
        postProcessQuality = 0,
        textureQuality = 0,
        effectsQuality = 0,
        foliageQuality = 0,
        frameRateLimit = 60f,
        graphicsQuality = 0,
        fpsSetting = 1
    )

    val BALANCED = GfxSettings(
        resolutionQuality = 85f,
        viewDistanceQuality = 1,
        shadowQuality = 1,
        postProcessQuality = 1,
        textureQuality = 1,
        effectsQuality = 1,
        foliageQuality = 0,
        frameRateLimit = 60f,
        graphicsQuality = 1,
        fpsSetting = 1
    )

    val HD = GfxSettings(
        resolutionQuality = 100f,
        viewDistanceQuality = 2,
        shadowQuality = 2,
        postProcessQuality = 2,
        textureQuality = 2,
        effectsQuality = 2,
        foliageQuality = 1,
        frameRateLimit = 40f,
        graphicsQuality = 2,
        fpsSetting = 1
    )

    val ULTRA = GfxSettings(
        resolutionQuality = 100f,
        viewDistanceQuality = 3,
        shadowQuality = 3,
        postProcessQuality = 3,
        textureQuality = 3,
        effectsQuality = 3,
        foliageQuality = 2,
        frameRateLimit = 30f,
        graphicsQuality = 4,
        fpsSetting = 0
    )

    val MAX_FPS = GfxSettings(
        resolutionQuality = 60f,
        viewDistanceQuality = 0,
        shadowQuality = 0,
        postProcessQuality = 0,
        textureQuality = 0,
        effectsQuality = 0,
        foliageQuality = 0,
        frameRateLimit = 90f,
        graphicsQuality = 0,
        fpsSetting = 1
    )
}