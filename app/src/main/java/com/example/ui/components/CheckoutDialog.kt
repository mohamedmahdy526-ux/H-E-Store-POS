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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.model.PaymentMethod
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DangerRed
import com.example.ui.theme.RoseGoldDark
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.SoftBlushBackground
import com.example.ui.theme.SuccessGreen
import java.util.Locale

@Composable
fun CheckoutDialog(
    subtotal: Double,
    currentDiscount: Double,
    onDiscountChanged: (Double) -> Unit,
    onConfirmSale: (PaymentMethod, Double) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf(PaymentMethod.CASH) }
    var discountInput by remember { mutableStateOf(if (currentDiscount > 0) currentDiscount.toString() else "") }
    var cashPaidInput by remember { mutableStateOf("") }
    var electronicRef by remember { mutableStateOf("") }

    val discountValue = discountInput.toDoubleOrNull() ?: 0.0
    val netTotal = maxOf(0.0, subtotal - discountValue)
    val cashPaidValue = cashPaidInput.toDoubleOrNull() ?: netTotal
    val changeAmount = if (selectedMethod == PaymentMethod.CASH) maxOf(0.0, cashPaidValue - netTotal) else 0.0

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
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
                        modifier = Modifier.testTag("checkout_close_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = CharcoalMuted)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.PriceCheck,
                            contentDescription = null,
                            tint = RoseGoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "تحصيل الحساب وإتمام البيع",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText
                        )
                    }

                    Spacer(modifier = Modifier.width(36.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Total Amount Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(RoseGoldPrimary.copy(alpha = 0.12f))
                        .border(1.dp, RoseGoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "المبلغ المطلوب سداده",
                            fontSize = 12.sp,
                            color = CharcoalMuted
                        )
                        Text(
                            text = "${String.format(Locale.US, "%.2f", netTotal)} ج.م",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RoseGoldDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Payment Method Selector
                Text(
                    text = "اختر طريقة الدفع:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalText,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PaymentMethodChip(
                        title = "كاش",
                        icon = Icons.Default.LocalAtm,
                        isSelected = selectedMethod == PaymentMethod.CASH,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedMethod = PaymentMethod.CASH }
                    )
                    PaymentMethodChip(
                        title = "إنستاباي",
                        icon = Icons.Default.AccountBalanceWallet,
                        isSelected = selectedMethod == PaymentMethod.INSTAPAY,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedMethod = PaymentMethod.INSTAPAY }
                    )
                    PaymentMethodChip(
                        title = "فودافون",
                        icon = Icons.Default.PhoneAndroid,
                        isSelected = selectedMethod == PaymentMethod.VODAFONE_CASH,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedMethod = PaymentMethod.VODAFONE_CASH }
                    )
                    PaymentMethodChip(
                        title = "فيزا",
                        icon = Icons.Default.CreditCard,
                        isSelected = selectedMethod == PaymentMethod.CARD,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedMethod = PaymentMethod.CARD }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Discount field
                OutlinedTextField(
                    value = discountInput,
                    onValueChange = {
                        discountInput = it
                        onDiscountChanged(it.toDoubleOrNull() ?: 0.0)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_discount_input"),
                    label = { Text("خصم إضافي على الفاتورة (ج.م)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Specific Method Fields
                if (selectedMethod == PaymentMethod.CASH) {
                    OutlinedTextField(
                        value = cashPaidInput,
                        onValueChange = { cashPaidInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_cash_paid_input"),
                        label = { Text("المبلغ المستلم من العميل (ج.م)") },
                        placeholder = { Text(String.format(Locale.US, "%.2f", netTotal)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick cash denomination chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(netTotal, 50.0, 100.0, 200.0).distinct().take(4).forEach { amount ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SoftBlushBackground,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { cashPaidInput = String.format(Locale.US, "%.0f", amount) }
                            ) {
                                Text(
                                    text = if (amount == netTotal) "بالضبط" else "${amount.toInt()} ج",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RoseGoldPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Change calculation display
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (changeAmount > 0) SuccessGreen.copy(alpha = 0.1f) else SoftBlushBackground)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "الباقي للعميل:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalText
                            )
                            Text(
                                text = "${String.format(Locale.US, "%.2f", changeAmount)} ج.م",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (changeAmount > 0) SuccessGreen else CharcoalMuted
                            )
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = electronicRef,
                        onValueChange = { electronicRef = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_ref_input"),
                        label = {
                            Text(
                                when (selectedMethod) {
                                    PaymentMethod.INSTAPAY -> "اسم حساب إنستاباي أو رقم التحويل"
                                    PaymentMethod.VODAFONE_CASH -> "رقم محفظة فودافون كاش المحول منها"
                                    PaymentMethod.CARD -> "آخر 4 أرقام من البطاقة / رقم العملية"
                                    else -> "ملاحظة أو مرجع العملية"
                                }
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = {
                        val paid = if (selectedMethod == PaymentMethod.CASH) {
                            cashPaidInput.toDoubleOrNull() ?: netTotal
                        } else {
                            netTotal
                        }
                        onConfirmSale(selectedMethod, paid)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("checkout_confirm_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "إتمام الدفع وطباعة الفاتورة",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("payment_chip_$title"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) RoseGoldPrimary else SoftBlushBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else CharcoalMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else CharcoalText
            )
        }
    }
}
