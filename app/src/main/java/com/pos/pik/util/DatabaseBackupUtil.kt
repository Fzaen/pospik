package com.pos.pik.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.pos.pik.data.local.AppDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DatabaseBackupUtil {

    fun performBackup(context: Context): Boolean {
        return try {
            val db = AppDatabase.getDatabase(context)
            try {
                db.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val dbFile = context.getDatabasePath("pos_pik_database.db")
            if (!dbFile.exists()) return false

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
            val backupFileName = "POS_PIK_Backup_$timestamp.db"

            val exportsDir = File(context.cacheDir, "backups")
            if (!exportsDir.exists()) exportsDir.mkdirs()

            val backupFile = File(exportsDir, backupFileName)
            FileInputStream(dbFile).use { input ->
                FileOutputStream(backupFile).use { output ->
                    input.copyTo(output)
                }
            }

            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                backupFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, "Backup Database POS PIK ($timestamp)")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Kirim / Simpan File Backup Database (.db)"))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun performRestore(context: Context, uri: Uri): Boolean {
        return try {
            AppDatabase.closeAndResetDatabase()

            val dbFile = context.getDatabasePath("pos_pik_database.db")
            val dbShm = context.getDatabasePath("pos_pik_database.db-shm")
            val dbWal = context.getDatabasePath("pos_pik_database.db-wal")

            if (dbShm.exists()) dbShm.delete()
            if (dbWal.exists()) dbWal.delete()

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            } ?: return false

            AppDatabase.getDatabase(context)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
