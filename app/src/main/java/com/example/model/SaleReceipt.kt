package com.example.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SaleReceipt(
    val orderNumber: String,
    val timestamp: Long,
    val items: List<CartItem>,
    val subtotal: Double,
    val discount: Double,
    val totalAmount: Double,
    val paymentMethod: PaymentMethod,
    val cashPaid: Double,
    val changeAmount: Double,
    val cashierName: String,
    val storeName: String = "H&E Store",
    val storePhone: String = "01000000000"
) {
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar"))
            return sdf.format(Date(timestamp))
        }

    fun toShareableText(): String {
        val sb = StringBuilder()
        sb.appendLine("✨ *فاتورة مبيعات - $storeName* ✨")
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("رقم الفاتورة: #$orderNumber")
        sb.appendLine("التاريخ: $formattedDate")
        sb.appendLine("الكاشير: $cashierName")
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("الأصناف:")
        items.forEachIndexed { index, item ->
            val discText = if (item.discountPerItem > 0) " (خصم: ${item.discountPerItem} ج.م)" else ""
            sb.appendLine("${index + 1}. ${item.product.name}")
            sb.appendLine("   ${item.quantity} × ${item.unitPrice} ج.م = ${item.totalPrice} ج.م$discText")
        }
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("المجموع الفرعي: ${String.format(Locale.US, "%.2f", subtotal)} ج.م")
        if (discount > 0) {
            sb.appendLine("إجمالي الخصم: ${String.format(Locale.US, "%.2f", discount)} ج.م")
        }
        sb.appendLine("*الإجمالي الصافي: ${String.format(Locale.US, "%.2f", totalAmount)} ج.م*")
        sb.appendLine("طريقة الدفع: ${paymentMethod.titleAr}")
        if (paymentMethod == PaymentMethod.CASH && cashPaid > 0) {
            sb.appendLine("المدفوع كاش: ${String.format(Locale.US, "%.2f", cashPaid)} ج.م")
            sb.appendLine("الباقي للعميل: ${String.format(Locale.US, "%.2f", changeAmount)} ج.م")
        }
        sb.appendLine("━━━━━━━━━━━━━━━━━━━")
        sb.appendLine("شكراً لزيارتكم متجر H&E Store! 💕")
        sb.appendLine("يسعدنا دائماً خدمتكم.")
        return sb.toString()
    }
}
