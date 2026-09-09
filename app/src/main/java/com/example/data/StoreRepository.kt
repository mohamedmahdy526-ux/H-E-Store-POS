package com.example.data

import kotlinx.coroutines.flow.Flow

class StoreRepository(private val dao: AppDao) {

    val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()
    val lowStockProducts: Flow<List<ProductEntity>> = dao.getLowStockProducts()
    val allOrders: Flow<List<OrderEntity>> = dao.getAllOrders()
    val activeShift: Flow<ShiftEntity?> = dao.getActiveShift()
    val allShifts: Flow<List<ShiftEntity>> = dao.getAllShifts()
    val allExpenses: Flow<List<ExpenseEntity>> = dao.getAllExpenses()

    suspend fun getProductById(id: Long): ProductEntity? = dao.getProductById(id)

    suspend fun getProductByBarcode(barcode: String): ProductEntity? = dao.getProductByBarcode(barcode)

    suspend fun insertProduct(product: ProductEntity): Long = dao.insertProduct(product)

    suspend fun updateProduct(product: ProductEntity) = dao.updateProduct(product)

    suspend fun deleteProduct(product: ProductEntity) = dao.deleteProduct(product)

    suspend fun receiveStock(productId: Long, quantityToAdd: Int) {
        dao.incrementStock(productId, quantityToAdd)
    }

    suspend fun reconcileStock(productId: Long, actualCountedStock: Int) {
        dao.updateStock(productId, actualCountedStock)
    }

    // Orders & Checkout
    suspend fun completeSale(
        order: OrderEntity,
        itemsToDeduct: List<Pair<Long, Int>>
    ): Long {
        val orderId = dao.insertOrder(order)
        // Deduct stock for each sold item
        for ((productId, quantity) in itemsToDeduct) {
            dao.incrementStock(productId, -quantity)
        }
        // Update active shift totals if this order belongs to an active shift
        val active = dao.getActiveShiftSync()
        if (active != null && active.id == order.shiftId) {
            if (order.paymentMethod == "CASH") {
                val updatedShift = active.copy(
                    totalCashSales = active.totalCashSales + order.netAmount
                )
                dao.updateShift(updatedShift)
            } else {
                val updatedShift = active.copy(
                    totalElectronicSales = active.totalElectronicSales + order.netAmount
                )
                dao.updateShift(updatedShift)
            }
        }
        return orderId
    }

    // Shifts
    suspend fun openShift(cashierName: String, openingBalance: Double, notes: String): Long {
        // Ensure any previous shift is closed or archive
        val active = dao.getActiveShiftSync()
        if (active != null) {
            val autoClosed = active.copy(
                isOpen = false,
                endTime = System.currentTimeMillis(),
                notes = "${active.notes} (تم إغلاقها تلقائياً لفتح وردية جديدة)"
            )
            dao.updateShift(autoClosed)
        }
        val newShift = ShiftEntity(
            cashierName = cashierName.ifBlank { "كاشير" },
            startTime = System.currentTimeMillis(),
            isOpen = true,
            openingBalance = openingBalance,
            notes = notes
        )
        return dao.insertShift(newShift)
    }

    suspend fun addExpenseOrWithdrawal(
        shiftId: Long,
        type: String,
        title: String,
        amount: Double,
        notes: String
    ): Long {
        val expense = ExpenseEntity(
            shiftId = shiftId,
            type = type,
            title = title,
            amount = amount,
            timestamp = System.currentTimeMillis(),
            notes = notes
        )
        val id = dao.insertExpense(expense)
        // Update shift expense totals
        val active = dao.getActiveShiftSync()
        if (active != null && active.id == shiftId) {
            val updated = if (type == "EXPENSE") {
                active.copy(totalExpenses = active.totalExpenses + amount)
            } else {
                active.copy(totalWithdrawals = active.totalWithdrawals + amount)
            }
            dao.updateShift(updated)
        }
        return id
    }

    suspend fun closeShift(shiftId: Long, countedCash: Double, notes: String): ShiftEntity? {
        val shift = dao.getShiftById(shiftId) ?: return null
        val expected = shift.openingBalance + shift.totalCashSales - shift.totalExpenses - shift.totalWithdrawals
        val diff = countedCash - expected

        val closed = shift.copy(
            isOpen = false,
            endTime = System.currentTimeMillis(),
            closingCountedCash = countedCash,
            expectedCash = expected,
            differenceCash = diff,
            notes = if (notes.isNotBlank()) "${shift.notes} | $notes" else shift.notes
        )
        dao.updateShift(closed)
        return closed
    }

    suspend fun resetDatabaseToDefaults() {
        dao.deleteAllProducts()
        dao.insertAllProducts(SeedData.initialProducts)
    }

    suspend fun getProductCount(): Int = dao.getProductCount()

    suspend fun insertAllProducts(list: List<ProductEntity>) = dao.insertAllProducts(list)
}
