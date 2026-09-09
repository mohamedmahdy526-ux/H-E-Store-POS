package com.example.model

import com.example.data.ProductEntity

data class CartItem(
    val product: ProductEntity,
    val quantity: Int = 1,
    val discountPerItem: Double = 0.0 // Custom discount per item in EGP
) {
    val unitPrice: Double
        get() = maxOf(0.0, product.sellPrice - discountPerItem)

    val totalPrice: Double
        get() = unitPrice * quantity

    val totalCost: Double
        get() = product.costPrice * quantity

    val totalProfit: Double
        get() = totalPrice - totalCost
}

enum class PaymentMethod(val titleAr: String, val code: String) {
    CASH("كاش", "CASH"),
    INSTAPAY("إنستاباي", "INSTAPAY"),
    VODAFONE_CASH("فودافون كاش", "VODAFONE_CASH"),
    CARD("بطاقة بنكية", "CARD");

    companion object {
        fun fromCode(code: String): PaymentMethod {
            return entries.find { it.code == code } ?: CASH
        }
    }
}
