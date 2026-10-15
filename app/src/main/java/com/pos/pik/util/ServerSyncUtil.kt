package com.pos.pik.util

import android.content.Context
import com.pos.pik.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ServerSyncUtil {

    suspend fun syncPushMasterData(context: Context, serverUrl: String, tenantId: String): Pair<Boolean, String> {
        return withContext(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(context)
                val categories = db.categoryDao().getAllCategoriesSync()
                val products = db.productDao().getAllProductsSync()

                val jsonBuilder = StringBuilder()
                jsonBuilder.append("{")
                jsonBuilder.append("\"tenant_id\":\"$tenantId\",")
                jsonBuilder.append("\"categories\":[")
                categories.forEachIndexed { index, cat ->
                    if (index > 0) jsonBuilder.append(",")
                    jsonBuilder.append("{\"cat_id\":${cat.catId},\"cat_name\":\"${cat.catName}\",\"cat_subname\":\"${cat.catSubname}\"}")
                }
                jsonBuilder.append("],")
                jsonBuilder.append("\"products\":[")
                products.forEachIndexed { index, p ->
                    if (index > 0) jsonBuilder.append(",")
                    jsonBuilder.append("{\"sku\":\"${p.prdSku}\",\"category_id\":${p.prdCategoryId},\"name\":\"${p.prdName}\",\"cost_price\":${p.prdCostPrice},\"selling_price\":${p.prdSellingPrice},\"image\":\"${p.prdImage ?: ""}\"}")
                }
                jsonBuilder.append("]")
                jsonBuilder.append("}")

                val urlObj = URL("$serverUrl/api/v1/sync/master")
                val conn = (urlObj.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    setRequestProperty("X-Tenant-ID", tenantId)
                    doOutput = true
                    connectTimeout = 10000
                    readTimeout = 10000
                }

                OutputStreamWriter(conn.outputStream, "UTF-8").use { writer ->
                    writer.write(jsonBuilder.toString())
                    writer.flush()
                }

                val responseCode = conn.responseCode
                if (responseCode in 200..299) {
                    Pair(true, "Sinkronisasi Master Berhasil Dikirim ke Server!")
                } else {
                    val errorMsg = try {
                        BufferedReader(InputStreamReader(conn.errorStream)).use { it.readText() }
                    } catch (e: Exception) {
                        "HTTP Error Code: $responseCode"
                    }
                    Pair(false, "Gagal Sync: $errorMsg")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Pair(false, "Error Koneksi Server: ${e.localizedMessage}")
            }
        }
    }
}
