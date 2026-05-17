package com.hiaashuu.gfxtool.util

import android.content.pm.PackageManager
import com.hiaashuu.gfxtool.data.model.GameProfile
import com.hiaashuu.gfxtool.data.model.SupportedGames

object GameDetector {

    fun getInstalledGames(packageManager: PackageManager): List<GameProfile> {
        return SupportedGames.ALL_GAMES.filter { game ->
            isPackageInstalled(game.packageName, packageManager)
        }
    }

    fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getGameVersion(packageName: String, packageManager: PackageManager): String? {
        return try {
            val info = packageManager.getPackageInfo(packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}