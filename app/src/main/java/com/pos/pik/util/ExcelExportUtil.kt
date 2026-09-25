package com.pos.pik.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.pos.pik.data.local.CategorySalesReportRow
import com.pos.pik.data.local.ItemSalesReportRow
import com.pos.pik.data.local.SubCategorySalesReportRow
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExcelExportUtil {

    fun exportItemReportToCsv(
        context: Context,
        reportData: List<ItemSalesReportRow>,
        title: String
    ) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val fileName = "Laporan_Item_$timestamp.csv"
        
        val sb = StringBuilder()
        sb.append("SKU,Produk,Kategori,Sub-Kategori,Qty,Trx,HPP Satuan,Jual Satuan,HPP Total,Jual Total,Margin\n")

        reportData.forEach { row ->
            sb.append("${row.itmSku},")
            sb.append("\"${row.prdName}\",")
            sb.append("\"${row.catName}\",")
            sb.append("\"${row.catSubname}\",")
            sb.append("${row.totalQty},")
            sb.append("${row.totalTrx},")
            sb.append("${row.costPrice.toInt()},")
            sb.append("${row.sellingPrice.toInt()},")
            sb.append("${row.totalCost.toInt()},")
            sb.append("${row.totalSales.toInt()},")
            sb.append("${row.totalProfit.toInt()}\n")
        }

        shareCsvFile(context, fileName, sb.toString(), title)
    }

    fun exportCategoryReportToCsv(
        context: Context,
        reportData: List<CategorySalesReportRow>,
        title: String
    ) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val fileName = "Laporan_Kategori_$timestamp.csv"

        val sb = StringBuilder()
        sb.append("Kategori,Qty,Trx,HPP Total,Jual Total,Margin\n")

        reportData.forEach { row ->
            sb.append("\"${row.catName}\",")
            sb.append("${row.totalQty},")
            sb.append("${row.totalTrx},")
            sb.append("${row.totalCost},")
            sb.append("${row.totalSales},")
            sb.append("${row.totalProfit}\n")
        }

        shareCsvFile(context, fileName, sb.toString(), title)
    }

    fun exportSubCategoryReportToCsv(
        context: Context,
        reportData: List<SubCategorySalesReportRow>,
        title: String
    ) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val fileName = "Laporan_SubKategori_$timestamp.csv"

        val sb = StringBuilder()
        sb.append("Kategori,Sub-Kategori,Qty,Trx,HPP Total,Jual Total,Margin\n")

        reportData.forEach { row ->
            sb.append("\"${row.catName}\",")
            sb.append("\"${row.catSubname}\",")
            sb.append("${row.totalQty},")
            sb.append("${row.totalTrx},")
            sb.append("${row.totalCost},")
            sb.append("${row.totalSales},")
            sb.append("${row.totalProfit}\n")
        }

        shareCsvFile(context, fileName, sb.toString(), title)
    }

    private fun shareCsvFile(context: Context, fileName: String, csvContent: String, title: String) {
        val exportsDir = File(context.cacheDir, "exports")
        if (!exportsDir.exists()) exportsDir.mkdirs()

        val csvFile = File(exportsDir, fileName)
        FileOutputStream(csvFile).use { out ->
            out.write(csvContent.toByteArray(Charsets.UTF_8))
        }

        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            csvFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_SUBJECT, title)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Laporan"))
    }
}
