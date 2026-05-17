package com.hiaashuu.gfxtool.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiaashuu.gfxtool.data.model.GameProfile
import com.hiaashuu.gfxtool.data.repository.GfxRepository
import com.hiaashuu.gfxtool.ui.components.GameCard
import com.hiaashuu.gfxtool.ui.theme.DarkBackground
import com.hiaashuu.gfxtool.ui.theme.DarkSurfaceVariant
import com.hiaashuu.gfxtool.ui.theme.NeonCyan
import com.hiaashuu.gfxtool.ui.theme.NeonPurple
import com.hiaashuu.gfxtool.util.GameDetector

@Composable
fun HomeScreen(
    onGameClick: (String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { GfxRepository() }

    var isLoading by remember { mutableStateOf(true) }
    var installedGames by remember { mutableStateOf<List<GameProfile>>(emptyList()) }
    var configStatus by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var versionMap by remember { mutableStateOf<Map<String, String?>>(emptyMap()) }

    LaunchedEffect(Unit) {
        val detected = GameDetector.getInstalledGames(context.packageManager)
        installedGames = detected

        val statusMap = mutableMapOf<String, Boolean>()
        val versions = mutableMapOf<String, String?>()

        detected.forEach { game ->
            statusMap[game.packageName] = repository.configExists(game)
            versions[game.packageName] = GameDetector.getGameVersion(
                game.packageName,
                context.packageManager
            )
        }

        configStatus = statusMap
        versionMap = versions
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    NeonPurple.copy(alpha = 0.2f),
                                    DarkBackground
                                )
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Column {
                        Text(
                            text = "GFX TOOL",
                            style = TextStyle(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(NeonPurple, NeonCyan)
                                ),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 4.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Graphics Optimizer",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.45f),
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            StatChip(
                                label = "${installedGames.size} Games Detected",
                                color = NeonPurple
                            )
                            val readyCount = configStatus.values.count { it }
                            if (readyCount > 0) {
                                StatChip(
                                    label = "$readyCount Ready",
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "DETECTED GAMES",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.35f),
                    letterSpacing = 3.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = NeonPurple,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            if (!isLoading && installedGames.isNotEmpty()) {
                items(installedGames, key = { it.packageName }) { game ->
                    GameCard(
                        game = game,
                        version = versionMap[game.packageName],
                        configFound = configStatus[game.packageName] ?: false,
                        onClick = { onGameClick(game.packageName) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            if (!isLoading && installedGames.isEmpty()) {
                item {
                    EmptyGamesState()
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height(32.dp)
                        .navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
private fun StatChip(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun EmptyGamesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape = RoundedCornerShape(20.dp),
            color = DarkSurfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Games,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "No Supported Games Found",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.75f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Install PUBG Mobile, BGMI, Free Fire, Free Fire MAX, or COD Mobile to get started.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.4f),
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = NeonCyan.copy(alpha = 0.5f),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "App detects installed games automatically",
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan.copy(alpha = 0.5f)
            )
        }
    }
}