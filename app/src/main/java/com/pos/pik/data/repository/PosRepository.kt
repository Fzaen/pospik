package com.pos.pik.data.repository

import com.pos.pik.data.local.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*

class PosRepository(private val db: AppDatabase) {

    // Auth
    suspend fun login(username: String, password: String): UserWithRole? {
        return db.userDao().login(username, password)
    }

    suspend fun verifyAdminPassword(password: String): Boolean {
        return db.userDao().verifyAdminPassword(password) > 0
    }

    // Roles & Users
    fun getAllRoles(): List<RoleEntity> {
        // Will be called in coroutine
        return emptyList()
    }

    suspend fun fetchRoles(): List<RoleEntity> = db.roleDao().getAllRoles()

    fun getAllUsers(): Flow<List<UserWithRole>> = db.userDao().getAllUsers()

    suspend fun addUser(user: UserEntity): Long = db.userDao().insertUser(user)

    suspend fun updateUser(user: UserEntity) = db.userDao().updateUser(user)

    // Categories
    fun getAllCategories(): Flow<List<CategoryEntity>> = db.categoryDao().getAllCategories()

    suspend fun getMainCategories(): List<String> = db.categoryDao().getMainCategories()

    suspend fun addCategory(name: String, subname: String): Long {
        return db.categoryDao().insertCategory(CategoryEntity(catName = name, catSubname = subname))
    }

    suspend fun updateCategory(id: Int, name: String, subname: String) {
        db.categoryDao().updateCategory(CategoryEntity(catId = id, catName = name, catSubname = subname))
    }

    suspend fun deleteCategory(id: Int): Boolean {
        val count = db.categoryDao().getProductCountByCategory(id)
        if (count > 0) return false
        db.categoryDao().deleteCategory(id)
        return true
    }

    // Products
    fun getAllProducts(mainCat: String? = null, query: String? = null): Flow<List<ProductWithCategory>> {
        return db.productDao().getAllProducts(mainCat, query)
    }

    fun getActiveProducts(mainCat: String? = null, query: String? = null): Flow<List<ProductWithCategory>> {
        return db.productDao().getActiveProducts(mainCat, query)
    }

    suspend fun generateNextSku(catId: Int): String {
        val prefix = if (catId > 0 && catId <= 999) catId.toString().padStart(3, '0') else "001"
        val lastSku = db.productDao().getLastSkuWithPrefix(prefix)
        return if (lastSku != null && lastSku.startsWith(prefix) && lastSku.length == 7) {
            val seq = lastSku.substring(3).toIntOrNull() ?: 0
            prefix + (seq + 1).toString().padStart(4, '0')
        } else {
            "${prefix}0001"
        }
    }

    suspend fun addProduct(product: ProductEntity, initialStock: Int = 0) {
        db.productDao().insertProduct(product)
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        db.masterStockDao().insertStock(
            MasterStockEntity(
                stPrdSku = product.prdSku,
                stYear = currentYear,
                stInitialQty = initialStock,
                stFinalQty = initialStock
            )
        )
    }

    suspend fun updateProduct(product: ProductEntity) = db.productDao().updateProduct(product)

    suspend fun deleteProduct(sku: String) {
        db.productDao().deleteProductBySku(sku)
    }

    suspend fun deleteAllProducts() {
        db.productDao().deleteAllProducts()
    }

    // Cart
    fun getActiveCart(userId: Int): Flow<List<CartItemWithProduct>> = db.posCartDao().getActiveCart(userId)

    suspend fun addToCart(userId: Int, sku: String, qty: Int, price: Double, costPrice: Double) {
        val existing = db.posCartDao().getCartItem(userId, sku)
        if (existing != null) {
            val newQty = existing.cartQty + qty
            updateCartQty(userId, existing.cartId, newQty)
        } else {
            val cart = PosCartEntity(
                cartUserId = userId,
                cartPrdSku = sku,
                cartQty = qty,
                cartPrice = price,
                cartCostPrice = costPrice,
                cartSubtotal = qty * price,
                cartStatus = 0
            )
            db.posCartDao().insertCart(cart)
        }
    }

    suspend fun updateCartQty(userId: Int, cartId: Int, newQty: Int) {
        val existing = db.posCartDao().getCartItemById(cartId) ?: return
        if (newQty <= 0) return
        val oldQty = existing.cartQty
        if (newQty < oldQty) {
            db.posLogDao().insertLog(
                PosLogEntity(
                    logUserId = userId,
                    logPrdSku = existing.cartPrdSku,
                    logAction = "REDUCE",
                    logOldQty = oldQty,
                    logNewQty = newQty,
                    logDescription = "Pengurangan kuantitas di keranjang"
                )
            )
        }
        val updated = existing.copy(
            cartQty = newQty,
            cartSubtotal = newQty * existing.cartPrice
        )
        db.posCartDao().updateCart(updated)
    }

    suspend fun removeFromCart(userId: Int, cartId: Int) {
        val existing = db.posCartDao().getCartItemById(cartId) ?: return
        db.posLogDao().insertLog(
            PosLogEntity(
                logUserId = userId,
                logPrdSku = existing.cartPrdSku,
                logAction = "DELETE",
                logOldQty = existing.cartQty,
                logNewQty = 0,
                logDescription = "Penghapusan item dari keranjang"
            )
        )
        db.posCartDao().deleteCartItem(cartId)
    }

    // Payment Processing
    suspend fun processPayment(
        userId: Int,
        subtotal: Double,
        paidAmount: Double,
        changeAmount: Double,
        cartItems: List<CartItemWithProduct>,
        paymentMethod: String = "Tunai"
    ): String {
        val datePart = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastSale = db.saleDao().getLastSale()
        var sequence = 1
        if (lastSale != null && lastSale.slsInvoiceNumber.contains(datePart)) {
            val parts = lastSale.slsInvoiceNumber.split("-")
            if (parts.isNotEmpty()) {
                val lastSeq = parts.last().toIntOrNull()
                if (lastSeq != null) sequence = lastSeq + 1
            }
        }
        val invoiceNumber = "INV-$datePart-${sequence.toString().padStart(4, '0')}"

        val sale = SaleEntity(
            slsInvoiceNumber = invoiceNumber,
            slsUserId = userId,
            slsSubtotal = subtotal,
            slsDiscountAmount = 0.0,
            slsGrandTotal = subtotal,
            slsPaidAmount = paidAmount,
            slsChangeAmount = changeAmount,
            slsPaymentMethod = paymentMethod,
            slsTotalItem = cartItems.size
        )
        db.saleDao().insertSale(sale)

        val items = cartItems.map {
            SaleItemEntity(
                itmSaleId = invoiceNumber,
                itmSku = it.cartPrdSku,
                itmDiscount = 0.0,
                itmCashback = 0.0,
                itmQuantity = it.cartQty,
                itmUnitPrice = it.cartPrice,
                itmCostPrice = it.cartCostPrice,
                itmSubtotal = it.cartSubtotal
            )
        }
        db.saleDao().insertSaleItems(items)

        // Checkout Cart Status
        db.posCartDao().checkoutCart(userId)

        return invoiceNumber
    }

    // Invoice Query
    suspend fun getSaleByInvoice(invoiceNumber: String): SaleWithUser? = db.saleDao().getSaleByInvoice(invoiceNumber)

    suspend fun getSaleItemsByInvoice(invoiceNumber: String): List<SaleItemWithProduct> = db.saleDao().getSaleItemsByInvoice(invoiceNumber)

    // Reports
    fun getSalesHistory(startDate: String, endDate: String): Flow<List<SaleWithUser>> = db.saleDao().getSalesHistory(startDate, endDate)

    fun getProfitReport(startDate: String, endDate: String): Flow<List<ProfitReportRow>> = db.saleDao().getProfitReport(startDate, endDate)

    fun getSalesByItemReport(startDate: String, endDate: String): Flow<List<ItemSalesReportRow>> = db.saleDao().getSalesByItemReport(startDate, endDate)

    fun getSalesByCategoryReport(startDate: String, endDate: String): Flow<List<CategorySalesReportRow>> = db.saleDao().getSalesByCategoryReport(startDate, endDate)

    fun getSalesBySubCategoryReport(startDate: String, endDate: String): Flow<List<SubCategorySalesReportRow>> = db.saleDao().getSalesBySubCategoryReport(startDate, endDate)

    fun getAuditLogs(startDate: String, endDate: String): Flow<List<PosLogWithDetails>> = db.posLogDao().getLogs(startDate, endDate)

    fun getTodayStatsFlow(): Flow<TodayStats> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return combine(db.saleDao().getTodayCountFlow(today), db.saleDao().getTodayOmzetFlow(today)) { count, omzet ->
            TodayStats(count, omzet)
        }
    }

    suspend fun getTodayStats(): TodayStats {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val count = db.saleDao().getTodayCount(today)
        val omzet = db.saleDao().getTodayOmzet(today)
        return TodayStats(count, omzet)
    }

    // Settings
    fun getSettings(): Flow<AppSettingEntity?> = db.appSettingDao().getSettings()

    suspend fun getSettingsSync(): AppSettingEntity {
        return db.appSettingDao().getSettingsSync() ?: AppSettingEntity()
    }

    suspend fun updateSettings(setting: AppSettingEntity) = db.appSettingDao().updateSettings(setting)

    // --- INVENTORY / GUDANG METHODS ---

    fun getMasterStock(year: Int, query: String? = null): Flow<List<MasterStockWithProduct>> {
        return db.masterStockDao().getMasterStockByYear(year, query)
    }

    suspend fun recalculateStockForYear(year: Int) {
        val allProducts = db.productDao().getLastSkuWithPrefix("").let {
            // Fetch all products
            db.productDao().getActiveProducts(null, null)
        }
        // Query products directly
        val productsList = db.productDao().getLastSkuWithPrefix("") // helper
        val activeProducts = db.productDao().getActiveProducts(null, null)
    }

    suspend fun calculateStockForSkuAndYear(sku: String, year: Int) {
        var existing = db.masterStockDao().getStockBySkuAndYear(sku, year)
        val initialQty = existing?.stInitialQty ?: 0
        val incomingQty = db.inventoryIncomingDao().getTotalIncomingQty(sku, year)
        val salesQty = db.saleDao().getTotalSalesQtyForSku(sku, year)
        val damagedQty = db.inventoryDamagedDao().getTotalDamagedQty(sku, year)
        val internalUseQty = db.inventoryInternalUseDao().getTotalInternalUseQty(sku, year)

        val finalQty = initialQty + incomingQty - salesQty - damagedQty - internalUseQty

        val dateNow = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val updatedStock = MasterStockEntity(
            stId = existing?.stId ?: 0,
            stPrdSku = sku,
            stYear = year,
            stInitialQty = initialQty,
            stIncomingQty = incomingQty,
            stSalesQty = salesQty,
            stDamagedQty = damagedQty,
            stInternalUseQty = internalUseQty,
            stFinalQty = finalQty,
            stLastUpdated = dateNow
        )
        db.masterStockDao().insertStock(updatedStock)
    }

    suspend fun recalculateAllStock(year: Int) {
        val currentStockList = db.masterStockDao().getAllStockByYear(year)
        val skus = currentStockList.map { it.stPrdSku }.toMutableSet()
        
        // Also add products that don't have stock row yet
        // Recalculate for each sku
        for (sku in skus) {
            calculateStockForSkuAndYear(sku, year)
        }
    }

    suspend fun ensureStockEntryExists(sku: String, year: Int) {
        val existing = db.masterStockDao().getStockBySkuAndYear(sku, year)
        if (existing == null) {
            db.masterStockDao().insertStock(
                MasterStockEntity(
                    stPrdSku = sku,
                    stYear = year,
                    stInitialQty = 0,
                    stFinalQty = 0
                )
            )
        }
    }

    suspend fun recordIncomingInventory(
        userId: Int,
        sku: String,
        packageQty: Int,
        fraction: Int,
        totalCost: Double,
        note: String?
    ) {
        val totalQty = packageQty * fraction
        val unitCost = if (totalQty > 0) totalCost / totalQty else 0.0

        val incoming = InventoryIncomingEntity(
            incUserId = userId,
            incPrdSku = sku,
            incPackageQty = packageQty,
            incFraction = fraction,
            incTotalQty = totalQty,
            incTotalCost = totalCost,
            incUnitCost = unitCost,
            incNote = note
        )
        db.inventoryIncomingDao().insertIncoming(incoming)

        // Update product cost price (HPP) in product master
        if (unitCost > 0) {
            db.productDao().updateProductCostPrice(sku, unitCost)
        }

        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        ensureStockEntryExists(sku, currentYear)
        calculateStockForSkuAndYear(sku, currentYear)
    }

    suspend fun recordDamagedInventory(userId: Int, sku: String, qty: Int, reason: String?) {
        val damaged = InventoryDamagedEntity(
            dmgUserId = userId,
            dmgPrdSku = sku,
            dmgQty = qty,
            dmgReason = reason
        )
        db.inventoryDamagedDao().insertDamaged(damaged)

        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        ensureStockEntryExists(sku, currentYear)
        calculateStockForSkuAndYear(sku, currentYear)
    }

    suspend fun recordInternalUseInventory(userId: Int, sku: String, qty: Int, note: String?) {
        val internalUse = InventoryInternalUseEntity(
            useUserId = userId,
            usePrdSku = sku,
            useQty = qty,
            useNote = note
        )
        db.inventoryInternalUseDao().insertInternalUse(internalUse)

        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        ensureStockEntryExists(sku, currentYear)
        calculateStockForSkuAndYear(sku, currentYear)
    }

    suspend fun performTutupBuku(fromYear: Int, toYear: Int) {
        recalculateAllStock(fromYear)
        val fromStocks = db.masterStockDao().getAllStockByYear(fromYear)

        db.masterStockDao().deleteStockByYear(toYear)

        val dateNow = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        for (st in fromStocks) {
            val newStock = MasterStockEntity(
                stPrdSku = st.stPrdSku,
                stYear = toYear,
                stInitialQty = st.stFinalQty,
                stIncomingQty = 0,
                stSalesQty = 0,
                stDamagedQty = 0,
                stInternalUseQty = 0,
                stFinalQty = st.stFinalQty,
                stLastUpdated = dateNow
            )
            db.masterStockDao().insertStock(newStock)
        }
    }

    fun getIncomingHistory(startDate: String, endDate: String): Flow<List<InventoryIncomingWithDetails>> =
        db.inventoryIncomingDao().getIncomingHistory(startDate, endDate)

    fun getDamagedHistory(startDate: String, endDate: String): Flow<List<InventoryDamagedWithDetails>> =
        db.inventoryDamagedDao().getDamagedHistory(startDate, endDate)

    fun getInternalUseHistory(startDate: String, endDate: String): Flow<List<InventoryInternalUseWithDetails>> =
        db.inventoryInternalUseDao().getInternalUseHistory(startDate, endDate)
}
