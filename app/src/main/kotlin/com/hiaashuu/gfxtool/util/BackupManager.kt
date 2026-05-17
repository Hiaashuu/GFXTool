package com.hiaashuu.gfxtool.util

import android.content.Context
import com.hiaashuu.gfxtool.data.model.GameProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BackupFile(
    val file: File,
    val gamePkg: String,
    val timestamp: Long,
    val displayName: String,
    val sizeKb: Long
)

class BackupManager(private val context: Context) {

    private fun getBackupDir(gamePkg: String): File {
        val dir = File(context.getExternalFilesDir(null), "GFXBackups/$gamePkg")
        dir.mkdirs()
        return dir
    }

    suspend fun createBackup(game: GameProfile, sourceFilePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val sourceFile = File(sourceFilePath)
                if (!sourceFile.exists()) {
                    return@withContext false
                }

                val timestamp = System.currentTimeMillis()
                val dateStr = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(Date(timestamp))

                val backupDir = getBackupDir(game.packageName)
                val safeName = game.name.replace(" ", "_")
                val destFile = File(backupDir, "${safeName}_${dateStr}.ini.bak")

                sourceFile.copyTo(destFile, overwrite = true)
                destFile.exists()
            } catch (e: Exception) {
                false
            }
        }
    }

    fun listBackups(gamePkg: String): List<BackupFile> {
        val backupDir = getBackupDir(gamePkg)
        if (!backupDir.exists()) {
            return emptyList()
        }

        val displayFormat = SimpleDateFormat("MMM dd, yyyy  HH:mm:ss", Locale.getDefault())

        return backupDir.listFiles()
            ?.filter { it.isFile }
            ?.map { file ->
                val displayName = try {
                    displayFormat.format(Date(file.lastModified()))
                } catch (e: Exception) {
                    file.name
                }
                BackupFile(
                    file = file,
                    gamePkg = gamePkg,
                    timestamp = file.lastModified(),
                    displayName = displayName,
                    sizeKb = file.length() / 1024
                )
            }
            ?.sortedByDescending { it.timestamp }
            ?: emptyList()
    }

    suspend fun restoreBackup(backupFile: BackupFile, destinationPath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val destFile = File(destinationPath)
                val parentDir = destFile.parentFile
                if (parentDir == null || !parentDir.exists()) {
                    return@withContext false
                }
                backupFile.file.copyTo(destFile, overwrite = true)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    fun deleteBackup(backupFile: BackupFile): Boolean {
        return try {
            backupFile.file.delete()
        } catch (e: Exception) {
            false
        }
    }

    fun getBackupCount(gamePkg: String): Int {
        return listBackups(gamePkg).size
    }
}