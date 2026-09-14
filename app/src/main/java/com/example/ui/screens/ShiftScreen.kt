package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExpenseEntity
import com.example.data.ShiftEntity
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceVariant
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerRedLight
import com.example.ui.theme.RoseGoldLight
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShiftScreen(
    activeShift: ShiftEntity?,
    expenses: List<ExpenseEntity>,
    shiftHistory: List<ShiftEntity>,
    onOpenShiftClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onCloseShiftClick: () -> Unit
) {
    val sdf = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar"))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Active Shift Card
        item {
            if (activeShift != null && activeShift.isOpen) {
                ActiveShiftCard(
                    shift = activeShift,
                    onAddExpense = onAddExpenseClick,
                    onCloseShift = onCloseShiftClick
                )
            } else {
                NoActiveShiftCard(onOpenShift = onOpenShiftClick)
            }
        }

        // Current Shift Expenses & Withdrawals
        if (activeShift != null && activeShift.isOpen) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مصروفات وسحوبات الوردية الحالية",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    OutlinedButton(
                        onClick = onAddExpenseClick,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, DangerRed.copy(alpha = 0.5f)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.MoneyOff, contentDescription = null, modifier = Modifier.size(15.dp), tint = DangerRed)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+ تسجيل مصروف", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DangerRed)
                    }
                }
            }

            val currentExpenses = expenses.filter { it.shiftId == activeShift.id }
            if (currentExpenses.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "لم يتم تسجيل أي مصروفات أو سحوبات نقدية في هذه الوردية بعد.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(currentExpenses) { expense ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (expense.type == "EXPENSE") "نثريات" else "سحب كاش",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (expense.type == "EXPENSE") RoseGoldPrimary else DangerRed,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (expense.type == "EXPENSE") RoseGoldLight
                                                else DangerRedLight
                                            )
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = expense.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                                if (expense.notes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = expense.notes,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Text(
                                text = "-${String.format(Locale.US, "%.2f", expense.amount)} ج.م",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DangerRed
                            )
                        }
                    }
                }
            }
        }

        // Shift Archives & History
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, contentDescription = null, tint = RoseGoldPrimary, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "سجل الورديات السابقة المقفلة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        val closedShifts = shiftHistory.filter { !it.isOpen }
        if (closedShifts.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "لا توجد ورديات سابقة مؤرشفة حتى الآن.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(closedShifts) { shift ->
                ClosedShiftCard(shift = shift, sdf = sdf)
            }
        }
    }
}

@Composable
private fun ActiveShiftCard(
    shift: ShiftEntity,
    onAddExpense: () -> Unit,
    onCloseShift: () -> Unit
) {
    val expectedCash = shift.openingBalance + shift.totalCashSales - shift.totalExpenses - shift.totalWithdrawals

    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SuccessGreenLight,
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "الوردية الحالية نشطة",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }

                Text(
                    text = "الكاشير: ${shift.cashierName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Big Cash Drawer Counter
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CardSurfaceVariant,
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "الكاش المتوقع حالياً بالدرج",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${String.format(Locale.US, "%.2f", expectedCash)} ج.م",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoseGoldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Breakdown Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("العهدة الابتدائية:", fontSize = 13.sp, color = TextSecondary)
                    Text("${String.format(Locale.US, "%.2f", shift.openingBalance)} ج.م", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("مبيعات كاش:", fontSize = 13.sp, color = TextSecondary)
                    Text("+${String.format(Locale.US, "%.2f", shift.totalCashSales)} ج.م", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("مبيعات إلكترونية (إنستاباي / فيزا):", fontSize = 13.sp, color = TextSecondary)
                    Text("${String.format(Locale.US, "%.2f", shift.totalElectronicSales)} ج.م", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ChampagneGold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("المصروفات والسحوبات:", fontSize = 13.sp, color = TextSecondary)
                    Text("-${String.format(Locale.US, "%.2f", shift.totalExpenses + shift.totalWithdrawals)} ج.م", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DangerRed)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onAddExpense,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("shift_add_expense_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Icon(Icons.Default.MoneyOff, contentDescription = null, tint = DangerRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تسجيل مصروف", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DangerRed)
                }

                Button(
                    onClick = onCloseShift,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("shift_close_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.LockClock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تقفيل الوردية", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun NoActiveShiftCard(onOpenShift: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
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
                    Icons.Default.LockClock,
                    contentDescription = null,
                    tint = RoseGoldPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "لا توجد وردية مفتوحة حالياً",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "ابدأ بفتح وردية جديدة وسجل عهدة الكاش الابتدائية لبدء البيع ومتابعة الخزينة بكل دقة.",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onOpenShift,
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(44.dp)
                    .testTag("start_shift_button")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("فتح وردية جديدة", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ClosedShiftCard(shift: ShiftEntity, sdf: SimpleDateFormat) {
    val diff = shift.differenceCash ?: 0.0

    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, CardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "وردية: ${shift.cashierName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = sdf.format(Date(shift.startTime)),
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "مبيعات: ${String.format(Locale.US, "%.0f", shift.totalCashSales + shift.totalElectronicSales)} ج.م",
                    fontSize = 13.sp,
                    color = RoseGoldPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                // Variance Badge
                val diffColor = when {
                    Math.abs(diff) < 0.01 -> SuccessGreen
                    diff > 0 -> ChampagneGold
                    else -> DangerRed
                }
                val diffBg = when {
                    Math.abs(diff) < 0.01 -> SuccessGreenLight
                    diff > 0 -> ChampagneGold.copy(alpha = 0.12f)
                    else -> DangerRedLight
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = diffBg
                ) {
                    Text(
                        text = when {
                            Math.abs(diff) < 0.01 -> "مطابقة 100%"
                            diff > 0 -> "زيادة: +${String.format(Locale.US, "%.0f", diff)} ج"
                            else -> "عجز: ${String.format(Locale.US, "%.0f", diff)} ج"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = diffColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
