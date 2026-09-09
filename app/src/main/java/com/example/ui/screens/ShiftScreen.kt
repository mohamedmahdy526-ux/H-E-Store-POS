package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExpenseEntity
import com.example.data.ShiftEntity
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
            .background(SoftBlushBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        color = CharcoalText
                    )
                    OutlinedButton(
                        onClick = onAddExpenseClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.MoneyOff, contentDescription = null, modifier = Modifier.size(14.dp), tint = DangerRed)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ تسجيل مصروف", fontSize = 11.sp, color = DangerRed)
                    }
                }
            }

            val currentExpenses = expenses.filter { it.shiftId == activeShift.id }
            if (currentExpenses.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "لم يتم تسجيل أي مصروفات أو سحوبات نقدية في هذه الوردية بعد.",
                            fontSize = 12.sp,
                            color = CharcoalMuted,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            } else {
                items(currentExpenses) { expense ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (expense.type == "EXPENSE") "نثريات" else "سحب كاش",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (expense.type == "EXPENSE") RoseGoldDark else DangerRed,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (expense.type == "EXPENSE") RoseGoldPrimary.copy(alpha = 0.1f)
                                                else DangerRed.copy(alpha = 0.1f)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = expense.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CharcoalText
                                    )
                                }
                                if (expense.notes.isNotBlank()) {
                                    Text(
                                        text = expense.notes,
                                        fontSize = 11.sp,
                                        color = CharcoalMuted
                                    )
                                }
                            }

                            Text(
                                text = "-${String.format(Locale.US, "%.2f", expense.amount)} ج.م",
                                fontSize = 14.sp,
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
                Icon(Icons.Default.History, contentDescription = null, tint = RoseGoldPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "سجل الورديات السابقة المقفلة",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalText
                )
            }
        }

        val closedShifts = shiftHistory.filter { !it.isOpen }
        if (closedShifts.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "لا توجد ورديات سابقة مؤرشفة حتى الآن.",
                        fontSize = 12.sp,
                        color = CharcoalMuted,
                        modifier = Modifier.padding(14.dp)
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
        colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "الوردية الحالية نشطة",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }

                Text(
                    text = "الكاشير: ${shift.cashierName}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CharcoalText
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Big Cash Drawer Counter
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(RoseGoldPrimary.copy(alpha = 0.08f))
                    .border(1.dp, RoseGoldPrimary.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "الكاش المتوقع حالياً بالدرج",
                        fontSize = 12.sp,
                        color = CharcoalMuted
                    )
                    Text(
                        text = "${String.format(Locale.US, "%.2f", expectedCash)} ج.م",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoseGoldDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Breakdown Grid
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("العهدة الابتدائية:", fontSize = 12.sp, color = CharcoalMuted)
                    Text("${String.format(Locale.US, "%.2f", shift.openingBalance)} ج.م", fontSize = 12.sp, color = CharcoalText)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("مبيعات كاش:", fontSize = 12.sp, color = CharcoalMuted)
                    Text("+${String.format(Locale.US, "%.2f", shift.totalCashSales)} ج.م", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("مبيعات إلكترونية (إنستاباي / فيزا):", fontSize = 12.sp, color = CharcoalMuted)
                    Text("${String.format(Locale.US, "%.2f", shift.totalElectronicSales)} ج.م", fontSize = 12.sp, color = ChampagneGold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("المصروفات والسحوبات:", fontSize = 12.sp, color = CharcoalMuted)
                    Text("-${String.format(Locale.US, "%.2f", shift.totalExpenses + shift.totalWithdrawals)} ج.م", fontSize = 12.sp, color = DangerRed)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.MoneyOff, contentDescription = null, tint = DangerRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تسجيل مصروف", fontSize = 12.sp, color = DangerRed)
                }

                Button(
                    onClick = onCloseShift,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("shift_close_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.LockClock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تقفيل الوردية", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun NoActiveShiftCard(onOpenShift: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.LockClock,
                contentDescription = null,
                tint = CharcoalMuted,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "لا توجد وردية مفتوحة حالياً",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalText
            )
            Text(
                text = "ابدأ بفتح وردية جديدة وسجل عهدة الكاش الابتدائية لبدء البيع ومتابعة الخزينة.",
                fontSize = 12.sp,
                color = CharcoalMuted,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onOpenShift,
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("start_shift_button")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("فتح وردية جديدة", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ClosedShiftCard(shift: ShiftEntity, sdf: SimpleDateFormat) {
    val diff = shift.differenceCash ?: 0.0

    Card(
        colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
        shape = RoundedCornerShape(14.dp),
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
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalText
                )
                Text(
                    text = sdf.format(Date(shift.startTime)),
                    fontSize = 11.sp,
                    color = CharcoalMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "مبيعات: ${String.format(Locale.US, "%.0f", shift.totalCashSales + shift.totalElectronicSales)} ج.م",
                    fontSize = 12.sp,
                    color = RoseGoldPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                // Variance Badge
                val diffColor = when {
                    Math.abs(diff) < 0.01 -> SuccessGreen
                    diff > 0 -> ChampagneGold
                    else -> DangerRed
                }
                Text(
                    text = when {
                        Math.abs(diff) < 0.01 -> "مطابقة 100%"
                        diff > 0 -> "زيادة: +${String.format(Locale.US, "%.0f", diff)} ج"
                        else -> "عجز: ${String.format(Locale.US, "%.0f", diff)} ج"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = diffColor
                )
            }
        }
    }
}
