package com.hiaashuu.gfxtool.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiaashuu.gfxtool.data.model.GameProfile
import com.hiaashuu.gfxtool.data.model.GfxPresets
import com.hiaashuu.gfxtool.data.model.GfxSettings
import com.hiaashuu.gfxtool.data.model.ParserType
import com.hiaashuu.gfxtool.data.model.SupportedGames
import com.hiaashuu.gfxtool.data.model.chipIndexToFpsValue
import com.hiaashuu.gfxtool.data.model.fpsValueToChipIndex
import com.hiaashuu.gfxtool.data.repository.GfxRepository
import com.hiaashuu.gfxtool.ui.components.GlowCard
import com.hiaashuu.gfxtool.ui.components.SettingChipGroup
import com.hiaashuu.gfxtool.ui.components.SettingSlider
import com.hiaashuu.gfxtool.ui.theme.DarkBackground
import com.hiaashuu.gfxtool.ui.theme.DarkSurface
import com.hiaashuu.gfxtool.ui.theme.DarkSurfaceVariant
import com.hiaashuu.gfxtool.ui.theme.SuccessGreen
import com.hiaashuu.gfxtool.ui.theme.WarningOrange
import com.hiaashuu.gfxtool.util.BackupManager
import kotlinx.coroutines.launch

private val QUALITY_LABELS_WITH_OFF = listOf("Off", "Low", "Medium", "High")
private val QUALITY_LABELS_NO_OFF = listOf("Low", "Medium", "High", "Ultra")
private val FPS_LABELS = listOf("30 FPS", "40 FPS", "60 FPS", "90 FPS", "Unlimited")
private val FREE_FIRE_QUALITY_LABELS = listOf("Smooth", "Balanced", "HD", "HDR", "Ultra")
private val FREE_FIRE_FPS_LABELS = listOf("30 FPS", "60 FPS")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GameSettingsScreen(
    gamePackage: String,
    onBack: () -> Unit,
    onOpenBackup: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { GfxRepository() }
    val backupManager = remember { BackupManager(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val game: GameProfile? = remember(gamePackage) {
        SupportedGames.ALL_GAMES.find { it.packageName == gamePackage }
    }

    var isLoading by remember { mutableStateOf(true) }
    var configFound by remember { mutableStateOf(false) }
    var settings by remember { mutableStateOf(GfxSettings()) }
    var isApplying by remember { mutableStateOf(false) }

    LaunchedEffect(gamePackage) {
        if (game != null) {
            configFound = repository.configExists(game)
            val loaded = repository.readGameSettings(game)
            if (loaded != null) {
                settings = loaded
            }
        }
        isLoading = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = game?.name ?: "Settings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Graphics Settings",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenBackup) {
                        Icon(
                            imageVector = Icons.Filled.Backup,
                            contentDescription = "Backups",
                            tint = game?.primaryColor ?: Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                )
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->

        if (game == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Game not found",
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
            return@Scaffold
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = game.primaryColor,
                    modifier = Modifier.size(44.dp)
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                game.primaryColor.copy(alpha = 0.25f),
                                game.accentColor.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = null,
                        tint = game.primaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = game.tagLine,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                        if (configFound) {
                            Text(
                                text = "Config loaded successfully",
                                style = MaterialTheme.typography.labelSmall,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Text(
                                text = "Config file not found",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarningOrange,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                color = game.primaryColor.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            if (!configFound) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = WarningOrange.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = null,
                            tint = WarningOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Config file not found",
                                style = MaterialTheme.typography.labelLarge,
                                color = WarningOrange,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Launch ${game.name} at least once and let it reach the main menu, then return here. The game creates its own config folder on first run.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.6f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader(
                    title = "QUICK PRESETS",
                    icon = Icons.Filled.Settings,
                    tint = game.primaryColor
                )
                Spacer(modifier = Modifier.height(10.dp))
                GlowCard(
                    glowColor = game.primaryColor,
                    backgroundColor = DarkSurface,
                    cornerRadius = 14.dp,
                    borderAlpha = 0.2f
                ) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PresetChip(
                            label = "⚡ Smooth",
                            color = Color(0xFF4CAF50),
                            onClick = { settings = GfxPresets.SMOOTH }
                        )
                        PresetChip(
                            label = "⚖ Balanced",
                            color = game.primaryColor,
                            onClick = { settings = GfxPresets.BALANCED }
                        )
                        PresetChip(
                            label = "🎨 HD",
                            color = Color(0xFF2196F3),
                            onClick = { settings = GfxPresets.HD }
                        )
                        PresetChip(
                            label = "💎 Ultra HD",
                            color = Color(0xFFFF6D00),
                            onClick = { settings = GfxPresets.ULTRA }
                        )
                        PresetChip(
                            label = "🚀 Max FPS",
                            color = Color(0xFFFF1744),
                            onClick = { settings = GfxPresets.MAX_FPS }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (game.parserType) {
                ParserType.INI_UE4 -> {
                    Ue4SettingsSection(
                        settings = settings,
                        accentColor = game.primaryColor,
                        onSettingsChange = { settings = it }
                    )
                }

                ParserType.FREEFIRE_INI -> {
                    FreeFireSettingsSection(
                        settings = settings,
                        accentColor = game.primaryColor,
                        onSettingsChange = { settings = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            isApplying = true

                            val configPath = repository.getConfigFilePath(game)
                            backupManager.createBackup(game, configPath)
                            val result = repository.writeGameSettings(game, settings)
                            isApplying = false
                            if (result) {
                                snackbarHostState.showSnackbar("✅ Settings applied! Auto-backup created.")
                            } else {
                                snackbarHostState.showSnackbar("❌ Failed to apply. Launch the game first.")
                            }
                        }
                    },
                    enabled = configFound && !isApplying,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = game.primaryColor,
                        disabledContainerColor = game.primaryColor.copy(alpha = 0.35f)
                    )
                ) {
                    if (isApplying) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Apply Settings",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                OutlinedButton(
                    onClick = onOpenBackup,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedButtonDefaults.outlinedButtonColors(
                        contentColor = game.primaryColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Manage Backups",
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(32.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun Ue4SettingsSection(
    settings: GfxSettings,
    accentColor: Color,
    onSettingsChange: (GfxSettings) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SectionHeader(
            title = "GRAPHICS SETTINGS",
            icon = Icons.Filled.Tune,
            tint = accentColor
        )

        SettingsCard(accentColor = accentColor) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                SettingSlider(
                    title = "Render Scale",
                    value = settings.resolutionQuality,
                    onValueChange = { onSettingsChange(settings.copy(resolutionQuality = it)) },
                    valueRange = 50f..100f,
                    accentColor = accentColor,
                    valueDisplay = "${settings.resolutionQuality.toInt()}%"
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                SettingChipGroup(
                    title = "Frame Rate",
                    options = FPS_LABELS,
                    selectedIndex = fpsValueToChipIndex(settings.frameRateLimit),
                    onOptionSelected = { idx ->
                        onSettingsChange(settings.copy(frameRateLimit = chipIndexToFpsValue(idx)))
                    },
                    accentColor = accentColor
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                SettingChipGroup(
                    title = "Shadow Quality",
                    options = QUALITY_LABELS_WITH_OFF,
                    selectedIndex = settings.shadowQuality.coerceIn(0, 3),
                    onOptionSelected = { onSettingsChange(settings.copy(shadowQuality = it)) },
                    accentColor = accentColor
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                SettingChipGroup(
                    title = "Texture Quality",
                    options = QUALITY_LABELS_NO_OFF,
                    selectedIndex = settings.textureQuality.coerceIn(0, 3),
                    onOptionSelected = { onSettingsChange(settings.copy(textureQuality = it)) },
                    accentColor = accentColor
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                SettingChipGroup(
                    title = "Effects Quality",
                    options = QUALITY_LABELS_WITH_OFF,
                    selectedIndex = settings.effectsQuality.coerceIn(0, 3),
                    onOptionSelected = { onSettingsChange(settings.copy(effectsQuality = it)) },
                    accentColor = accentColor
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                SettingChipGroup(
                    title = "View Distance",
                    options = QUALITY_LABELS_NO_OFF,
                    selectedIndex = settings.viewDistanceQuality.coerceIn(0, 3),
                    onOptionSelected = {
                        onSettingsChange(settings.copy(viewDistanceQuality = it))
                    },
                    accentColor = accentColor
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                SettingChipGroup(
                    title = "Post Processing",
                    options = QUALITY_LABELS_WITH_OFF,
                    selectedIndex = settings.postProcessQuality.coerceIn(0, 3),
                    onOptionSelected = {
                        onSettingsChange(settings.copy(postProcessQuality = it))
                    },
                    accentColor = accentColor
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                SettingChipGroup(
                    title = "Foliage / Grass",
                    options = QUALITY_LABELS_WITH_OFF,
                    selectedIndex = settings.foliageQuality.coerceIn(0, 3),
                    onOptionSelected = { onSettingsChange(settings.copy(foliageQuality = it)) },
                    accentColor = accentColor
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                tint = accentColor.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Settings take effect on next game launch.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.35f)
            )
        }
    }
}

@Composable
private fun FreeFireSettingsSection(
    settings: GfxSettings,
    accentColor: Color,
    onSettingsChange: (GfxSettings) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SectionHeader(
            title = "GRAPHICS SETTINGS",
            icon = Icons.Filled.Tune,
            tint = accentColor
        )

        SettingsCard(accentColor = accentColor) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                SettingChipGroup(
                    title = "Graphics Quality",
                    options = FREE_FIRE_QUALITY_LABELS,
                    selectedIndex = settings.graphicsQuality.coerceIn(0, 4),
                    onOptionSelected = { onSettingsChange(settings.copy(graphicsQuality = it)) },
                    accentColor = accentColor
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                SettingChipGroup(
                    title = "Frame Rate",
                    options = FREE_FIRE_FPS_LABELS,
                    selectedIndex = settings.fpsSetting.coerceIn(0, 1),
                    onOptionSelected = { onSettingsChange(settings.copy(fpsSetting = it)) },
                    accentColor = accentColor
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                tint = accentColor.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Settings take effect on next game launch.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.35f)
            )
        }
    }
}

@Composable
private fun SettingsCard(
    accentColor: Color,
    content: @Composable () -> Unit
) {
    GlowCard(
        glowColor = accentColor,
        backgroundColor = DarkSurface,
        cornerRadius = 14.dp,
        borderAlpha = 0.18f
    ) {
        content()
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = tint,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun PresetChip(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = color.copy(alpha = 0.15f),
            labelColor = color
        )
    )
}