package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.OrderEntity
import com.example.data.ProductEntity
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceVariant
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.DangerRed
import com.example.ui.theme.RoseGoldLight
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Calendar
import java.util.Date
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
    onRestoreDefaults: () -> Unit,
    onExportOrderPdf: (OrderEntity) -> Unit = {}
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
            onExportOrderPdf = onExportOrderPdf,
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
            .background(CanvasBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = CardSurface,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(RoseGoldLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = RoseGoldPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "لوحة تحكم الإدارة والتقارير",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "أدخل الرمز السري للوصول إلى تقارير المبيعات والأرباح (الافتراضي: 1234)",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = enteredPin,
                    onValueChange = {
                        if (it.length <= 6) enteredPin = it
                        errorMessage = null
                    },
                    label = { Text("رمز PIN", fontSize = 13.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGoldPrimary,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = CardSurfaceVariant,
                        unfocusedContainerColor = CardSurfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("manager_pin_input")
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = DangerRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

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
                        .height(50.dp)
                        .testTag("submit_pin_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("دخول الإدارة", fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
    onExportOrderPdf: (OrderEntity) -> Unit,
    onExportBackup: () -> Unit,
    onRestoreDefaults: () -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }

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

    val cashSales = filteredOrders.filter { it.paymentMethod == "CASH" }.sumOf { it.netAmount }
    val instapaySales = filteredOrders.filter { it.paymentMethod == "INSTAPAY" }.sumOf { it.netAmount }
    val vodafoneSales = filteredOrders.filter { it.paymentMethod == "VODAFONE_CASH" }.sumOf { it.netAmount }
    val cardSales = filteredOrders.filter { it.paymentMethod == "CARD" }.sumOf { it.netAmount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Toolbar: Period Selector & Lock button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("TODAY" to "اليوم", "WEEK" to "الأسبوع", "ALL" to "الكل").forEach { (code, label) ->
                        val isSelected = period == code
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) RoseGoldPrimary else CardSurface,
                            border = BorderStroke(1.dp, if (isSelected) RoseGoldPrimary else CardBorder),
                            modifier = Modifier
                                .height(38.dp)
                                .clickable { onPeriodSelected(code) }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 14.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextPrimary
                                )
                            }
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = CardSurface,
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.size(38.dp)
                ) {
                    IconButton(
                        onClick = onLock,
                        modifier = Modifier.testTag("lock_manager_button")
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "قفل", tint = TextSecondary, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        // Summary KPI 2x2 Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Payment Methods Breakdown
        item {
            Surface(
                color = CardSurface,
                border = BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "توزيع طرق التحصيل والدفع",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    PaymentProgressRow("كاش نقدي", cashSales, totalSales, RoseGoldPrimary)
                    PaymentProgressRow("إنستاباي (InstaPay)", instapaySales, totalSales, ChampagneGold)
                    PaymentProgressRow("فودافون كاش", vodafoneSales, totalSales, Color(0xFFE91E63))
                    PaymentProgressRow("بطاقات بنكية (Visa/Mastercard)", cardSales, totalSales, SuccessGreen)
                }
            }
        }

        // Catalog Status
        item {
            Surface(
                color = CardSurface,
                border = BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "حالة كتالوج المخزون",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val totalStockQty = products.sumOf { it.stockQuantity }
                    val totalInventoryValue = products.sumOf { it.stockQuantity * it.sellPrice }
                    val totalInventoryCost = products.sumOf { it.stockQuantity * it.costPrice }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("إجمالي عدد القطع المتاحة:", fontSize = 13.sp, color = TextSecondary)
                        Text("$totalStockQty قطعة", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("قيمة المخزون بسعر البيع:", fontSize = 13.sp, color = TextSecondary)
                        Text("${String.format(Locale.US, "%.0f", totalInventoryValue)} ج.م", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoseGoldPrimary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("رأس المال المجمد (التكلفة):", fontSize = 13.sp, color = TextSecondary)
                        Text("${String.format(Locale.US, "%.0f", totalInventoryCost)} ج.م", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ChampagneGold)
                    }
                }
            }
        }

        // Recent Invoices & PDF Export Card
        item {
            Surface(
                color = CardSurface,
                border = BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "سجل الفواتير والمبيعات (تصدير PDF)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${filteredOrders.size} فاتورة",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RoseGoldPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    if (filteredOrders.isEmpty()) {
                        Text(
                            text = "لا توجد فواتير مسجلة في هذه الفترة الزمنية.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        val recentOrders = filteredOrders.sortedByDescending { it.timestamp }.take(10)
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            recentOrders.forEach { order ->
                                val sdf = remember { java.text.SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale("ar")) }
                                val dateFormatted = sdf.format(Date(order.timestamp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = CardSurfaceVariant,
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "فاتورة #${order.orderNumber}",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "${String.format(Locale.US, "%.2f", order.netAmount)} ج.م",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = RoseGoldPrimary
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "$dateFormatted • ${order.cashierName}",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }

                                        Button(
                                            onClick = { onExportOrderPdf(order) },
                                            colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Share,
                                                contentDescription = "مشاركة PDF",
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "تصدير PDF",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tools & Settings: Export, Reset, Change PIN
        item {
            Surface(
                color = CardSurface,
                border = BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "أدوات النظام والنسخ الاحتياطي",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = onExportBackup,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CardBorder)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = RoseGoldPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تصدير نسخة احتياطية من الأصناف (JSON)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onRestoreDefaults,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CardBorder)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = ChampagneGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("استعادة كتالوج الـ 237 منتجاً الافتراضي", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { showPinDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CardBorder)
                    ) {
                        Icon(Icons.Default.Password, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تغيير رمز PIN السري للإدارة", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
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
    Surface(
        color = CardSurface,
        border = BorderStroke(1.dp, CardBorder),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, fontSize = 12.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 11.sp, color = color, fontWeight = FontWeight.SemiBold)
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

    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 12.sp, color = TextPrimary)
            Text(
                "${String.format(Locale.US, "%.0f", amount)} ج (${String.format(Locale.US, "%.0f", fraction * 100)}%)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = CardSurfaceVariant
        )
    }
}

@Composable
private fun ChangePinDialog(
    onChangePin: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newPin by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CardSurface,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("تغيير رمز PIN الإدارة", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newPin,
                    onValueChange = { if (it.length <= 6) newPin = it },
                    label = { Text("رمز جديد (4-6 أرقام)", fontSize = 13.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGoldPrimary,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = CardSurfaceVariant,
                        unfocusedContainerColor = CardSurfaceVariant
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (newPin.length >= 4) {
                            onChangePin(newPin)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("حفظ الرمز الجديد", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
