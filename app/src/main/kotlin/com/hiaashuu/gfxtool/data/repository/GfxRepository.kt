package com.hiaashuu.gfxtool.data.repository

import android.os.Environment
import com.hiaashuu.gfxtool.data.model.GameProfile
import com.hiaashuu.gfxtool.data.model.GfxSettings
import com.hiaashuu.gfxtool.data.model.ParserType
import com.hiaashuu.gfxtool.data.parser.FreeFireParser
import com.hiaashuu.gfxtool.data.parser.IniParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class GfxRepository {

    fun getConfigFilePath(game: GameProfile): String {
        val sdcard = Environment.getExternalStorageDirectory().absolutePath
        return "$sdcard/Android/data/${game.packageName}/${game.configRelativePath}"
    }

    fun getGameDataPath(game: GameProfile): String {
        val sdcard = Environment.getExternalStorageDirectory().absolutePath
        return "$sdcard/Android/data/${game.packageName}"
    }

    fun configExists(game: GameProfile): Boolean {
        val path = getConfigFilePath(game)
        val file = File(path)
        return file.exists() && file.canRead()
    }

    fun gameDataDirExists(game: GameProfile): Boolean {
        val path = getGameDataPath(game)
        return File(path).exists()
    }

    suspend fun readGameSettings(game: GameProfile): GfxSettings? {
        return withContext(Dispatchers.IO) {
            when (game.parserType) {
                ParserType.INI_UE4 -> {
                    val path = getConfigFilePath(game)
                    IniParser.readSettings(path)
                }

                ParserType.FREEFIRE_INI -> {
                    val configPath = getConfigFilePath(game)
                    val configFile = File(configPath)
                    if (configFile.exists()) {
                        FreeFireParser.readSettings(configPath)
                    } else {
                        val gameDataPath = getGameDataPath(game)
                        val found = FreeFireParser.findSettingsFile(gameDataPath)
                        if (found != null) {
                            FreeFireParser.readSettings(found.absolutePath)
                        } else {
                            null
                        }
                    }
                }
            }
        }
    }

    suspend fun writeGameSettings(game: GameProfile, settings: GfxSettings): Boolean {
        return withContext(Dispatchers.IO) {
            when (game.parserType) {
                ParserType.INI_UE4 -> {
                    val path = getConfigFilePath(game)
                    IniParser.writeSettings(path, settings)
                }

                ParserType.FREEFIRE_INI -> {
                    val configPath = getConfigFilePath(game)
                    val configFile = File(configPath)
                    if (configFile.exists()) {
                        FreeFireParser.writeSettings(configPath, settings)
                    } else {
                        val gameDataPath = getGameDataPath(game)
                        val found = FreeFireParser.findSettingsFile(gameDataPath)
                        if (found != null) {
                            FreeFireParser.writeSettings(found.absolutePath, settings)
                        } else {
                            false
                        }
                    }
                }
            }
        }
    }
}