package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cashierName: String,
    val startTime: Long,
    val endTime: Long? = null,
    val isOpen: Boolean = true,
    val openingBalance: Double,
    val closingCountedCash: Double? = null,
    val expectedCash: Double? = null,
    val differenceCash: Double? = null,
    val totalCashSales: Double = 0.0,
    val totalElectronicSales: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val totalWithdrawals: Double = 0.0,
    val notes: String = ""
)
