package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.model.CartItem
import com.example.ui.components.CompactCameraScanner
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceVariant
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.ChampagneGoldLight
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerRedLight
import com.example.ui.theme.RoseGoldDark
import com.example.ui.theme.RoseGoldLight
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.ui.theme.WarningOrangeLight
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
    onOpenScanner: () -> Unit = {},
    onBarcodeScanned: (String) -> Boolean = { false }
) {
    val categories = listOf("الكل", "عيون", "شفايف", "بشرة", "عناية", "إكسسوارات", "عطور", "أطفال")
    var isCompactCameraOpen by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Search & Category Header Bar
            Surface(
                color = CardSurface,
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Modern High-Contrast Search Input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("cashier_search_input"),
                        placeholder = {
                            Text(
                                "ابحث بالاسم، الباركود، أو التصنيف...",
                                fontSize = 14.sp,
                                color = TextMuted
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "بحث",
                                tint = RoseGoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { onSearchChanged("") },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(Icons.Default.Clear, contentDescription = "مسح", tint = TextSecondary)
                                }
                            } else {
                                IconButton(
                                    onClick = {
                                        isCompactCameraOpen = !isCompactCameraOpen
                                    },
                                    modifier = Modifier
                                        .size(44.dp)
                                        .testTag("cashier_open_camera_button")
                                ) {
                                    Icon(
                                        Icons.Default.QrCodeScanner,
                                        contentDescription = "مسح بالكاميرا المدمجة",
                                        tint = if (isCompactCameraOpen) RoseGoldPrimary else TextSecondary
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseGoldPrimary,
                            unfocusedBorderColor = CardBorder,
                            focusedContainerColor = CardSurfaceVariant,
                            unfocusedContainerColor = CardSurfaceVariant,
                            cursorColor = RoseGoldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Clean Category Filter Pills
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            val isSelected = cat == selectedCategory
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) RoseGoldPrimary else CardSurfaceVariant,
                                border = if (isSelected) null else BorderStroke(1.dp, CardBorder),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onCategorySelected(cat) }
                                    .testTag("category_chip_$cat")
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Compact CameraX Scanner Window (نافذة الكاميرا الصغيرة لمسح الباركود مع ظهور المنتجات تحتها)
            if (isCompactCameraOpen) {
                CompactCameraScanner(
                    products = products,
                    onBarcodeScanned = { barcode ->
                        onBarcodeScanned(barcode)
                    },
                    onClose = { isCompactCameraOpen = false }
                )
            }

            // Sub-Header for Vertical List
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "قائمة الأصناف",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${products.size} صنف)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = RoseGoldLight,
                    border = BorderStroke(1.dp, RoseGoldPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onOpenScanner() }
                        .testTag("cashier_quick_scan_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = RoseGoldPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "مسح باركود",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseGoldPrimary
                        )
                    }
                }
            }

            // Vertical Product Catalog (عمودي واحد ورا واحد ورا واحد)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (products.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(RoseGoldLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = RoseGoldPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "لا توجد منتجات مطابقة",
                            fontSize = 16.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "جرب البحث باسم آخر أو اختر تصنيفاً مختلفاً",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 14.dp,
                            end = 14.dp,
                            top = 4.dp,
                            bottom = if (cartTotalCount > 0) 84.dp else 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("products_vertical_list")
                    ) {
                        items(products, key = { it.id }) { product ->
                            VerticalProductCard(
                                product = product,
                                onAddToCart = { onAddToCart(product) }
                            )
                        }
                    }
                }
            }
        }

        // Floating Bottom Cart Bar (visible when items in cart, clean and dockable)
        if (cartTotalCount > 0 && !isCartSheetExpanded) {
            Surface(
                color = CardSurface,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CardBorder),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clickable { onToggleCartSheet() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RoseGoldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$cartTotalCount",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {
                            Text(
                                text = "سلة المبيعات الحالية",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "${String.format(Locale.US, "%.2f", cartNetTotal)} ج.م",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    Button(
                        onClick = onToggleCartSheet,
                        colors = ButtonDefaults.buttonColors(containerColor = RoseGoldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "عرض وتأكيد 🛒",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Expanded Cart Modal Sheet (Overlay)
        AnimatedVisibility(
            visible = isCartSheetExpanded,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                color = CardSurface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                border = BorderStroke(1.dp, CardBorder),
                shadowElevation = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    // Drag handle & Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = RoseGoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "محتويات السلة ($cartTotalCount أصناف)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (cartItems.isNotEmpty()) {
                                TextButton(
                                    onClick = onClearCart,
                                    modifier = Modifier.testTag("clear_cart_button")
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = DangerRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("تفريغ", fontSize = 12.sp, color = DangerRed)
                                }
                            }

                            IconButton(
                                onClick = onToggleCartSheet,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = TextSecondary)
                            }
                        }
                    }

                    HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                    if (cartItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "السلة فارغة. اضغط على أي منتج لإضافته مباشرة.",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    } else {
                        // Cart Items List
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(cartItems, key = { it.product.id }) { item ->
                                Surface(
                                    color = CardSurfaceVariant,
                                    shape = RoundedCornerShape(12.dp),
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
                                            Text(
                                                text = item.product.name,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextPrimary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "${String.format(Locale.US, "%.2f", item.unitPrice)} ج.م للقطعة",
                                                fontSize = 12.sp,
                                                color = RoseGoldPrimary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        // Stepper Controls with 40dp touch target
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(CardSurface)
                                                    .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                                                    .clickable { onUpdateCartQty(item.product.id, -1) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Remove,
                                                    contentDescription = "تقليل",
                                                    tint = TextPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            Text(
                                                text = "${item.quantity}",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(CardSurface)
                                                    .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                                                    .clickable { onUpdateCartQty(item.product.id, 1) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Add,
                                                    contentDescription = "زيادة",
                                                    tint = TextPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = { onRemoveCartItem(item.product.id) },
                                                modifier = Modifier.size(34.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "حذف",
                                                    tint = DangerRed,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                    // Summary & Checkout CTA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الإجمالي الصافي:",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${String.format(Locale.US, "%.2f", cartNetTotal)} ج.م",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onOpenCheckout,
                        enabled = cartItems.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("checkout_main_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoseGoldPrimary,
                            disabledContainerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تحصيل الحساب ودفع الفاتورة",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VerticalProductCard(
    product: ProductEntity,
    onAddToCart: () -> Unit
) {
    val isLowStock = product.stockQuantity <= product.minStockAlert
    val isOutOfStock = product.stockQuantity <= 0

    val categoryIcon = when (product.category) {
        "بشرة" -> Icons.Default.Spa
        "شفايف" -> Icons.Default.AutoAwesome
        "عيون" -> Icons.Default.Visibility
        "إكسسوارات" -> Icons.Default.Diamond
        "عطور" -> Icons.Default.LocalFlorist
        else -> Icons.Default.ShoppingBag
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = !isOutOfStock) { onAddToCart() }
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Category Icon Thumbnail
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardSurfaceVariant)
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    categoryIcon,
                    contentDescription = null,
                    tint = if (isOutOfStock) TextMuted else RoseGoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info Column: Name, Category, Stock & Barcode
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOutOfStock) TextMuted else TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = product.category,
                        fontSize = 10.sp,
                        color = RoseGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(RoseGoldLight)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )

                    val (badgeBg, badgeColor, badgeText) = when {
                        isOutOfStock -> Triple(DangerRedLight, DangerRed, "نفد")
                        isLowStock -> Triple(WarningOrangeLight, WarningOrange, "${product.stockQuantity} ق")
                        else -> Triple(SuccessGreenLight, SuccessGreen, "${product.stockQuantity} ق")
                    }

                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )

                    Text(
                        text = "#${product.barcode}",
                        fontSize = 10.sp,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Trailing: Price & Add To Cart Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${String.format(Locale.US, "%.0f", product.sellPrice)} ج.م",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOutOfStock) TextMuted else TextPrimary
                )

                IconButton(
                    onClick = onAddToCart,
                    enabled = !isOutOfStock,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isOutOfStock) CardBorder else RoseGoldPrimary)
                        .testTag("add_to_cart_${product.id}")
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "إضافة للسلة",
                        tint = if (isOutOfStock) TextMuted else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
