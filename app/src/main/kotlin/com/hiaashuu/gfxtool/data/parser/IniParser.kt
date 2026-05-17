package com.hiaashuu.gfxtool.data.parser

import com.hiaashuu.gfxtool.data.model.GfxSettings
import java.io.File

object IniParser {

    fun readSettings(filePath: String): GfxSettings? {
        return try {
            val file = File(filePath)
            if (!file.exists() || !file.canRead()) {
                return null
            }

            val map = mutableMapOf<String, String>()
            file.forEachLine { line ->
                val trimmed = line.trim()
                if (trimmed.startsWith("[") || trimmed.startsWith(";") || trimmed.isEmpty()) {
                    return@forEachLine
                }
                val eqIndex = trimmed.indexOf('=')
                if (eqIndex > 0) {
                    val key = trimmed.substring(0, eqIndex).trim()
                    val value = trimmed.substring(eqIndex + 1).trim()
                    map[key] = value
                }
            }

            GfxSettings(
                resolutionQuality = map["sg.ResolutionQuality"]?.toFloatOrNull() ?: 100f,
                viewDistanceQuality = map["sg.ViewDistanceQuality"]?.toIntOrNull() ?: 1,
                shadowQuality = map["sg.ShadowQuality"]?.toIntOrNull() ?: 0,
                postProcessQuality = map["sg.PostProcessQuality"]?.toIntOrNull() ?: 1,
                textureQuality = map["sg.TextureQuality"]?.toIntOrNull() ?: 1,
                effectsQuality = map["sg.EffectsQuality"]?.toIntOrNull() ?: 1,
                foliageQuality = map["sg.FoliageQuality"]?.toIntOrNull() ?: 0,
                frameRateLimit = map["FrameRateLimit"]?.toFloatOrNull() ?: 0f,
                resolutionX = map["ResolutionSizeX"]?.toIntOrNull() ?: 1280,
                resolutionY = map["ResolutionSizeY"]?.toIntOrNull() ?: 720
            )
        } catch (e: Exception) {
            null
        }
    }

    fun writeSettings(filePath: String, settings: GfxSettings): Boolean {
        return try {
            val file = File(filePath)
            if (!file.exists() || !file.canWrite()) {
                return false
            }

            val newValues = mapOf(
                "sg.ResolutionQuality" to "%.6f".format(settings.resolutionQuality),
                "sg.ViewDistanceQuality" to settings.viewDistanceQuality.toString(),
                "sg.ShadowQuality" to settings.shadowQuality.toString(),
                "sg.PostProcessQuality" to settings.postProcessQuality.toString(),
                "sg.TextureQuality" to settings.textureQuality.toString(),
                "sg.EffectsQuality" to settings.effectsQuality.toString(),
                "sg.FoliageQuality" to settings.foliageQuality.toString(),
                "FrameRateLimit" to "%.6f".format(settings.frameRateLimit),
                "ResolutionSizeX" to settings.resolutionX.toString(),
                "ResolutionSizeY" to settings.resolutionY.toString()
            )

            val originalLines = file.readLines()
            val updatedLines = originalLines.map { line ->
                val trimmed = line.trim()
                if (trimmed.startsWith("[") || trimmed.startsWith(";") || trimmed.isEmpty()) {
                    return@map line
                }
                val eqIndex = trimmed.indexOf('=')
                if (eqIndex > 0) {
                    val key = trimmed.substring(0, eqIndex).trim()
                    val replacement = newValues[key]
                    if (replacement != null) {
                        return@map "$key=$replacement"
                    }
                }
                return@map line
            }

            file.writeText(updatedLines.joinToString("\n"))
            true
        } catch (e: Exception) {
            false
        }
    }
}