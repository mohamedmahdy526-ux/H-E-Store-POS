package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val shiftId: Long,
    val type: String, // "EXPENSE" (نثريات/مصروفات) or "WITHDRAWAL" (سحوبات)
    val title: String,
    val amount: Double,
    val timestamp: Long,
    val notes: String = ""
)
