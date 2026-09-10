package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.model.CartItem
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
fun CashierScreen(
    products: List<ProductEntity>,
    selectedCategory: String,
    searchQuery: String,
    cartItems: List<CartItem>,
    cartSubtotal: Double,
    cartNetTotal: Double,
    overallDiscount: Double,
    cartTotalCount: Int,
    onCategorySelected: (String) -> Unit,
    onSearchChanged: (String) -> Unit,
    onAddToCart: (ProductEntity) -> Unit,
    onUpdateCartQty: (productId: Long, delta: Int) -> Unit,
    onRemoveCartItem: (productId: Long) -> Unit,
    onClearCart: () -> Unit,
    onOpenCheckout: () -> Unit,
    isCartSheetExpanded: Boolean,
    onToggleCartSheet: () -> Unit,
    onOpenScanner: () -> Unit = {}
) {
    val context = LocalContext.current
    val categories = listOf("الكل", "عيون", "شفايف", "بشرة", "عناية", "إكسسوارات", "عطور", "أطفال")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBlushBackground)
    ) {
        // Search Bar & Filter Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SoftBlushCard)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cashier_search_input"),
                placeholder = { Text("ابحث بالاسم، الباركود، أو الفئة...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "بحث", tint = RoseGoldPrimary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "مسح", tint = CharcoalMuted)
                        }
                    } else {
                        IconButton(
                            onClick = onOpenScanner,
                            modifier = Modifier.testTag("cashier_open_camera_button")
                        ) {
                            Icon(
                                Icons.Default.QrCodeScanner,
                                contentDescription = "مسح بالكاميرا",
                                tint = RoseGoldPrimary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoseGoldPrimary,
                    unfocusedBorderColor = SoftBlushBorder,
                    focusedContainerColor = SoftBlushBackground,
                    unfocusedContainerColor = SoftBlushBackground
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) RoseGoldPrimary else SoftBlushCardVariant,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, SoftBlushBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onCategorySelected(cat) }
                            .testTag("category_chip_$cat")
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else CharcoalText,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Product Catalog Grid
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (products.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = CharcoalMuted.copy(alpha = 0.5f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "لا توجد منتجات مطابقة للبحث أو الفئة",
                        fontSize = 14.sp,
                        color = CharcoalMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("products_grid")
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = { onAddToCart(product) }
                        )
                    }
                }
            }
        }

        // Bottom Cart Sheet
        CartBottomBar(
            cartItems = cartItems,
            cartSubtotal = cartSubtotal,
            cartNetTotal = cartNetTotal,
            overallDiscount = overallDiscount,
            cartTotalCount = cartTotalCount,
            isExpanded = isCartSheetExpanded,
            onToggleExpand = onToggleCartSheet,
            onUpdateQty = onUpdateCartQty,
            onRemoveItem = onRemoveCartItem,
            onClearCart = onClearCart,
            onCheckout = onOpenCheckout
        )
    }
}

@Composable
private fun ProductCard(
    product: ProductEntity,
    onAddToCart: () -> Unit
) {
    val isLowStock = product.stockQuantity <= product.minStockAlert
    val isOutOfStock = product.stockQuantity <= 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = !isOutOfStock) { onAddToCart() }
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(containerColor = SoftBlushCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Category tag and Stock badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.category,
                    fontSize = 10.sp,
                    color = RoseGoldDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(RoseGoldPrimary.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )

                // Stock Badge
                val badgeColor = when {
                    isOutOfStock -> DangerRed
                    isLowStock -> Color(0xFFE65100)
                    else -> SuccessGreen
                }
                Text(
                    text = when {
                        isOutOfStock -> "نفد"
                        isLowStock -> "${product.stockQuantity} ق (ناقص)"
                        else -> "${product.stockQuantity} ق"
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Name
            Text(
                text = product.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Price & Add button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${String.format(Locale.US, "%.0f", product.sellPrice)} ج.م",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoseGoldPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isOutOfStock) Color.LightGray else RoseGoldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "إضافة للسلة",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CartBottomBar(
    cartItems: List<CartItem>,
    cartSubtotal: Double,
    cartNetTotal: Double,
    overallDiscount: Double,
    cartTotalCount: Int,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onUpdateQty: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onClearCart: () -> Unit,
    onCheckout: () -> Unit
) {
    Surface(
        color = SoftBlushCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Cart Header Bar (Always visible)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = RoseGoldPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "سلة المبيعات ($cartTotalCount صنف)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${String.format(Locale.US, "%.2f", cartNetTotal)} ج.م",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RoseGoldPrimary
                    )
                }
            }

            // Expanded Cart Drawer
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    HorizontalDivider(color = SoftBlushBorder)
                    Spacer(modifier = Modifier.height(6.dp))

                    if (cartItems.isEmpty()) {
                        Text(
                            text = "السلة فارغة. اضغط على أي منتج لإضافته فوراً.",
                            fontSize = 12.sp,
                            color = CharcoalMuted,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        // Clear Cart action
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = onClearCart,
                                modifier = Modifier.testTag("clear_cart_button")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = DangerRed, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("تفريغ السلة", fontSize = 11.sp, color = DangerRed)
                            }
                        }

                        // List of items in cart
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            items(cartItems, key = { it.product.id }) { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.product.name,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = CharcoalText,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${String.format(Locale.US, "%.2f", item.unitPrice)} ج.م",
                                            fontSize = 11.sp,
                                            color = RoseGoldDark
                                        )
                                    }

                                    // Quantity Controls
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(SoftBlushBackground)
                                                .border(1.dp, SoftBlushBorder, CircleShape)
                                                .clickable { onUpdateQty(item.product.id, -1) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = "تقليل", tint = CharcoalText, modifier = Modifier.size(14.dp))
                                        }

                                        Text(
                                            text = "${item.quantity}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CharcoalText
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(SoftBlushBackground)
                                                .border(1.dp, SoftBlushBorder, CircleShape)
                                                .clickable { onUpdateQty(item.product.id, 1) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "زيادة", tint = CharcoalText, modifier = Modifier.size(14.dp))
                                        }

                                        IconButton(
                                            onClick = { onRemoveItem(item.product.id) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "حذف", tint = DangerRed, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Checkout CTA Button
            Button(
                onClick = onCheckout,
                enabled = cartItems.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("checkout_main_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoseGoldPrimary,
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تحصيل الحساب (${String.format(Locale.US, "%.2f", cartNetTotal)} ج.م)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
