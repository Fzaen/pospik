package com.pos.pik.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import androidx.core.content.FileProvider
import com.pos.pik.data.local.AppSettingEntity
import com.pos.pik.data.local.SaleItemWithProduct
import com.pos.pik.data.local.SaleWithUser
import java.io.File
import java.io.FileOutputStream

object PrintHelper {

    fun generateReceiptPdf(
        context: Context,
        sale: SaleWithUser,
        items: List<SaleItemWithProduct>,
        settings: AppSettingEntity,
        isReprint: Boolean = false
    ): File {
        val pdfDocument = PdfDocument()

        val paperSizeMm = settings.setPaperSize
        val marginMm = settings.setMargin

        // 72 points per inch, 1 inch = 25.4 mm
        val pageWidthPoints = (paperSizeMm * 72 / 25.4).toInt()
        val marginPoints = (marginMm * 72 / 25.4).toInt()

        // Calculate dynamic page height based on number of items
        val baseHeightPoints = 200 + (items.size * 25)
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidthPoints, baseHeightPoints, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = if (paperSizeMm == 80) 10f else 8f
            isAntiAlias = true
        }

        val paintBold = Paint(paint).apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val paintCenter = Paint(paint).apply {
            textAlign = Paint.Align.CENTER
        }

        val paintCenterBold = Paint(paintBold).apply {
            textAlign = Paint.Align.CENTER
            textSize = if (paperSizeMm == 80) 12f else 10f
        }

        var y = marginPoints + 15
        val centerX = pageWidthPoints / 2f
        val leftX = marginPoints.toFloat()
        val rightX = (pageWidthPoints - marginPoints).toFloat()

        // Store Name
        canvas.drawText(settings.setWarungName, centerX, y.toFloat(), paintCenterBold)
        y += 15

        // Address & Phone
        paint.textSize = if (paperSizeMm == 80) 8f else 7f
        paintCenter.textSize = if (paperSizeMm == 80) 8f else 7f
        canvas.drawText(settings.setAddress, centerX, y.toFloat(), paintCenter)
        y += 12
        canvas.drawText("Telp: ${settings.setPhone}", centerX, y.toFloat(), paintCenter)
        y += 15

        // Divider
        canvas.drawLine(leftX, y.toFloat(), rightX, y.toFloat(), paint)
        y += 12

        // Invoice No & Date
        val invShort = sale.slsInvoiceNumber.split("-").last()
        canvas.drawText("Inv: $invShort (${sale.usrUsername})", leftX, y.toFloat(), paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(Formatters.formatDateDisplay(sale.slsTransactionDate), rightX, y.toFloat(), paint)
        paint.textAlign = Paint.Align.LEFT
        y += 12

        // Divider
        canvas.drawLine(leftX, y.toFloat(), rightX, y.toFloat(), paint)
        y += 15

        // Items List
        items.forEach { item ->
            val itemLine = "${item.itmQuantity}x ${item.prdName}"
            canvas.drawText(itemLine, leftX, y.toFloat(), paintBold)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(Formatters.formatRupiah(item.itmSubtotal), rightX, y.toFloat(), paint)
            paint.textAlign = Paint.Align.LEFT
            y += 14
        }

        // Divider
        canvas.drawLine(leftX, y.toFloat(), rightX, y.toFloat(), paint)
        y += 15

        // TOTAL
        paintBold.textSize = if (paperSizeMm == 80) 11f else 9f
        canvas.drawText("TOTAL", leftX, y.toFloat(), paintBold)
        paintBold.textAlign = Paint.Align.RIGHT
        canvas.drawText(Formatters.formatRupiah(sale.slsGrandTotal), rightX, y.toFloat(), paintBold)
        paintBold.textAlign = Paint.Align.LEFT
        y += 14

        // Paid & Change
        paint.textSize = if (paperSizeMm == 80) 9f else 8f
        canvas.drawText("Bayar", leftX, y.toFloat(), paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(Formatters.formatRupiah(sale.slsPaidAmount), rightX, y.toFloat(), paint)
        paint.textAlign = Paint.Align.LEFT
        y += 12

        canvas.drawText("Kembali", leftX, y.toFloat(), paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(Formatters.formatRupiah(sale.slsChangeAmount), rightX, y.toFloat(), paint)
        paint.textAlign = Paint.Align.LEFT
        y += 15

        if (isReprint) {
            y += 10
            canvas.drawText("*** REPRINT STRUK ***", centerX, y.toFloat(), paintCenterBold)
        }

        pdfDocument.finishPage(page)

        // Save PDF to cache folder
        val exportsDir = File(context.cacheDir, "exports")
        if (!exportsDir.exists()) exportsDir.mkdirs()

        val pdfFile = File(exportsDir, "Struk_${sale.slsInvoiceNumber}.pdf")
        FileOutputStream(pdfFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return pdfFile
    }

    fun printDirect(
        context: Context,
        sale: SaleWithUser,
        items: List<SaleItemWithProduct>,
        settings: AppSettingEntity,
        isReprint: Boolean = false
    ) {
        val pdfFile = generateReceiptPdf(context, sale, items, settings, isReprint)
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = "Struk-${sale.slsInvoiceNumber}"

        printManager.print(
            jobName,
            object : PrintDocumentAdapter() {
                override fun onLayout(
                    oldAttributes: PrintAttributes?,
                    newAttributes: PrintAttributes?,
                    cancellationSignal: CancellationSignal?,
                    callback: LayoutResultCallback?,
                    extras: Bundle?
                ) {
                    if (cancellationSignal?.isCanceled == true) {
                        callback?.onLayoutCancelled()
                        return
                    }
                    val info = PrintDocumentInfo.Builder(jobName)
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(1)
                        .build()
                    callback?.onLayoutFinished(info, true)
                }

                override fun onWrite(
                    pages: Array<out PageRange>?,
                    destination: ParcelFileDescriptor?,
                    cancellationSignal: CancellationSignal?,
                    callback: WriteResultCallback?
                ) {
                    try {
                        pdfFile.inputStream().use { input ->
                            FileOutputStream(destination?.fileDescriptor).use { output ->
                                input.copyTo(output)
                            }
                        }
                        callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                    } catch (e: Exception) {
                        callback?.onWriteFailed(e.message)
                    }
                }
            },
            null
        )
    }

    fun shareReceipt(
        context: Context,
        sale: SaleWithUser,
        items: List<SaleItemWithProduct>,
        settings: AppSettingEntity,
        isReprint: Boolean = false
    ) {
        val pdfFile = generateReceiptPdf(context, sale, items, settings, isReprint)
        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            pdfFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_SUBJECT, "Struk Belanja - ${settings.setWarungName}")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Struk"))
    }
}
