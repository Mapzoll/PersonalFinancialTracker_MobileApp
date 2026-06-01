package com.example.projectmap.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.projectmap.data.local.TransactionEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ExportUtils {

    fun exportToCsv(context: Context, transactions: List<TransactionEntity>) {
        val fileName = "ProjectMAP_Report_${System.currentTimeMillis()}.csv"
        val csvHeader = "ID,Type,Amount,Category,Date,Note\n"
        val csvBody = transactions.joinToString("\n") { 
            "${it.transactionId},${it.transactionType},${it.amountOfMoney},${it.category},${formatDate(it.date)},${it.note}"
        }
        
        val file = File(context.cacheDir, "reports/$fileName")
        file.parentFile?.mkdirs()
        file.writeText(csvHeader + csvBody)
        
        shareFile(context, file, "text/csv")
    }

    fun exportToPdf(context: Context, transactions: List<TransactionEntity>) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText("Project MAP - Financial Report", 50f, 50f, paint)

        paint.textSize = 12f
        paint.isFakeBoldText = false
        var yPos = 100f

        canvas.drawText("ID | Type | Amount | Category | Date", 50f, yPos, paint)
        yPos += 20f
        canvas.drawLine(50f, yPos - 10f, 545f, yPos - 10f, paint)

        transactions.take(30).forEach { trans ->
            val text = "${trans.transactionId} | ${trans.transactionType} | ${trans.amountOfMoney} | ${trans.category} | ${formatDate(trans.date)}"
            canvas.drawText(text, 50f, yPos, paint)
            yPos += 20f
        }

        pdfDocument.finishPage(page)

        val fileName = "ProjectMAP_Report_${System.currentTimeMillis()}.pdf"
        val file = File(context.cacheDir, "reports/$fileName")
        file.parentFile?.mkdirs()
        
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        shareFile(context, file, "application/pdf")
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun shareFile(context: Context, file: File, mimeType: String) {
        val uri = FileProvider.getUriForFile(context, "com.example.projectmap.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Report"))
    }
}
