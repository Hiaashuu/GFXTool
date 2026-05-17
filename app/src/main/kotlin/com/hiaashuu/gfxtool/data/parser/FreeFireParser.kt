package com.hiaashuu.gfxtool.data.parser

import com.hiaashuu.gfxtool.data.model.GfxSettings
import java.io.File

object FreeFireParser {

    private val CANDIDATE_FILE_NAMES = listOf(
        "FrSettingNew.ini",
        "FrSetting.ini",
        "setting.ini",
        "config.ini"
    )

    fun findSettingsFile(gameDataPath: String): File? {

        for (name in CANDIDATE_FILE_NAMES) {
            val f = File(gameDataPath, name)
            if (f.exists() && f.canRead()) {
                return f
            }
        }

        val filesSubDir = File(gameDataPath, "files")
        if (filesSubDir.exists()) {
            for (name in CANDIDATE_FILE_NAMES) {
                val f = File(filesSubDir, name)
                if (f.exists() && f.canRead()) {
                    return f
                }
            }
        }
        return null
    }

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

            val graphicsQuality = map["GraphQuality"]?.toIntOrNull()
                ?: map["GraphicsQuality"]?.toIntOrNull()
                ?: 1

            val fpsSetting = map["FPS"]?.toIntOrNull()
                ?: map["FrameRate"]?.toIntOrNull()
                ?: 1

            GfxSettings(
                graphicsQuality = graphicsQuality,
                fpsSetting = fpsSetting
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
                "GraphQuality" to settings.graphicsQuality.toString(),
                "GraphicsQuality" to settings.graphicsQuality.toString(),
                "FPS" to settings.fpsSetting.toString(),
                "FrameRate" to settings.fpsSetting.toString()
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