package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.model.PaymentMethod
import com.example.ui.StoreViewModel
import com.example.ui.components.BarcodeScannerDialog
import com.example.ui.components.CheckoutDialog
import com.example.ui.components.CloseShiftDialog
import com.example.ui.components.OpenShiftDialog
import com.example.ui.components.ProductFormDialog
import com.example.ui.components.QuickReceiveStockDialog
import com.example.ui.components.ReceiptDialog
import com.example.ui.components.ReconcileStockDialog
import com.example.ui.components.RecordExpenseDialog
import com.example.ui.components.StoreHeader
import com.example.ui.screens.CashierScreen
import com.example.ui.screens.InventoryScreen
import com.example.ui.screens.ManagerScreen
import com.example.ui.screens.ShiftScreen
import com.example.ui.theme.HEStoreTheme
import com.example.ui.theme.RoseGoldDark
import com.example.ui.theme.RoseGoldPrimary
import com.example.ui.theme.SoftBlushBackground
import com.example.ui.theme.SoftBlushBorder
import com.example.ui.theme.SoftBlushCard

enum class StoreNavScreen(val titleAr: String) {
    CASHIER("الكاشير"),
    INVENTORY("المخزون"),
    SHIFT("الوردية"),
    MANAGER("الإدارة")
}

class MainActivity : ComponentActivity() {

    private val viewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HEStoreTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    StoreApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun StoreApp(viewModel: StoreViewModel) {
    val context = LocalContext.current

    // Observe ViewModel State
    val allProducts by viewModel.allProducts.collectAsState()
    val lowStockProducts by viewModel.lowStockProducts.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLowStockOnly by viewModel.showOnlyLowStock.collectAsState()

    val cartItems by viewModel.cartItems.collectAsState()
    val cartSubtotal by viewModel.cartSubtotal.collectAsState()
    val cartNetTotal by viewModel.cartNetTotal.collectAsState()
    val cartTotalCount by viewModel.cartTotalCount.collectAsState()
    val overallDiscount by viewModel.overallDiscount.collectAsState()
    val currentReceipt by viewModel.currentReceipt.collectAsState()

    val activeShift by viewModel.activeShift.collectAsState()
    val allShifts by viewModel.allShifts.collectAsState()
    val allExpenses by viewModel.allExpenses.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    val isManagerUnlocked by viewModel.isManagerUnlocked.collectAsState()
    val dashboardPeriod by viewModel.dashboardPeriod.collectAsState()

    // Navigation & UI dialogs state
    var currentScreen by remember { mutableStateOf(StoreNavScreen.CASHIER) }
    var isCartSheetExpanded by remember { mutableStateOf(false) }

    var showScannerDialog by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }

    var productForQuickReceive by remember { mutableStateOf<ProductEntity?>(null) }
    var productForReconciliation by remember { mutableStateOf<ProductEntity?>(null) }
    var productForEdit by remember { mutableStateOf<ProductEntity?>(null) }
    var showAddProductDialog by remember { mutableStateOf(false) }

    var showOpenShiftDialog by remember { mutableStateOf(false) }
    var showExpenseDialog by remember { mutableStateOf(false) }
    var showCloseShiftDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            StoreHeader(
                activeShift = activeShift,
                cartCount = cartTotalCount,
                onOpenScanner = { showScannerDialog = true },
                onCartClicked = {
                    currentScreen = StoreNavScreen.CASHIER
                    isCartSheetExpanded = !isCartSheetExpanded
                }
            )
        },
        bottomBar = {
            Surface(
                color = SoftBlushCard,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                NavigationBar(
                    containerColor = SoftBlushCard,
                    tonalElevation = 0.dp,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.CASHIER,
                        onClick = { currentScreen = StoreNavScreen.CASHIER },
                        icon = {
                            Icon(
                                Icons.Default.PointOfSale,
                                contentDescription = "الكاشير",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = { Text("الكاشير", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = RoseGoldPrimary,
                            selectedTextColor = RoseGoldDark,
                            indicatorColor = RoseGoldPrimary.copy(alpha = 0.15f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_cashier")
                    )

                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.INVENTORY,
                        onClick = { currentScreen = StoreNavScreen.INVENTORY },
                        icon = {
                            Icon(
                                Icons.Default.Inventory2,
                                contentDescription = "المخزون",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = { Text("المخزون", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = RoseGoldPrimary,
                            selectedTextColor = RoseGoldDark,
                            indicatorColor = RoseGoldPrimary.copy(alpha = 0.15f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_inventory")
                    )

                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.SHIFT,
                        onClick = { currentScreen = StoreNavScreen.SHIFT },
                        icon = {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = "الوردية",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = { Text("الوردية", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = RoseGoldPrimary,
                            selectedTextColor = RoseGoldDark,
                            indicatorColor = RoseGoldPrimary.copy(alpha = 0.15f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_shift")
                    )

                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.MANAGER,
                        onClick = { currentScreen = StoreNavScreen.MANAGER },
                        icon = {
                            Icon(
                                Icons.Default.Assessment,
                                contentDescription = "الإدارة",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = { Text("الإدارة", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = RoseGoldPrimary,
                            selectedTextColor = RoseGoldDark,
                            indicatorColor = RoseGoldPrimary.copy(alpha = 0.15f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_manager")
                    )
                }
            }
        },
        containerColor = SoftBlushBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentScreen) {
                StoreNavScreen.CASHIER -> {
                    CashierScreen(
                        products = filteredProducts,
                        selectedCategory = selectedCategory,
                        searchQuery = searchQuery,
                        cartItems = cartItems,
                        cartSubtotal = cartSubtotal,
                        cartNetTotal = cartNetTotal,
                        overallDiscount = overallDiscount,
                        cartTotalCount = cartTotalCount,
                        onCategorySelected = { viewModel.setSelectedCategory(it) },
                        onSearchChanged = { viewModel.setSearchQuery(it) },
                        onAddToCart = { product -> viewModel.addToCart(product, context) },
                        onUpdateCartQty = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                        onRemoveCartItem = { id -> viewModel.removeCartItem(id) },
                        onClearCart = { viewModel.clearCart() },
                        onOpenCheckout = { showCheckoutDialog = true },
                        isCartSheetExpanded = isCartSheetExpanded,
                        onToggleCartSheet = { isCartSheetExpanded = !isCartSheetExpanded }
                    )
                }

                StoreNavScreen.INVENTORY -> {
                    InventoryScreen(
                        products = filteredProducts,
                        totalProductCount = allProducts.size,
                        lowStockCount = lowStockProducts.size,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        isLowStockOnly = isLowStockOnly,
                        onSearchChanged = { viewModel.setSearchQuery(it) },
                        onCategorySelected = { viewModel.setSelectedCategory(it) },
                        onToggleLowStockOnly = { viewModel.toggleShowOnlyLowStock(it) },
                        onOpenAddProduct = { showAddProductDialog = true },
                        onOpenEditProduct = { productForEdit = it },
                        onOpenQuickReceive = { productForQuickReceive = it },
                        onOpenReconciliation = { productForReconciliation = it }
                    )
                }

                StoreNavScreen.SHIFT -> {
                    ShiftScreen(
                        activeShift = activeShift,
                        expenses = allExpenses,
                        shiftHistory = allShifts,
                        onOpenShiftClick = { showOpenShiftDialog = true },
                        onAddExpenseClick = { showExpenseDialog = true },
                        onCloseShiftClick = { showCloseShiftDialog = true }
                    )
                }

                StoreNavScreen.MANAGER -> {
                    ManagerScreen(
                        isUnlocked = isManagerUnlocked,
                        orders = allOrders,
                        products = allProducts,
                        period = dashboardPeriod,
                        onPeriodSelected = { viewModel.setDashboardPeriod(it) },
                        onVerifyPin = { pin -> viewModel.verifyPin(pin) },
                        onLock = { viewModel.lockManager() },
                        onChangePin = { newPin -> viewModel.updateManagerPin(newPin) },
                        onExportBackup = { viewModel.exportBackupJson() },
                        onRestoreDefaults = { viewModel.resetCatalogToDefaults() }
                    )
                }
            }
        }
    }

    // --- Modal Dialogs ---

    // 1. Barcode Scanner Dialog
    if (showScannerDialog) {
        BarcodeScannerDialog(
            sampleProducts = allProducts,
            onBarcodeDetected = { barcode ->
                viewModel.onBarcodeScanned(barcode, context)
            },
            onDismiss = { showScannerDialog = false }
        )
    }

    // 2. Checkout Dialog
    if (showCheckoutDialog) {
        CheckoutDialog(
            subtotal = cartSubtotal,
            currentDiscount = overallDiscount,
            onDiscountChanged = { viewModel.setOverallDiscount(it) },
            onConfirmSale = { method, cashPaid ->
                viewModel.completeCheckout(method, cashPaid, context)
                showCheckoutDialog = false
                isCartSheetExpanded = false
            },
            onDismiss = { showCheckoutDialog = false }
        )
    }

    // 3. Receipt Dialog
    currentReceipt?.let { receipt ->
        ReceiptDialog(
            receipt = receipt,
            onShare = { r -> viewModel.shareReceipt(r, context) },
            onDismiss = { viewModel.dismissReceipt() }
        )
    }

    // 4. Quick Receive Stock Dialog
    productForQuickReceive?.let { product ->
        QuickReceiveStockDialog(
            product = product,
            onConfirmAdd = { qty ->
                viewModel.quickReceiveStock(product.id, qty, context)
            },
            onDismiss = { productForQuickReceive = null }
        )
    }

    // 5. Reconcile Stock Dialog
    productForReconciliation?.let { product ->
        ReconcileStockDialog(
            product = product,
            onConfirmReconciliation = { actualShelfStock ->
                viewModel.reconcileStock(product.id, actualShelfStock)
            },
            onDismiss = { productForReconciliation = null }
        )
    }

    // 6. Product Add / Edit Dialog
    if (showAddProductDialog || productForEdit != null) {
        ProductFormDialog(
            initialProduct = productForEdit,
            onSave = { name, barcode, category, sell, cost, stock, minStock, desc ->
                viewModel.saveProduct(
                    id = productForEdit?.id ?: 0L,
                    name = name,
                    barcode = barcode,
                    category = category,
                    sellPrice = sell,
                    costPrice = cost,
                    stockQuantity = stock,
                    minStockAlert = minStock,
                    description = desc
                )
                showAddProductDialog = false
                productForEdit = null
            },
            onDismiss = {
                showAddProductDialog = false
                productForEdit = null
            }
        )
    }

    // 7. Open Shift Dialog
    if (showOpenShiftDialog) {
        OpenShiftDialog(
            onConfirm = { cashier, balance, notes ->
                viewModel.openShift(cashier, balance, notes)
            },
            onDismiss = { showOpenShiftDialog = false }
        )
    }

    // 8. Expense / Withdrawal Dialog
    if (showExpenseDialog) {
        RecordExpenseDialog(
            onConfirm = { type, title, amount, notes ->
                viewModel.recordExpenseOrWithdrawal(type, title, amount, notes)
            },
            onDismiss = { showExpenseDialog = false }
        )
    }

    // 9. Close Shift Dialog
    if (showCloseShiftDialog && activeShift != null) {
        CloseShiftDialog(
            shift = activeShift!!,
            onConfirmClose = { countedCash, notes ->
                viewModel.closeActiveShift(countedCash, notes)
            },
            onDismiss = { showCloseShiftDialog = false }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
