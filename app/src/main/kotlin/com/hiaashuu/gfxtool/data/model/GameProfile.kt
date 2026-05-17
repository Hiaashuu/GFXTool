package com.hiaashuu.gfxtool.data.model

import androidx.compose.ui.graphics.Color
import com.hiaashuu.gfxtool.ui.theme.BgmiBlue
import com.hiaashuu.gfxtool.ui.theme.BgmiBlueGlow
import com.hiaashuu.gfxtool.ui.theme.BgmiOrange
import com.hiaashuu.gfxtool.ui.theme.CodDarkAccent
import com.hiaashuu.gfxtool.ui.theme.CodGreen
import com.hiaashuu.gfxtool.ui.theme.CodGreenGlow
import com.hiaashuu.gfxtool.ui.theme.FreeFireGold
import com.hiaashuu.gfxtool.ui.theme.FreeFireMaxAmber
import com.hiaashuu.gfxtool.ui.theme.FreeFireMaxRed
import com.hiaashuu.gfxtool.ui.theme.FreeFireMaxRedGlow
import com.hiaashuu.gfxtool.ui.theme.FreeFireOrange
import com.hiaashuu.gfxtool.ui.theme.FreeFireOrangeGlow
import com.hiaashuu.gfxtool.ui.theme.PubgGold
import com.hiaashuu.gfxtool.ui.theme.PubgGoldGlow
import com.hiaashuu.gfxtool.ui.theme.PubgOlive

enum class ParserType {
    INI_UE4,
    FREEFIRE_INI
}

data class GameProfile(
    val name: String,
    val packageName: String,
    val configRelativePath: String,
    val parserType: ParserType,
    val primaryColor: Color,
    val accentColor: Color,
    val glowColor: Color,
    val tagLine: String
)

object SupportedGames {

    val PUBG_MOBILE = GameProfile(
        name = "PUBG Mobile",
        packageName = "com.tencent.ig",
        configRelativePath = "files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/GameUserSettings.ini",
        parserType = ParserType.INI_UE4,
        primaryColor = PubgGold,
        accentColor = PubgOlive,
        glowColor = PubgGoldGlow,
        tagLine = "Battle Royale · Tencent"
    )

    val BGMI = GameProfile(
        name = "BGMI",
        packageName = "com.pubg.krmobile",
        configRelativePath = "files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/GameUserSettings.ini",
        parserType = ParserType.INI_UE4,
        primaryColor = BgmiBlue,
        accentColor = BgmiOrange,
        glowColor = BgmiBlueGlow,
        tagLine = "Battle Royale · Krafton"
    )

    val FREE_FIRE = GameProfile(
        name = "Free Fire",
        packageName = "com.dts.freefireth",
        configRelativePath = "files/FrSettingNew.ini",
        parserType = ParserType.FREEFIRE_INI,
        primaryColor = FreeFireOrange,
        accentColor = FreeFireGold,
        glowColor = FreeFireOrangeGlow,
        tagLine = "Battle Royale · Garena"
    )

    val FREE_FIRE_MAX = GameProfile(
        name = "Free Fire MAX",
        packageName = "com.dts.freefiremax",
        configRelativePath = "files/FrSettingNew.ini",
        parserType = ParserType.FREEFIRE_INI,
        primaryColor = FreeFireMaxRed,
        accentColor = FreeFireMaxAmber,
        glowColor = FreeFireMaxRedGlow,
        tagLine = "Battle Royale MAX · Garena"
    )

    val COD_MOBILE = GameProfile(
        name = "COD Mobile",
        packageName = "com.activision.callofduty.shooter",
        configRelativePath = "files/UE4Game/CODMobile/CODMobile/Saved/Config/Android/GameUserSettings.ini",
        parserType = ParserType.INI_UE4,
        primaryColor = CodGreen,
        accentColor = CodDarkAccent,
        glowColor = CodGreenGlow,
        tagLine = "FPS · Battle Royale · Activision"
    )

    val ALL_GAMES = listOf(PUBG_MOBILE, BGMI, FREE_FIRE, FREE_FIRE_MAX, COD_MOBILE)
}