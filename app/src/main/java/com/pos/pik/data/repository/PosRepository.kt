package com.pos.pik.data.repository

import com.pos.pik.data.local.*
import kotlinx.coroutines.flow.Flow
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
        val prefix = if (catId > 0) catId.toString() else "1"
        val lastSku = db.productDao().getLastSkuWithPrefix(prefix)
        return if (lastSku != null) {
            val lastNum = lastSku.toLongOrNull() ?: (prefix + "0000").toLong()
            (lastNum + 1).toString()
        } else {
            "${prefix}0001"
        }
    }

    suspend fun addProduct(product: ProductEntity) = db.productDao().insertProduct(product)

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
}
