package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OrderEntity
import com.example.data.ProductEntity
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DangerRed
import com.example.ui.theme.RoseGoldDark
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.SoftBlushBackground
import com.example.ui.theme.SoftBlushBorder
import com.example.ui.theme.SoftBlushCard
import com.example.ui.theme.SuccessGreen
import java.util.Calendar
import java.util.Locale

@Composable
fun ManagerScreen(
    isUnlocked: Boolean,
    orders: List<OrderEntity>,
    products: List<ProductEntity>,
    period: String,
    onPeriodSelected: (String) -> Unit,
    onVerifyPin: (String) -> Boolean,
    onLock: () -> Unit,
    onChangePin: (String) -> Unit,
    onExportBackup: () -> String,
    onRestoreDefaults: () -> Unit
) {
    val context = LocalContext.current

    if (!isUnlocked) {
        PinLockView(onVerifyPin = onVerifyPin)
    } else {
        ManagerDashboardContent(
            orders = orders,
            products = products,
            period = period,
            onPeriodSelected = onPeriodSelected,
            onLock = onLock,
            onChangePin = onChangePin,
            onExportBackup = {
                val json = onExportBackup()
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("H&E Store Backup", json)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "تم نسخ نسخة البيانات بصيغة JSON إلى الحافظة!", Toast.LENGTH_LONG).show()
            },
            onRestoreDefaults = {
                onRestoreDefaults()
                Toast.makeText(context, "تمت إعادة تعيين كتالوج المنتجات الأصلي بنجاح!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun PinLockView(onVerifyPin: (String) -> Boolean) {
    var enteredPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBlushBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(RoseGoldPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = RoseGoldPrimary,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "لوحة تحكم الإدارة والتقارير",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalText
                )

                Text(
                    text = "أدخل الرمز السري للوصول إلى تقارير المبيعات والأرباح (الافتراضي: 1234)",
                    fontSize = 12.sp,
                    color = CharcoalMuted,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = enteredPin,
                    onValueChange = {
                        if (it.length <= 6) enteredPin = it
                        errorMessage = null
                    },
                    label = { Text("رمز PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("manager_pin_input")
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = DangerRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (onVerifyPin(enteredPin)) {
                            enteredPin = ""
                        } else {
                            errorMessage = "الرمز السري غير صحيح. حاول مجدداً."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_pin_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("دخول الإدارة", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ManagerDashboardContent(
    orders: List<OrderEntity>,
    products: List<ProductEntity>,
    period: String,
    onPeriodSelected: (String) -> Unit,
    onLock: () -> Unit,
    onChangePin: (String) -> Unit,
    onExportBackup: () -> Unit,
    onRestoreDefaults: () -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }

    // Filter orders by period
    val now = System.currentTimeMillis()
    val filteredOrders = remember(orders, period) {
        when (period) {
            "TODAY" -> {
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                orders.filter { it.timestamp >= cal.timeInMillis }
            }
            "WEEK" -> {
                val oneWeekAgo = now - (7L * 24 * 60 * 60 * 1000)
                orders.filter { it.timestamp >= oneWeekAgo }
            }
            else -> orders
        }
    }

    val totalSales = filteredOrders.sumOf { it.netAmount }
    val totalCost = filteredOrders.sumOf { it.totalCost }
    val netProfit = maxOf(0.0, totalSales - totalCost)
    val marginPercent = if (totalSales > 0) (netProfit / totalSales) * 100 else 0.0
    val totalOrdersCount = filteredOrders.size

    // Payment methods breakdown
    val cashSales = filteredOrders.filter { it.paymentMethod == "CASH" }.sumOf { it.netAmount }
    val instapaySales = filteredOrders.filter { it.paymentMethod == "INSTAPAY" }.sumOf { it.netAmount }
    val vodafoneSales = filteredOrders.filter { it.paymentMethod == "VODAFONE_CASH" }.sumOf { it.netAmount }
    val cardSales = filteredOrders.filter { it.paymentMethod == "CARD" }.sumOf { it.netAmount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBlushBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Toolbar: Period Selector & Lock button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("TODAY" to "اليوم", "WEEK" to "الأسبوع", "ALL" to "الكل").forEach { (code, label) ->
                        FilterChip(
                            selected = period == code,
                            onClick = { onPeriodSelected(code) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoseGoldPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onLock,
                    modifier = Modifier.testTag("lock_manager_button")
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "قفل", tint = CharcoalMuted)
                }
            }
        }

        // Summary KPI 2x2 Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    KpiCard(
                        title = "إجمالي المبيعات",
                        value = "${String.format(Locale.US, "%.0f", totalSales)} ج.م",
                        subtitle = "$totalOrdersCount فاتورة",
                        icon = Icons.Default.ReceiptLong,
                        color = RoseGoldPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "صافي الأرباح",
                        value = "${String.format(Locale.US, "%.0f", netProfit)} ج.م",
                        subtitle = "هامش ${String.format(Locale.US, "%.1f", marginPercent)}%",
                        icon = Icons.Default.TrendingUp,
                        color = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    KpiCard(
                        title = "تكلفة البضاعة (COGS)",
                        value = "${String.format(Locale.US, "%.0f", totalCost)} ج.م",
                        subtitle = "سعر شراء المنتجات",
                        icon = Icons.Default.AttachMoney,
                        color = ChampagneGold,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "متوسط الفاتورة",
                        value = if (totalOrdersCount > 0) "${String.format(Locale.US, "%.0f", totalSales / totalOrdersCount)} ج.م" else "0 ج.م",
                        subtitle = "معدل إنفاق العميل",
                        icon = Icons.Default.PieChart,
                        color = RoseGoldDark,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Payment Methods Breakdown
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "توزيع طرق التحصيل والدفع",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    PaymentProgressRow("كاش نقدي", cashSales, totalSales, RoseGoldPrimary)
                    PaymentProgressRow("إنستاباي (InstaPay)", instapaySales, totalSales, ChampagneGold)
                    PaymentProgressRow("فودافون كاش", vodafoneSales, totalSales, Color(0xFFE91E63))
                    PaymentProgressRow("بطاقات بنكية (Visa/Mastercard)", cardSales, totalSales, SuccessGreen)
                }
            }
        }

        // Top Selling vs Slow Moving Insights
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "حالة كتالوج المخزون",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val totalStockQty = products.sumOf { it.stockQuantity }
                    val totalInventoryValue = products.sumOf { it.stockQuantity * it.sellPrice }
                    val totalInventoryCost = products.sumOf { it.stockQuantity * it.costPrice }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("إجمالي عدد القطع المتاحة:", fontSize = 12.sp, color = CharcoalMuted)
                        Text("$totalStockQty قطعة", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CharcoalText)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("قيمة المخزون بسعر البيع:", fontSize = 12.sp, color = CharcoalMuted)
                        Text("${String.format(Locale.US, "%.0f", totalInventoryValue)} ج.م", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RoseGoldPrimary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("رأس المال المجمد (التكلفة):", fontSize = 12.sp, color = CharcoalMuted)
                        Text("${String.format(Locale.US, "%.0f", totalInventoryCost)} ج.م", fontSize = 12.sp, color = ChampagneGold)
                    }
                }
            }
        }

        // Tools & Settings: Export, Reset, Change PIN
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "أدوات النظام والنسخ الاحتياطي",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onExportBackup,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = RoseGoldPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تصدير نسخة احتياطية من الأصناف (JSON)", color = CharcoalText)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onRestoreDefaults,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = ChampagneGold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("استعادة كتالوج الـ 237 منتجاً الافتراضي", color = CharcoalText)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { showPinDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Password, contentDescription = null, tint = RoseGoldDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تغيير رمز PIN السري للإدارة", color = CharcoalText)
                    }
                }
            }
        }
    }

    if (showPinDialog) {
        ChangePinDialog(
            onChangePin = {
                onChangePin(it)
                showPinDialog = false
            },
            onDismiss = { showPinDialog = false }
        )
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 11.sp, color = CharcoalMuted)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = CharcoalText)
            Text(subtitle, fontSize = 10.sp, color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PaymentProgressRow(
    title: String,
    amount: Double,
    total: Double,
    color: Color
) {
    val fraction = if (total > 0) (amount / total).toFloat().coerceIn(0f, 1f) else 0f

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 11.sp, color = CharcoalText)
            Text(
                "${String.format(Locale.US, "%.0f", amount)} ج (${String.format(Locale.US, "%.0f", fraction * 100)}%)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = SoftBlushBorder
        )
    }
}

@Composable
private fun ChangePinDialog(
    onChangePin: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newPin by remember { mutableStateOf("") }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("تغيير رمز PIN الإدارة", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CharcoalText)
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = newPin,
                    onValueChange = { if (it.length <= 6) newPin = it },
                    label = { Text("رمز جديد (4-6 أرقام)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (newPin.length >= 4) {
                            onChangePin(newPin)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("حفظ الرمز الجديد", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
