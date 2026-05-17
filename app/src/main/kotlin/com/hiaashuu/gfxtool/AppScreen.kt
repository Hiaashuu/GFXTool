package com.hiaashuu.gfxtool

sealed class AppScreen {

    object Splash : AppScreen()

    object Permission : AppScreen()

    object Home : AppScreen()

    data class GameSettings(val gamePackage: String) : AppScreen()

    data class Backup(val gamePackage: String) : AppScreen()
}