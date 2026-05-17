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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiaashuu.gfxtool.data.model.SupportedGames
import com.hiaashuu.gfxtool.data.repository.GfxRepository
import com.hiaashuu.gfxtool.ui.components.GlowCard
import com.hiaashuu.gfxtool.ui.theme.DarkBackground
import com.hiaashuu.gfxtool.ui.theme.DarkSurface
import com.hiaashuu.gfxtool.ui.theme.DarkSurfaceVariant
import com.hiaashuu.gfxtool.util.BackupFile
import com.hiaashuu.gfxtool.util.BackupManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    gamePackage: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val backupManager = remember { BackupManager(context) }
    val repository = remember { GfxRepository() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val game = remember(gamePackage) {
        SupportedGames.ALL_GAMES.find { it.packageName == gamePackage }
    }

    var backups by remember { mutableStateOf<List<BackupFile>>(emptyList()) }
    var backupToDelete by remember { mutableStateOf<BackupFile?>(null) }

    fun refreshBackups() {
        backups = backupManager.listBackups(gamePackage)
    }

    LaunchedEffect(gamePackage) {
        refreshBackups()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Backups",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = game?.name ?: gamePackage,
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                )
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Manual backup button
            if (game != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurface)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                val configPath = repository.getConfigFilePath(game)
                                val result = backupManager.createBackup(game, configPath)
                                if (result) {
                                    snackbarHostState.showSnackbar("✅ Backup created successfully!")
                                    refreshBackups()
                                } else {
                                    snackbarHostState.showSnackbar("❌ Backup failed. Config file not found.")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = game.primaryColor
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Backup,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Create Backup Now",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                HorizontalDivider(
                    color = game.primaryColor.copy(alpha = 0.15f),
                    thickness = 1.dp
                )
            }

            if (backups.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = DarkSurfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.FolderOff,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "No Backups Yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Backups are created automatically every time you apply settings, or manually from the button above.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                // Section label
                Text(
                    text = "${backups.size} BACKUP${if (backups.size != 1) "S" else ""}  ·  NEWEST FIRST",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.3f),
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    )
                ) {
                    items(backups, key = { it.file.absolutePath }) { backup ->
                        BackupItem(
                            backup = backup,
                            accentColor = game?.primaryColor ?: Color(0xFF7C4DFF),
                            onRestore = {
                                scope.launch {
                                    if (game != null) {
                                        val destination = repository.getConfigFilePath(game)
                                        val result = backupManager.restoreBackup(backup, destination)
                                        if (result) {
                                            snackbarHostState.showSnackbar("✅ Restored successfully!")
                                        } else {
                                            snackbarHostState.showSnackbar("❌ Restore failed.")
                                        }
                                    }
                                }
                            },
                            onDelete = {
                                backupToDelete = backup
                            }
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .height(24.dp)
                                .navigationBarsPadding()
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (backupToDelete != null) {
        AlertDialog(
            onDismissRequest = { backupToDelete = null },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = "Delete Backup?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will permanently delete the backup from ${backupToDelete?.displayName}. This cannot be undone."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = backupToDelete
                        backupToDelete = null
                        if (target != null) {
                            val deleted = backupManager.deleteBackup(target)
                            refreshBackups()
                            scope.launch {
                                if (deleted) {
                                    snackbarHostState.showSnackbar("Backup deleted.")
                                } else {
                                    snackbarHostState.showSnackbar("Delete failed.")