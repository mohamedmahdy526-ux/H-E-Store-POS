package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val timestamp: Long,
    val totalAmount: Double,
    val discountAmount: Double,
    val netAmount: Double,
    val totalCost: Double,
    val paymentMethod: String, // CASH, INSTAPAY, VODAFONE_CASH, CARD
    val cashPaid: Double,
    val changeAmount: Double,
    val shiftId: Long,
    val cashierName: String,
    val itemsSummary: String // Formatted summary or JSON
)
