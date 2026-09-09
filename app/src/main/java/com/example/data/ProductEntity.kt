package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val barcode: String,
    val category: String,
    val sellPrice: Double,
    val costPrice: Double,
    val stockQuantity: Int,
    val minStockAlert: Int = 3,
    val description: String = ""
)
