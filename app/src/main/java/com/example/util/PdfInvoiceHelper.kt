package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.OrderEntity
import com.example.model.CartItem
import com.example.model.PaymentMethod
import com.example.model.SaleReceipt
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfInvoiceHelper {

    private const val TAG = "PdfInvoiceHelper"

    // Page dimensions in Points (A4: 595 x 842 pt at 72 DPI)
    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 36f

    // Theme Colors
    private val COLOR_ROSE_GOLD = Color.rgb(183, 110, 121)       // #B76E79
    private val COLOR_ROSE_DARK = Color.rgb(142, 75, 85)         // #8E4B55
    private val COLOR_CHAMPAGNE = Color.rgb(212, 175, 55)        // #D4AF37
    private val COLOR_CHARCOAL = Color.rgb(43, 35, 38)           // #2B2326
    private val COLOR_CHARCOAL_MUTED = Color.rgb(109, 95, 99)    // #6D5F63
    private val COLOR_SOFT_BLUSH = Color.rgb(255, 244, 246)      // #FFF4F6
    private val COLOR_BORDER = Color.rgb(234, 216, 220)          // #EAD8DC
    private val COLOR_SUCCESS_GREEN = Color.rgb(46, 125, 50)     // #2E7D32
    private val COLOR_DANGER_RED = Color.rgb(198, 40, 40)        // #C62828

    /**
     * Generates a PDF invoice file from a SaleReceipt.
     */
    fun generateInvoicePdf(context: Context, receipt: SaleReceipt): File {
        val invoicesDir = File(context.cacheDir, "invoices").apply {
            if (!exists()) mkdirs()
        }
        val file = File(invoicesDir, "Invoice_HE_${receipt.orderNumber}.pdf")

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        drawInvoiceContent(
            canvas = canvas,
            orderNumber = receipt.orderNumber,
            dateStr = receipt.formattedDate,
            cashierName = receipt.cashierName,
            paymentMethod = receipt.paymentMethod.titleAr,
            items = receipt.items,
            subtotal = receipt.subtotal,
            discount = receipt.discount,
            netTotal = receipt.totalAmount,
            isCash = receipt.paymentMethod == PaymentMethod.CASH,
            cashPaid = receipt.cashPaid,
            changeAmount = receipt.changeAmount,
            storeName = receipt.storeName,
            storePhone = receipt.storePhone
        )

        pdfDocument.finishPage(page)

        try {
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to write invoice PDF", e)
            throw e
        } finally {
            pdfDocument.close()
        }

        return file
    }

    /**
     * Generates and exports the final PDF invoice, opening the Android share sheet
     * to share it directly via messaging apps (WhatsApp, Telegram, etc.)
     */
    fun exportAndSharePdfInvoice(context: Context, receipt: SaleReceipt) {
        try {
            val pdfFile = generateInvoicePdf(context, receipt)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "فاتورة مبيعات متجر H&E Store #${receipt.orderNumber}")
                val messageText = buildString {
                    appendLine("✨ *فاتورة مبيعات - ${receipt.storeName}* ✨")
                    appendLine("رقم الفاتورة: #${receipt.orderNumber}")
                    appendLine("التاريخ: ${receipt.formattedDate}")
                    appendLine("الكاشير: ${receipt.cashierName}")
                    appendLine("الإجمالي الصافي: ${String.format(Locale.US, "%.2f", receipt.totalAmount)} ج.م")
                    appendLine("طريقة الدفع: ${receipt.paymentMethod.titleAr}")
                    appendLine("━━━━━━━━━━━━━━━━━━━")
                    appendLine("مرفق ملف الفاتورة الرسمي كـ PDF قابل للطباعة والحفظ.")
                    appendLine("شكراً لزيارتكم متجرنا! 💕")
                }
                putExtra(Intent.EXTRA_TEXT, messageText)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "مشاركة الفاتورة عبر تطبيقات المراسلة:")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            Toast.makeText(context, "تم تجهيز فاتورة PDF للمشاركة بنجاح ✨", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting and sharing PDF invoice", e)
            Toast.makeText(context, "فشل في تصدير الفاتورة كملف PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Generates and exports a PDF for an existing OrderEntity (from sales logs).
     */
    fun exportAndShareOrderPdf(context: Context, order: OrderEntity) {
        try {
            val sdf = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar"))
            val formattedDate = sdf.format(Date(order.timestamp))
            val paymentMethodObj = PaymentMethod.fromCode(order.paymentMethod)

            val invoicesDir = File(context.cacheDir, "invoices").apply {
                if (!exists()) mkdirs()
            }
            val file = File(invoicesDir, "Invoice_HE_${order.orderNumber}.pdf")

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            drawOrderContent(
                canvas = canvas,
                order = order,
                dateStr = formattedDate,
                paymentMethodTitle = paymentMethodObj.titleAr,
                isCash = paymentMethodObj == PaymentMethod.CASH
            )

            pdfDocument.finishPage(page)

            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "فاتورة مبيعات H&E Store #${order.orderNumber}")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "مرفق ملف فاتورة المبيعات الرسمية #${order.orderNumber} بمبلغ ${String.format(Locale.US, "%.2f", order.netAmount)} ج.م من متجر H&E Store."
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "مشاركة الفاتورة عبر تطبيقات المراسلة:")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            Toast.makeText(context, "تم تجهيز فاتورة PDF للطلب #${order.orderNumber} بنجاح ✨", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting Order PDF", e)
            Toast.makeText(context, "فشل تصدير الفاتورة: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun drawInvoiceContent(
        canvas: Canvas,
        orderNumber: String,
        dateStr: String,
        cashierName: String,
        paymentMethod: String,
        items: List<CartItem>,
        subtotal: Double,
        discount: Double,
        netTotal: Double,
        isCash: Boolean,
        cashPaid: Double,
        changeAmount: Double,
        storeName: String,
        storePhone: String
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

        val contentWidth = PAGE_WIDTH - (MARGIN * 2)

        // 1. Background Canvas
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), paint)

        // 2. Top Header Ribbon / Card (y=30 to y=115)
        val headerRect = RectF(MARGIN, 30f, PAGE_WIDTH - MARGIN, 115f)
        paint.color = COLOR_SOFT_BLUSH
        canvas.drawRoundRect(headerRect, 16f, 16f, paint)

        paint.color = COLOR_ROSE_GOLD
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f
        canvas.drawRoundRect(headerRect, 16f, 16f, paint)
        paint.style = Paint.Style.FILL

        // Header Title (Store Name)
        textPaint.color = COLOR_ROSE_DARK
        textPaint.textSize = 24f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, storeName, MARGIN + 16f, 42f, (contentWidth - 32).toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)

        // Header Subtitle
        textPaint.color = COLOR_CHAMPAGNE
        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "بوتيك مستحضرات التجميل والعناية الفاخرة • فاتورة مبيعات معتمدة", MARGIN + 16f, 72f, (contentWidth - 32).toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)

        // Contact & Location
        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "خدمة العملاء: $storePhone | القاهرة، مصر", MARGIN + 16f, 92f, (contentWidth - 32).toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)

        // 3. Invoice Metadata Card (y=125 to y=185)
        val metaRect = RectF(MARGIN, 125f, PAGE_WIDTH - MARGIN, 185f)
        paint.color = Color.rgb(250, 250, 252)
        canvas.drawRoundRect(metaRect, 10f, 10f, paint)

        paint.color = COLOR_BORDER
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(metaRect, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        // Column 1 (Right): Invoice Number & Date
        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "رقم الفاتورة:", PAGE_WIDTH - MARGIN - 120f, 134f, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        textPaint.color = COLOR_ROSE_DARK
        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "#$orderNumber", PAGE_WIDTH - MARGIN - 260f, 133f, 140, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "تاريخ ووقت الإصدار:", PAGE_WIDTH - MARGIN - 120f, 158f, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        textPaint.color = COLOR_CHARCOAL
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, dateStr, PAGE_WIDTH - MARGIN - 260f, 158f, 140, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        // Column 2 (Left): Cashier & Payment Method
        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "مسؤول الكاشير:", MARGIN + 12f, 134f, 80, textPaint, Layout.Alignment.ALIGN_NORMAL)

        textPaint.color = COLOR_CHARCOAL
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, cashierName, MARGIN + 95f, 134f, 120, textPaint, Layout.Alignment.ALIGN_NORMAL)

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "طريقة السداد:", MARGIN + 12f, 158f, 80, textPaint, Layout.Alignment.ALIGN_NORMAL)

        textPaint.color = COLOR_SUCCESS_GREEN
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, paymentMethod, MARGIN + 95f, 158f, 120, textPaint, Layout.Alignment.ALIGN_NORMAL)

        // 4. Table Header (y=195 to y=220)
        val tableTop = 195f
        val headerHeight = 25f
        val tableHeaderRect = RectF(MARGIN, tableTop, PAGE_WIDTH - MARGIN, tableTop + headerHeight)
        paint.color = COLOR_ROSE_GOLD
        canvas.drawRoundRect(tableHeaderRect, 6f, 6f, paint)

        // Column layout: Total(90) | UnitPrice(85) | Qty(50) | ItemName(250) | Num(40)
        val colNumX = MARGIN + 8f
        val colItemX = MARGIN + 40f
        val colQtyX = MARGIN + 290f
        val colPriceX = MARGIN + 345f
        val colTotalX = MARGIN + 430f

        textPaint.color = Color.WHITE
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        drawTextAligned(canvas, "م", colNumX, tableTop + 6f, 30, textPaint, Layout.Alignment.ALIGN_CENTER)
        drawTextAligned(canvas, "الصنف / المنتج", colItemX, tableTop + 6f, 240, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
        drawTextAligned(canvas, "الكمية", colQtyX, tableTop + 6f, 50, textPaint, Layout.Alignment.ALIGN_CENTER)
        drawTextAligned(canvas, "سعر الوحدة", colPriceX, tableTop + 6f, 80, textPaint, Layout.Alignment.ALIGN_CENTER)
        drawTextAligned(canvas, "الإجمالي", colTotalX, tableTop + 6f, 80, textPaint, Layout.Alignment.ALIGN_CENTER)

        // 5. Table Rows
        var currentY = tableTop + headerHeight + 4f
        val rowHeight = 24f

        items.forEachIndexed { index, item ->
            // Zebra background
            if (index % 2 == 1) {
                paint.color = COLOR_SOFT_BLUSH
                canvas.drawRect(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + rowHeight, paint)
            }

            // Divider line
            paint.color = COLOR_BORDER
            paint.strokeWidth = 0.5f
            canvas.drawLine(MARGIN, currentY + rowHeight, PAGE_WIDTH - MARGIN, currentY + rowHeight, paint)

            textPaint.color = COLOR_CHARCOAL_MUTED
            textPaint.textSize = 9f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            drawTextAligned(canvas, "${index + 1}", colNumX, currentY + 5f, 30, textPaint, Layout.Alignment.ALIGN_CENTER)

            textPaint.color = COLOR_CHARCOAL
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            drawTextAligned(canvas, item.product.name, colItemX, currentY + 5f, 240, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

            textPaint.color = COLOR_CHARCOAL
            textPaint.textSize = 9.5f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            drawTextAligned(canvas, "${item.quantity}", colQtyX, currentY + 5f, 50, textPaint, Layout.Alignment.ALIGN_CENTER)

            drawTextAligned(canvas, "${String.format(Locale.US, "%.2f", item.unitPrice)} ج.م", colPriceX, currentY + 5f, 80, textPaint, Layout.Alignment.ALIGN_CENTER)

            textPaint.color = COLOR_ROSE_DARK
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            drawTextAligned(canvas, "${String.format(Locale.US, "%.2f", item.totalPrice)} ج.م", colTotalX, currentY + 5f, 80, textPaint, Layout.Alignment.ALIGN_CENTER)

            currentY += rowHeight
        }

        // 6. Summary Totals Box (Right bottom of table)
        currentY += 14f
        val summaryBoxTop = currentY
        val summaryWidth = 240f
        val summaryLeft = PAGE_WIDTH - MARGIN - summaryWidth
        val summaryHeight = if (isCash && cashPaid > 0) 125f else 95f

        val summaryRect = RectF(summaryLeft, summaryBoxTop, PAGE_WIDTH - MARGIN, summaryBoxTop + summaryHeight)
        paint.color = Color.rgb(253, 252, 253)
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(summaryRect, 10f, 10f, paint)

        paint.color = COLOR_BORDER
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(summaryRect, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        var lineY = summaryBoxTop + 8f

        // Subtotal
        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "المجموع الفرعي:", summaryLeft + 120f, lineY, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
        textPaint.color = COLOR_CHARCOAL
        drawTextAligned(canvas, "${String.format(Locale.US, "%.2f", subtotal)} ج.م", summaryLeft + 10f, lineY, 105, textPaint, Layout.Alignment.ALIGN_NORMAL)

        lineY += 18f
        // Discount (if any)
        if (discount > 0) {
            textPaint.color = COLOR_DANGER_RED
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            drawTextAligned(canvas, "قيمة الخصم:", summaryLeft + 120f, lineY, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            drawTextAligned(canvas, "-${String.format(Locale.US, "%.2f", discount)} ج.م", summaryLeft + 10f, lineY, 105, textPaint, Layout.Alignment.ALIGN_NORMAL)
            lineY += 18f
        }

        // Net Total Ribbon
        val netRect = RectF(summaryLeft + 6f, lineY, PAGE_WIDTH - MARGIN - 6f, lineY + 26f)
        paint.color = COLOR_ROSE_GOLD
        canvas.drawRoundRect(netRect, 6f, 6f, paint)

        textPaint.color = Color.WHITE
        textPaint.textSize = 11f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "الصافي النهائي:", summaryLeft + 115f, lineY + 5f, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
        drawTextAligned(canvas, "${String.format(Locale.US, "%.2f", netTotal)} ج.م", summaryLeft + 12f, lineY + 5f, 105, textPaint, Layout.Alignment.ALIGN_NORMAL)

        lineY += 32f

        // Cash Details (if cash)
        if (isCash && cashPaid > 0) {
            textPaint.color = COLOR_CHARCOAL_MUTED
            textPaint.textSize = 9.5f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            drawTextAligned(canvas, "المدفوع نقداً:", summaryLeft + 120f, lineY, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
            textPaint.color = COLOR_CHARCOAL
            drawTextAligned(canvas, "${String.format(Locale.US, "%.2f", cashPaid)} ج.م", summaryLeft + 10f, lineY, 105, textPaint, Layout.Alignment.ALIGN_NORMAL)

            lineY += 16f
            textPaint.color = COLOR_CHARCOAL
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            drawTextAligned(canvas, "الباقي للعميل:", summaryLeft + 120f, lineY, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
            textPaint.color = COLOR_CHAMPAGNE
            drawTextAligned(canvas, "${String.format(Locale.US, "%.2f", changeAmount)} ج.م", summaryLeft + 10f, lineY, 105, textPaint, Layout.Alignment.ALIGN_NORMAL)
        }

        // 7. Left Side Notes / QR Place
        val notesRect = RectF(MARGIN, summaryBoxTop, summaryLeft - 16f, summaryBoxTop + summaryHeight)
        paint.color = COLOR_SOFT_BLUSH
        canvas.drawRoundRect(notesRect, 10f, 10f, paint)

        paint.color = COLOR_BORDER
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.8f
        canvas.drawRoundRect(notesRect, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        textPaint.color = COLOR_ROSE_DARK
        textPaint.textSize = 11f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "سياسة الاستبدال والاسترجاع", MARGIN + 10f, summaryBoxTop + 10f, (summaryLeft - 16f - MARGIN - 20f).toInt(), textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 8.5f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val policyText = "• يحق للعميل الاستبدال أو الاسترجاع خلال 14 يوماً مع تقديم أصل الفاتورة بحالتها الأصلية.\n• مستحضرات التجميل المفتوحة لا تسترد حفاظاً على السلامة العامة.\n• شكراً لاختياركم متجرنا ونتمنى لكم تجربة تسوق ممتعة."
        drawTextAligned(canvas, policyText, MARGIN + 10f, summaryBoxTop + 28f, (summaryLeft - 16f - MARGIN - 20f).toInt(), textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        // 8. Footer (Bottom of page)
        val footerY = PAGE_HEIGHT - 60f

        paint.color = COLOR_BORDER
        paint.strokeWidth = 1f
        canvas.drawLine(MARGIN, footerY, PAGE_WIDTH - MARGIN, footerY, paint)

        textPaint.color = COLOR_ROSE_GOLD
        textPaint.textSize = 10.5f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "✨ متجر H&E Store — الأناقة والجمال برعاية فريقنا ✨", MARGIN, footerY + 8f, contentWidth.toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 8.5f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "فاتورة إلكترونية معتمدة • صادرة عبر نظام H&E POS السحابي والمحلي", MARGIN, footerY + 24f, contentWidth.toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)
    }

    private fun drawOrderContent(
        canvas: Canvas,
        order: OrderEntity,
        dateStr: String,
        paymentMethodTitle: String,
        isCash: Boolean
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

        val contentWidth = PAGE_WIDTH - (MARGIN * 2)

        // Background
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), paint)

        // Header
        val headerRect = RectF(MARGIN, 30f, PAGE_WIDTH - MARGIN, 115f)
        paint.color = COLOR_SOFT_BLUSH
        canvas.drawRoundRect(headerRect, 16f, 16f, paint)

        paint.color = COLOR_ROSE_GOLD
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f
        canvas.drawRoundRect(headerRect, 16f, 16f, paint)
        paint.style = Paint.Style.FILL

        textPaint.color = COLOR_ROSE_DARK
        textPaint.textSize = 24f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "H&E Store", MARGIN + 16f, 42f, (contentWidth - 32).toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)

        textPaint.color = COLOR_CHAMPAGNE
        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "فاتورة مبيعات معتمدة من سجلات النظام", MARGIN + 16f, 72f, (contentWidth - 32).toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "خدمة العملاء: 01000000000 | القاهرة، مصر", MARGIN + 16f, 92f, (contentWidth - 32).toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)

        // Metadata
        val metaRect = RectF(MARGIN, 125f, PAGE_WIDTH - MARGIN, 185f)
        paint.color = Color.rgb(250, 250, 252)
        canvas.drawRoundRect(metaRect, 10f, 10f, paint)

        paint.color = COLOR_BORDER
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(metaRect, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "رقم الفاتورة:", PAGE_WIDTH - MARGIN - 120f, 134f, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        textPaint.color = COLOR_ROSE_DARK
        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "#${order.orderNumber}", PAGE_WIDTH - MARGIN - 260f, 133f, 140, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "تاريخ العملية:", PAGE_WIDTH - MARGIN - 120f, 158f, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        textPaint.color = COLOR_CHARCOAL
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, dateStr, PAGE_WIDTH - MARGIN - 260f, 158f, 140, textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "مسؤول الكاشير:", MARGIN + 12f, 134f, 80, textPaint, Layout.Alignment.ALIGN_NORMAL)

        textPaint.color = COLOR_CHARCOAL
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, order.cashierName, MARGIN + 95f, 134f, 120, textPaint, Layout.Alignment.ALIGN_NORMAL)

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "طريقة السداد:", MARGIN + 12f, 158f, 80, textPaint, Layout.Alignment.ALIGN_NORMAL)

        textPaint.color = COLOR_SUCCESS_GREEN
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, paymentMethodTitle, MARGIN + 95f, 158f, 120, textPaint, Layout.Alignment.ALIGN_NORMAL)

        // Items Summary Box
        val tableTop = 195f
        val headerHeight = 25f
        val tableHeaderRect = RectF(MARGIN, tableTop, PAGE_WIDTH - MARGIN, tableTop + headerHeight)
        paint.color = COLOR_ROSE_GOLD
        canvas.drawRoundRect(tableHeaderRect, 6f, 6f, paint)

        textPaint.color = Color.WHITE
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "تفاصيل الأصناف المشتراة", MARGIN + 10f, tableTop + 6f, (contentWidth - 20).toInt(), textPaint, Layout.Alignment.ALIGN_OPPOSITE)

        // Parse summary or display lines
        val lines = order.itemsSummary.split(" | ")
        var currentY = tableTop + headerHeight + 6f
        val rowHeight = 26f

        lines.forEachIndexed { index, line ->
            if (index % 2 == 1) {
                paint.color = COLOR_SOFT_BLUSH
                canvas.drawRect(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + rowHeight, paint)
            }
            paint.color = COLOR_BORDER
            paint.strokeWidth = 0.5f
            canvas.drawLine(MARGIN, currentY + rowHeight, PAGE_WIDTH - MARGIN, currentY + rowHeight, paint)

            textPaint.color = COLOR_CHARCOAL
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            drawTextAligned(canvas, "${index + 1}. $line", MARGIN + 10f, currentY + 6f, (contentWidth - 20).toInt(), textPaint, Layout.Alignment.ALIGN_OPPOSITE)

            currentY += rowHeight
        }

        // Summary box
        currentY += 14f
        val summaryWidth = 240f
        val summaryLeft = PAGE_WIDTH - MARGIN - summaryWidth
        val summaryHeight = if (isCash && order.cashPaid > 0) 125f else 95f

        val summaryRect = RectF(summaryLeft, currentY, PAGE_WIDTH - MARGIN, currentY + summaryHeight)
        paint.color = Color.rgb(253, 252, 253)
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(summaryRect, 10f, 10f, paint)

        paint.color = COLOR_BORDER
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(summaryRect, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        var lineY = currentY + 8f

        textPaint.color = COLOR_CHARCOAL_MUTED
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        drawTextAligned(canvas, "المجموع الفرعي:", summaryLeft + 120f, lineY, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
        textPaint.color = COLOR_CHARCOAL
        drawTextAligned(canvas, "${String.format(Locale.US, "%.2f", order.totalAmount)} ج.م", summaryLeft + 10f, lineY, 105, textPaint, Layout.Alignment.ALIGN_NORMAL)

        lineY += 18f
        if (order.discountAmount > 0) {
            textPaint.color = COLOR_DANGER_RED
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            drawTextAligned(canvas, "قيمة الخصم:", summaryLeft + 120f, lineY, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            drawTextAligned(canvas, "-${String.format(Locale.US, "%.2f", order.discountAmount)} ج.م", summaryLeft + 10f, lineY, 105, textPaint, Layout.Alignment.ALIGN_NORMAL)
            lineY += 18f
        }

        val netRect = RectF(summaryLeft + 6f, lineY, PAGE_WIDTH - MARGIN - 6f, lineY + 26f)
        paint.color = COLOR_ROSE_GOLD
        canvas.drawRoundRect(netRect, 6f, 6f, paint)

        textPaint.color = Color.WHITE
        textPaint.textSize = 11f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "الصافي النهائي:", summaryLeft + 115f, lineY + 5f, 110, textPaint, Layout.Alignment.ALIGN_OPPOSITE)
        drawTextAligned(canvas, "${String.format(Locale.US, "%.2f", order.netAmount)} ج.م", summaryLeft + 12f, lineY + 5f, 105, textPaint, Layout.Alignment.ALIGN_NORMAL)

        // Footer
        val footerY = PAGE_HEIGHT - 60f
        paint.color = COLOR_BORDER
        paint.strokeWidth = 1f
        canvas.drawLine(MARGIN, footerY, PAGE_WIDTH - MARGIN, footerY, paint)

        textPaint.color = COLOR_ROSE_GOLD
        textPaint.textSize = 10.5f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        drawTextAligned(canvas, "✨ متجر H&E Store — الأناقة والجمال برعاية فريقنا ✨", MARGIN, footerY + 8f, contentWidth.toInt(), textPaint, Layout.Alignment.ALIGN_CENTER)
    }

    private fun drawTextAligned(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        width: Int,
        textPaint: TextPaint,
        alignment: Layout.Alignment
    ) {
        val safeWidth = maxOf(10, width)
        val layout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, textPaint, safeWidth)
                .setAlignment(alignment)
                .setIncludePad(false)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(text, textPaint, safeWidth, alignment, 1.0f, 0.0f, false)
        }
        canvas.save()
        canvas.translate(x, y)
        layout.draw(canvas)
        canvas.restore()
    }
}
