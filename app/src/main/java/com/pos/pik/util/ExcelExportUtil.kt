package com.pos.pik.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.pos.pik.data.local.CategorySalesReportRow
import com.pos.pik.data.local.ItemSalesReportRow
import com.pos.pik.data.local.SubCategorySalesReportRow
import com.pos.pik.data.local.InventoryIncomingWithDetails
import com.pos.pik.data.local.InventoryDamagedWithDetails
import com.pos.pik.data.local.InventoryInternalUseWithDetails
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

    fun exportIncomingReportToCsv(
        context: Context,
        reportData: List<InventoryIncomingWithDetails>,
        title: String
    ) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val fileName = "Laporan_Barang_Masuk_$timestamp.csv"
        val sb = StringBuilder()
        sb.append("Tanggal,SKU,Produk,Kategori,Sub-Kategori,Jumlah Dus,Fraction,Total Qty,Total Beli,HPP Satuan,Oleh,Catatan\n")
        reportData.forEach { item ->
            sb.append("${item.incDate},")
            sb.append("${item.incPrdSku},")
            sb.append("\"${item.prdName}\",")
            sb.append("\"${item.catName}\",")
            sb.append("\"${item.catSubname}\",")
            sb.append("${item.incPackageQty},")
            sb.append("${item.incFraction},")
            sb.append("${item.incTotalQty},")
            sb.append("${item.incTotalCost},")
            sb.append("${item.incUnitCost},")
            sb.append("\"${item.usrName}\",")
            sb.append("\"${item.incNote ?: ""}\"\n")
        }
        shareCsvFile(context, fileName, sb.toString(), title)
    }

    fun exportDamagedReportToCsv(
        context: Context,
        reportData: List<InventoryDamagedWithDetails>,
        title: String
    ) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val fileName = "Laporan_Barang_Rusak_$timestamp.csv"
        val sb = StringBuilder()
        sb.append("Tanggal,SKU,Produk,Kategori,Sub-Kategori,Qty Rusak,Alasan,Oleh\n")
        reportData.forEach { item ->
            sb.append("${item.dmgDate},")
            sb.append("${item.dmgPrdSku},")
            sb.append("\"${item.prdName}\",")
            sb.append("\"${item.catName}\",")
            sb.append("\"${item.catSubname}\",")
            sb.append("${item.dmgQty},")
            sb.append("\"${item.dmgReason ?: ""}\",")
            sb.append("\"${item.usrName}\"\n")
        }
        shareCsvFile(context, fileName, sb.toString(), title)
    }

    fun exportInternalUseReportToCsv(
        context: Context,
        reportData: List<InventoryInternalUseWithDetails>,
        title: String
    ) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val fileName = "Laporan_Pengeluaran_Khusus_$timestamp.csv"
        val sb = StringBuilder()
        sb.append("Tanggal,SKU,Produk,Kategori,Sub-Kategori,Qty Keluar,Keterangan,Oleh\n")
        reportData.forEach { item ->
            sb.append("${item.useDate},")
            sb.append("${item.usePrdSku},")
            sb.append("\"${item.prdName}\",")
            sb.append("\"${item.catName}\",")
            sb.append("\"${item.catSubname}\",")
            sb.append("${item.useQty},")
            sb.append("\"${item.useNote ?: ""}\",")
            sb.append("\"${item.usrName}\"\n")
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
