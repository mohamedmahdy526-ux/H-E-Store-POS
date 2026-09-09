package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.ProductEntity
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DangerRed
import com.example.ui.theme.RoseGoldDark
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.SoftBlushBackground
import com.example.ui.theme.SuccessGreen

@Composable
fun QuickReceiveStockDialog(
    product: ProductEntity,
    onConfirmAdd: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var quantityInput by remember { mutableStateOf("5") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
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
                        modifier = Modifier.testTag("receive_stock_close")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = CharcoalMuted)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            tint = RoseGoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "استلام بضاعة سريعة",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText
                        )
                    }

                    Spacer(modifier = Modifier.width(36.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Product Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SoftBlushBackground)
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = product.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseGoldDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "الرصيد الحالي: ${product.stockQuantity} قطعة",
                                fontSize = 12.sp,
                                color = CharcoalText
                            )
                            Text(
                                text = "سعر البيع: ${product.sellPrice} ج.م",
                                fontSize = 12.sp,
                                color = CharcoalMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick buttons
                Text(
                    text = "اختر كمية الاستلام السريعة:",
                    fontSize = 12.sp,
                    color = CharcoalMuted,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(3, 5, 10, 20, 50).forEach { qty ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (quantityInput == qty.toString()) RoseGoldPrimary else SoftBlushBackground,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { quantityInput = qty.toString() }
                                .testTag("quick_qty_$qty")
                        ) {
                            Text(
                                text = "+$qty",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (quantityInput == qty.toString()) Color.White else RoseGoldPrimary,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = quantityInput,
                    onValueChange = { quantityInput = it },
                    label = { Text("أو اكتب الكمية المضافة يدوياً") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("receive_stock_custom_qty"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                val addedQty = quantityInput.toIntOrNull() ?: 0
                val newTotalStock = product.stockQuantity + addedQty

                Text(
                    text = "الرصيد الجديد سيصبح: $newTotalStock قطعة",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (addedQty > 0) {
                            onConfirmAdd(addedQty)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_receive_stock_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("تأكيد إضافة الرصيد للمخزن", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReconcileStockDialog(
    product: ProductEntity,
    onConfirmReconciliation: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var shelfCountInput by remember { mutableStateOf(product.stockQuantity.toString()) }

    val counted = shelfCountInput.toIntOrNull() ?: product.stockQuantity
    val diff = counted - product.stockQuantity

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
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
                        modifier = Modifier.testTag("reconcile_close")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = CharcoalMuted)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Balance,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "جرد دوري وتسوية الرصيد",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText
                        )
                    }

                    Spacer(modifier = Modifier.width(36.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoseGoldDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "الرصيد المسجل بالسيستم:", fontSize = 13.sp, color = CharcoalMuted)
                    Text(
                        text = "${product.stockQuantity} قطعة",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = shelfCountInput,
                    onValueChange = { shelfCountInput = it },
                    label = { Text("العدد الفعلي على الرف (العد اليدوي)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reconcile_shelf_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Difference indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                diff == 0 -> SuccessGreen.copy(alpha = 0.1f)
                                diff > 0 -> ChampagneGold.copy(alpha = 0.15f)
                                else -> DangerRed.copy(alpha = 0.1f)
                            }
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when {
                            diff == 0 -> "الرصيد مطابق تماماً (لا يوجد فوارق) ✨"
                            diff > 0 -> "يوجد زيادة بالرف قدرها: +$diff قطعة 📈"
                            else -> "يوجد عجز بالمخزن قدره: $diff قطعة ⚠️"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            diff == 0 -> SuccessGreen
                            diff > 0 -> ChampagneGold
                            else -> DangerRed
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onConfirmReconciliation(counted)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_reconciliation_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("تسوية وتحديث رصيد المخزن", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
