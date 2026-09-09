package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
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

                        // Low Stock Alert Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (lowStockCount > 0) DangerRed.copy(alpha = 0.12f) else SuccessGreen.copy(alpha = 0.12f))
                                .clickable { onToggleLowStockOnly(!isLowStockOnly) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("low_stock_filter_badge")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (lowStockCount > 0) DangerRed else SuccessGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "نواقص: $lowStockCount",
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

                // Categories Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) RoseGoldPrimary else SoftBlushCardVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onCategorySelected(cat) }
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

@Composable
private fun InventoryItemCard(
    product: ProductEntity,
    onEdit: () -> Unit,
    onQuickReceive: () -> Unit,
    onReconcile: () -> Unit
) {
    val isLowStock = product.stockQuantity <= product.minStockAlert
    val isOutOfStock = product.stockQuantity <= 0

    Card(
        colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("inventory_item_${product.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top row: Category, Name & Edit
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

            // Product Name
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalText
            )

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

                // Current Stock Badge
                val stockColor = when {
                    isOutOfStock -> DangerRed
                    isLowStock -> Color(0xFFE65100)
                    else -> SuccessGreen
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(stockColor.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${product.stockQuantity} ق بالمخزن",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = stockColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Operations: استلام بضاعة سريعة & أداة جرد دوري
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onQuickReceive,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(14.dp), tint = RoseGoldPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("استلام بضاعة", fontSize = 11.sp, color = RoseGoldPrimary)
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
