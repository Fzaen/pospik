package com.pos.pik.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    fun saveImageToInternalStorage(context: Context, uri: Uri): Pair<String?, String?> {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return Pair(null, "Gagal membaca file gambar.")

            val bytes = inputStream.readBytes()
            inputStream.close()

            // Check size max 2 MB
            if (bytes.size > 2 * 1024 * 1024) {
                return Pair(null, "Ukuran gambar terlalu besar! Maksimal 2 MB.")
            }

            val imagesDir = File(context.filesDir, "product_images")
            if (!imagesDir.exists()) imagesDir.mkdirs()

            val fileName = "PRD_${System.currentTimeMillis()}.jpg"
            val destFile = File(imagesDir, fileName)

            FileOutputStream(destFile).use { out ->
                out.write(bytes)
            }

            Pair("file://${destFile.absolutePath}", null)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(null, "Error menyimpan gambar: ${e.localizedMessage}")
        }
    }
}
