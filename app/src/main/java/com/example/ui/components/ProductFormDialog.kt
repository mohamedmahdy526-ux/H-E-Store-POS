package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.ProductEntity
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceVariant
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProductFormDialog(
    initialProduct: ProductEntity? = null,
    onSave: (name: String, barcode: String, category: String, sellPrice: Double, costPrice: Double, stock: Int, minStock: Int, desc: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var barcode by remember {
        mutableStateOf(
            initialProduct?.barcode ?: (6220000000000L + (System.currentTimeMillis() % 100000000L)).toString()
        )
    }
    var category by remember { mutableStateOf(initialProduct?.category ?: "بشرة") }
    var sellPrice by remember { mutableStateOf(initialProduct?.sellPrice?.toString() ?: "") }
    var costPrice by remember { mutableStateOf(initialProduct?.costPrice?.toString() ?: "") }
    var stockQuantity by remember { mutableStateOf(initialProduct?.stockQuantity?.toString() ?: "10") }
    var minStockAlert by remember { mutableStateOf(initialProduct?.minStockAlert?.toString() ?: "3") }
    var description by remember { mutableStateOf(initialProduct?.description ?: "") }

    val categories = listOf("عيون", "شفايف", "بشرة", "عناية", "إكسسوارات", "عطور", "أطفال")
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CardSurface,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("product_form_close")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = TextSecondary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Inventory,
                            contentDescription = null,
                            tint = RoseGoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (initialProduct == null) "إضافة منتج جديد" else "تعديل بيانات المنتج",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(36.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم المنتج *", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("product_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGoldPrimary,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = CardSurfaceVariant,
                        unfocusedContainerColor = CardSurfaceVariant
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category selector
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("الفئة التصنيفية", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { categoryMenuExpanded = true }
                            .testTag("product_category_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseGoldPrimary,
                            unfocusedBorderColor = CardBorder,
                            focusedContainerColor = CardSurfaceVariant,
                            unfocusedContainerColor = CardSurfaceVariant
                        )
                    )
                    DropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { categoryMenuExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat, fontSize = 13.sp) },
                                onClick = {
                                    category = cat
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Barcode field + generate random button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = barcode,
                        onValueChange = { barcode = it },
                        label = { Text("الباركود", fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("product_barcode_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseGoldPrimary,
                            unfocusedBorderColor = CardBorder,
                            focusedContainerColor = CardSurfaceVariant,
                            unfocusedContainerColor = CardSurfaceVariant
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            barcode = (6220000000000L + (System.currentTimeMillis() % 100000000L)).toString()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("generate_barcode_button")
                    ) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = "توليد باركود تلقائي", tint = ChampagneGold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Prices
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = sellPrice,
                        onValueChange = { sellPrice = it },
                        label = { Text("سعر البيع (ج.م) *", fontSize = 13.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("product_sell_price_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseGoldPrimary,
                            unfocusedBorderColor = CardBorder,
                            focusedContainerColor = CardSurfaceVariant,
                            unfocusedContainerColor = CardSurfaceVariant
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = costPrice,
                        onValueChange = { costPrice = it },
                        label = { Text("سعر التكلفة (ج.م)", fontSize = 13.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("product_cost_price_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseGoldPrimary,
                            unfocusedBorderColor = CardBorder,
                            focusedContainerColor = CardSurfaceVariant,
                            unfocusedContainerColor = CardSurfaceVariant
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stock & Alert
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = stockQuantity,
                        onValueChange = { stockQuantity = it },
                        label = { Text("الرصيد المتاح *", fontSize = 13.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("product_stock_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseGoldPrimary,
                            unfocusedBorderColor = CardBorder,
                            focusedContainerColor = CardSurfaceVariant,
                            unfocusedContainerColor = CardSurfaceVariant
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = minStockAlert,
                        onValueChange = { minStockAlert = it },
                        label = { Text("حد التنبيه (نواقص)", fontSize = 13.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("product_min_stock_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseGoldPrimary,
                            unfocusedBorderColor = CardBorder,
                            focusedContainerColor = CardSurfaceVariant,
                            unfocusedContainerColor = CardSurfaceVariant
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("وصف أو تفاصيل إضافية (اختياري)", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("product_desc_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGoldPrimary,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = CardSurfaceVariant,
                        unfocusedContainerColor = CardSurfaceVariant
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val sell = sellPrice.toDoubleOrNull() ?: 0.0
                        val cost = costPrice.toDoubleOrNull() ?: (sell * 0.7)
                        val stock = stockQuantity.toIntOrNull() ?: 0
                        val minStock = minStockAlert.toIntOrNull() ?: 3
                        if (name.isNotBlank()) {
                            onSave(name, barcode, category, sell, cost, stock, minStock, description)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_product_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (initialProduct == null) "إضافة المنتج للمخزن" else "حفظ التعديلات",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
