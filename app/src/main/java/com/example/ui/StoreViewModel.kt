package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ExpenseEntity
import com.example.data.OrderEntity
import com.example.data.ProductEntity
import com.example.data.ShiftEntity
import com.example.data.StoreRepository
import com.example.model.CartItem
import com.example.model.PaymentMethod
import com.example.model.SaleReceipt
import com.example.util.SoundHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale
import java.util.UUID

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StoreRepository

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = StoreRepository(database.appDao())

        // Initial check to ensure products are seeded
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getProductCount() == 0) {
                repository.resetDatabaseToDefaults()
            }
        }
    }

    // --- State: Products & Filters ---
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockProducts: StateFlow<List<ProductEntity>> = repository.lowStockProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeShift: StateFlow<ShiftEntity?> = repository.activeShift
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allShifts: StateFlow<List<ShiftEntity>> = repository.allShifts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allExpenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCategory = MutableStateFlow("الكل")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showOnlyLowStock = MutableStateFlow(false)
    val showOnlyLowStock: StateFlow<Boolean> = _showOnlyLowStock.asStateFlow()

    // Filtered Products for Cashier & Inventory Screens
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        _selectedCategory,
        _searchQuery,
        _showOnlyLowStock
    ) { products, category, query, onlyLowStock ->
        products.filter { p ->
            val matchesCategory = (category == "الكل" || p.category == category)
            val matchesQuery = query.isBlank() ||
                    p.name.contains(query, ignoreCase = true) ||
                    p.barcode.contains(query.trim()) ||
                    p.category.contains(query, ignoreCase = true)
            val matchesLowStock = !onlyLowStock || p.stockQuantity <= p.minStockAlert
            matchesCategory && matchesQuery && matchesLowStock
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleShowOnlyLowStock(enabled: Boolean) {
        _showOnlyLowStock.value = enabled
    }

    // --- State: Cart ---
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _overallDiscount = MutableStateFlow(0.0) // Flat discount in EGP
    val overallDiscount: StateFlow<Double> = _overallDiscount.asStateFlow()

    val cartSubtotal: StateFlow<Double> = _cartItems.combine(_overallDiscount) { items, _ ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartNetTotal: StateFlow<Double> = combine(_cartItems, _overallDiscount) { items, discount ->
        val sub = items.sumOf { it.totalPrice }
        maxOf(0.0, sub - discount)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartTotalCount: StateFlow<Int> = _cartItems.combine(_overallDiscount) { items, _ ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addToCart(product: ProductEntity, context: Context? = null) {
        SoundHelper.playScannerBeep(context)
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val item = current[index]
            current[index] = item.copy(quantity = item.quantity + 1)
        } else {
            current.add(CartItem(product = product, quantity = 1))
        }
        _cartItems.value = current
    }

    fun updateCartQuantity(productId: Long, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index != -1) {
            val item = current[index]
            val newQty = item.quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = item.copy(quantity = newQty)
            }
            _cartItems.value = current
        }
    }

    fun updateItemDiscount(productId: Long, discountPerItem: Double) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index != -1) {
            current[index] = current[index].copy(discountPerItem = maxOf(0.0, discountPerItem))
            _cartItems.value = current
        }
    }

    fun removeCartItem(productId: Long) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _overallDiscount.value = 0.0
    }

    fun setOverallDiscount(discount: Double) {
        _overallDiscount.value = maxOf(0.0, discount)
    }

    // Barcode Scanner Action
    fun onBarcodeScanned(barcode: String, context: Context): Boolean {
        val trimmed = barcode.trim()
        val product = allProducts.value.find { it.barcode == trimmed }
        return if (product != null) {
            addToCart(product, context)
            Toast.makeText(context, "تمت إضافة: ${product.name}", Toast.LENGTH_SHORT).show()
            true
        } else {
            Toast.makeText(context, "لم يتم العثور على منتج بهذا الباركود: $trimmed", Toast.LENGTH_SHORT).show()
            false
        }
    }

    // --- Checkout & Sale Completion ---
    private val _currentReceipt = MutableStateFlow<SaleReceipt?>(null)
    val currentReceipt: StateFlow<SaleReceipt?> = _currentReceipt.asStateFlow()

    fun dismissReceipt() {
        _currentReceipt.value = null
    }

    fun completeCheckout(
        paymentMethod: PaymentMethod,
        cashPaid: Double,
        context: Context
    ) {
        val items = _cartItems.value
        if (items.isEmpty()) return

        val subtotal = items.sumOf { it.totalPrice }
        val discount = _overallDiscount.value
        val netTotal = maxOf(0.0, subtotal - discount)
        val totalCost = items.sumOf { it.totalCost }
        val change = if (paymentMethod == PaymentMethod.CASH) maxOf(0.0, cashPaid - netTotal) else 0.0
        val currentShift = activeShift.value
        val shiftId = currentShift?.id ?: 0L
        val cashierName = currentShift?.cashierName ?: "كاشير"

        // Build item summary
        val summaryList = items.map { "${it.quantity}x ${it.product.name} (${it.unitPrice} ج.م)" }
        val itemsSummary = summaryList.joinToString(" | ")

        val orderNumber = (1000 + (System.currentTimeMillis() % 9000)).toString()
        val order = OrderEntity(
            orderNumber = orderNumber,
            timestamp = System.currentTimeMillis(),
            totalAmount = subtotal,
            discountAmount = discount,
            netAmount = netTotal,
            totalCost = totalCost,
            paymentMethod = paymentMethod.code,
            cashPaid = if (paymentMethod == PaymentMethod.CASH) cashPaid else netTotal,
            changeAmount = change,
            shiftId = shiftId,
            cashierName = cashierName,
            itemsSummary = itemsSummary
        )

        viewModelScope.launch(Dispatchers.IO) {
            val deductions = items.map { Pair(it.product.id, it.quantity) }
            repository.completeSale(order, deductions)

            // Play success tone
            SoundHelper.playSuccessBeep()

            // Generate receipt
            val receipt = SaleReceipt(
                orderNumber = orderNumber,
                timestamp = order.timestamp,
                items = items,
                subtotal = subtotal,
                discount = discount,
                totalAmount = netTotal,
                paymentMethod = paymentMethod,
                cashPaid = order.cashPaid,
                changeAmount = change,
                cashierName = cashierName
            )
            _currentReceipt.value = receipt
            clearCart()
        }
    }

    // Share Receipt via WhatsApp or Android Share Sheet
    fun shareReceipt(receipt: SaleReceipt, context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "فاتورة H&E Store #${receipt.orderNumber}")
            putExtra(Intent.EXTRA_TEXT, receipt.toShareableText())
        }
        val chooser = Intent.createChooser(shareIntent, "مشاركة الفاتورة عبر:")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    // --- Inventory Operations ---
    fun saveProduct(
        id: Long = 0,
        name: String,
        barcode: String,
        category: String,
        sellPrice: Double,
        costPrice: Double,
        stockQuantity: Int,
        minStockAlert: Int = 3,
        description: String = ""
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = ProductEntity(
                id = id,
                name = name.trim(),
                barcode = barcode.trim(),
                category = category,
                sellPrice = sellPrice,
                costPrice = costPrice,
                stockQuantity = stockQuantity,
                minStockAlert = minStockAlert,
                description = description
            )
            if (id == 0L) {
                repository.insertProduct(product)
            } else {
                repository.updateProduct(product)
            }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
        }
    }

    fun quickReceiveStock(productId: Long, addedQuantity: Int, context: Context? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.receiveStock(productId, addedQuantity)
            SoundHelper.playSuccessBeep()
        }
    }

    fun reconcileStock(productId: Long, actualShelfStock: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.reconcileStock(productId, actualShelfStock)
            SoundHelper.playSuccessBeep()
        }
    }

    // --- Shift Operations ---
    fun openShift(cashierName: String, openingBalance: Double, notes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.openShift(cashierName, openingBalance, notes)
            SoundHelper.playSuccessBeep()
        }
    }

    fun recordExpenseOrWithdrawal(
        type: String, // "EXPENSE" or "WITHDRAWAL"
        title: String,
        amount: Double,
        notes: String
    ) {
        val shift = activeShift.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.addExpenseOrWithdrawal(shift.id, type, title, amount, notes)
            SoundHelper.playSuccessBeep()
        }
    }

    fun closeActiveShift(countedCash: Double, notes: String) {
        val shift = activeShift.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.closeShift(shift.id, countedCash, notes)
            SoundHelper.playSuccessBeep()
        }
    }

    // --- Manager Dashboard & Security ---
    private val _isManagerUnlocked = MutableStateFlow(false)
    val isManagerUnlocked: StateFlow<Boolean> = _isManagerUnlocked.asStateFlow()

    private val _managerPin = MutableStateFlow("1234") // Default PIN
    val managerPin: StateFlow<String> = _managerPin.asStateFlow()

    fun verifyPin(pin: String): Boolean {
        val match = (pin == _managerPin.value)
        if (match) {
            _isManagerUnlocked.value = true
        }
        return match
    }

    fun lockManager() {
        _isManagerUnlocked.value = false
    }

    fun updateManagerPin(newPin: String) {
        if (newPin.length >= 4) {
            _managerPin.value = newPin
        }
    }

    // Dashboard Time Filter: "TODAY", "WEEK", "ALL"
    private val _dashboardPeriod = MutableStateFlow("TODAY")
    val dashboardPeriod: StateFlow<String> = _dashboardPeriod.asStateFlow()

    fun setDashboardPeriod(period: String) {
        _dashboardPeriod.value = period
    }

    // Backup & Restore Database
    fun exportBackupJson(): String {
        val products = allProducts.value
        val jsonArray = JSONArray()
        for (p in products) {
            val obj = JSONObject().apply {
                put("name", p.name)
                put("barcode", p.barcode)
                put("category", p.category)
                put("sellPrice", p.sellPrice)
                put("costPrice", p.costPrice)
                put("stockQuantity", p.stockQuantity)
                put("minStockAlert", p.minStockAlert)
                put("description", p.description)
            }
            jsonArray.put(obj)
        }
        return jsonArray.toString(2)
    }

    fun resetCatalogToDefaults() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetDatabaseToDefaults()
        }
    }
}
