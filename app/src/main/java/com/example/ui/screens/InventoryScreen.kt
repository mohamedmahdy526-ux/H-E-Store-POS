package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.SoftBlushCardVariant
import com.example.ui.theme.SuccessGreen
import java.util.Locale

@Composable
fun InventoryScreen(
    products: List<ProductEntity>,
    totalProductCount: Int,
    lowStockCount: Int,
    searchQuery: String,
    selectedCategory: String,
    isLowStockOnly: Boolean,
    onSearchChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onToggleLowStockOnly: (Boolean) -> Unit,
    onOpenAddProduct: () -> Unit,
    onOpenEditProduct: (ProductEntity) -> Unit,
    onOpenQuickReceive: (ProductEntity) -> Unit,
    onOpenReconciliation: (ProductEntity) -> Unit
) {
    val categories = listOf("الكل", "عيون", "شفايف", "بشرة", "عناية", "إكسسوارات", "عطور", "أطفال")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBlushBackground)
    ) {
        // Inventory KPI & Quick Action Bar
        Surface(
            color = SoftBlushCard,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                // Top KPI count & Add Product Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Total Count Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(SoftBlushBackground)
                                .border(1.dp, SoftBlushBorder, RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "إجمالي: $totalProductCount صنف",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalText
                            )
                        }

                        // Low Stock Alert Badge (< 3 قطع)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (lowStockCount > 0) DangerRed.copy(alpha = 0.12f) else SuccessGreen.copy(alpha = 0.12f))
                                .border(
                                    1.dp,
                                    if (lowStockCount > 0) DangerRed.copy(alpha = 0.35f) else SuccessGreen.copy(alpha = 0.25f),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onToggleLowStockOnly(!isLowStockOnly) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("low_stock_filter_badge")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "تحذير النواقص",
                                    tint = if (lowStockCount > 0) DangerRed else SuccessGreen,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "نواقص (< 3 قطع): $lowStockCount",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (lowStockCount > 0) DangerRed else SuccessGreen
                                )
                            }
                        }
                    }

                    // Add Product Button
                    Button(
                        onClick = onOpenAddProduct,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_product_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ صنف جديد", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Low Stock Alerts Notification Banner
                AnimatedVisibility(
                    visible = lowStockCount > 0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DangerRed.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, DangerRed.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clickable { onToggleLowStockOnly(!isLowStockOnly) }
                            .testTag("inventory_low_stock_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(DangerRed.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.NotificationsActive,
                                        contentDescription = "تنبيه نقص المخزون",
                                        tint = DangerRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "تنبيه: يوجد $lowStockCount منتج يقل رصيده عن 3 قطع بالمخزن!",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DangerRed
                                    )
                                    Text(
                                        text = if (isLowStockOnly) "جاري عرض الأصناف الحرجة • اضغط لإلغاء التصفية" else "اضغط هنا لتصفية الأصناف الحرجة ومتابعة توريدها",
                                        fontSize = 10.sp,
                                        color = CharcoalMuted
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isLowStockOnly) DangerRed else DangerRed.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = if (isLowStockOnly) "عرض الكل" else "تصفية النواقص",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isLowStockOnly) Color.White else DangerRed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("inventory_search_input"),
                    placeholder = { Text("ابحث في المخزون بالاسم أو الباركود...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = RoseGoldPrimary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChanged("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "مسح", tint = CharcoalMuted)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGoldPrimary,
                        unfocusedBorderColor = SoftBlushBorder,
                        focusedContainerColor = SoftBlushBackground,
                        unfocusedContainerColor = SoftBlushBackground
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Categories & Alert Filter Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Alert Filter Chip (< 3 items)
                    item {
                        val isAlertActive = isLowStockOnly
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isAlertActive) DangerRed else DangerRed.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, if (isAlertActive) DangerRed else DangerRed.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onToggleLowStockOnly(!isLowStockOnly) }
                                .testTag("filter_low_stock_chip")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (isAlertActive) Color.White else DangerRed,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "نواقص (< 3)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAlertActive) Color.White else DangerRed
                                )
                                if (lowStockCount > 0) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(if (isAlertActive) Color.White else DangerRed)
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "$lowStockCount",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isAlertActive) DangerRed else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory && !isLowStockOnly
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) RoseGoldPrimary else SoftBlushCardVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    if (isLowStockOnly) onToggleLowStockOnly(false)
                                    onCategorySelected(cat)
                                }
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else CharcoalText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }

        // Product Items List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products, key = { it.id }) { product ->
                InventoryItemCard(
                    product = product,
                    onEdit = { onOpenEditProduct(product) },
                    onQuickReceive = { onOpenQuickReceive(product) },
                    onReconcile = { onOpenReconciliation(product) }
                )
            }
        }
    }
}

private data class StockAlertData(
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String
)

@Composable
private fun InventoryItemCard(
    product: ProductEntity,
    onEdit: () -> Unit,
    onQuickReceive: () -> Unit,
    onReconcile: () -> Unit
) {
    val isOutOfStock = product.stockQuantity <= 0
    val isCriticalOne = product.stockQuantity == 1
    val isCriticalTwo = product.stockQuantity == 2
    val isBelowThree = product.stockQuantity < 3
    val isLowStock = isBelowThree || product.stockQuantity <= product.minStockAlert

    // Determine Alert Styling & Colored Icons
    val alertData = when {
        isOutOfStock -> StockAlertData(
            color = DangerRed,
            icon = Icons.Default.ErrorOutline,
            title = "نفاد المخزون تماماً (0 قطع)",
            subtitle = "الرصيد فارغ بالمحل — يلزم استلام وتوريد شحنة فوراً"
        )
        isCriticalOne -> StockAlertData(
            color = Color(0xFFD84315), // Deep Rust/Crimson Alert
            icon = Icons.Default.Warning,
            title = "تنبيه حرج: متبقي قطعة واحدة فقط!",
            subtitle = "الرصيد أقل من 3 قطع — أوشك الصنف على النفاد"
        )
        isCriticalTwo -> StockAlertData(
            color = Color(0xFFF57C00), // Vibrant Amber/Orange
            icon = Icons.Default.WarningAmber,
            title = "تنبيه نقص: متبقي قطعتين فقط!",
            subtitle = "الرصيد أقل من 3 قطع بالمخزن"
        )
        isLowStock -> StockAlertData(
            color = Color(0xFFE65100),
            icon = Icons.Default.WarningAmber,
            title = "تنبيه الحد الأدنى: ${product.stockQuantity} قطع",
            subtitle = "أقل من أو يساوي حد الأمان (${product.minStockAlert})"
        )
        else -> StockAlertData(
            color = SuccessGreen,
            icon = Icons.Default.CheckCircle,
            title = "المخزون متوفر",
            subtitle = "الرصيد كافٍ بالمخزن"
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isBelowThree) alertData.color.copy(alpha = 0.04f) else SoftBlushCard
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (isBelowThree) {
            BorderStroke(1.5.dp, alertData.color.copy(alpha = 0.7f))
        } else {
            BorderStroke(1.dp, SoftBlushBorder.copy(alpha = 0.6f))
        },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isBelowThree) 2.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("inventory_item_${product.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top Alert Banner Ribbon if stock < 3
            if (isBelowThree) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = alertData.color.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, alertData.color.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .testTag("low_stock_warning_banner_${product.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(alertData.color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = alertData.icon,
                                    contentDescription = "أيقونة تحذير المخزون",
                                    tint = alertData.color,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = alertData.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = alertData.color
                                )
                                Text(
                                    text = alertData.subtitle,
                                    fontSize = 9.sp,
                                    color = CharcoalMuted
                                )
                            }
                        }

                        // Quick Restock Pill
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = alertData.color,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onQuickReceive() }
                                .testTag("quick_restock_pill_${product.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "توريد",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Top row: Category, Barcode & Edit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseGoldDark,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(RoseGoldPrimary.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "باركود: ${product.barcode}",
                        fontSize = 10.sp,
                        color = CharcoalMuted
                    )
                }

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = RoseGoldPrimary, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Product Name with low stock colored warning dot/tag if under 3
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isBelowThree) {
                    Icon(
                        imageVector = alertData.icon,
                        contentDescription = "تحذير",
                        tint = alertData.color,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price & Cost & Stock Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column {
                        Text("سعر البيع", fontSize = 10.sp, color = CharcoalMuted)
                        Text(
                            "${String.format(Locale.US, "%.0f", product.sellPrice)} ج.م",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseGoldPrimary
                        )
                    }

                    Column {
                        Text("التكلفة", fontSize = 10.sp, color = CharcoalMuted)
                        Text(
                            "${String.format(Locale.US, "%.0f", product.costPrice)} ج.م",
                            fontSize = 13.sp,
                            color = CharcoalMuted
                        )
                    }

                    Column {
                        Text("الربح القطعة", fontSize = 10.sp, color = CharcoalMuted)
                        Text(
                            "+${String.format(Locale.US, "%.0f", product.sellPrice - product.costPrice)} ج",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SuccessGreen
                        )
                    }
                }

                // Current Stock Badge with Colored Warning Icon
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(alertData.color.copy(alpha = 0.14f))
                        .border(1.dp, alertData.color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("stock_badge_${product.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = alertData.icon,
                            contentDescription = "أيقونة رصيد المخزون",
                            tint = alertData.color,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.stockQuantity} ق بالمخزن",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = alertData.color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Operations: استلام بضاعة سريعة & أداة جرد دوري
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onQuickReceive,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBelowThree) alertData.color else RoseGoldPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Icon(
                        Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isBelowThree) "توريد عاجل (+)" else "استلام بضاعة",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onReconcile,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Icon(Icons.Default.Balance, contentDescription = null, modifier = Modifier.size(14.dp), tint = ChampagneGold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("جرد وتسوية", fontSize = 11.sp, color = CharcoalText)
                }
            }
        }
    }
}
