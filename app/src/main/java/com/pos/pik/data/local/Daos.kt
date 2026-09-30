package com.pos.pik.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {
    @Query("SELECT * FROM roles")
    suspend fun getAllRoles(): List<RoleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(roles: List<RoleEntity>)
}

@Dao
interface UserDao {
    @Query("SELECT u.*, r.rol_name FROM users u JOIN roles r ON u.usr_role_id = r.rol_id ORDER BY u.usr_name ASC")
    fun getAllUsers(): Flow<List<UserWithRole>>

    @Query("SELECT u.*, r.rol_name FROM users u JOIN roles r ON u.usr_role_id = r.rol_id WHERE u.usr_username = :username AND u.usr_password = :password AND u.usr_is_active = 1 LIMIT 1")
    suspend fun login(username: String, password: String): UserWithRole?

    @Query("""
        SELECT COUNT(*) FROM users u 
        JOIN roles r ON u.usr_role_id = r.rol_id 
        WHERE LOWER(r.rol_name) = 'admin' AND u.usr_password = :password AND u.usr_is_active = 1
    """)
    suspend fun verifyAdminPassword(password: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY cat_name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT DISTINCT cat_name FROM categories")
    suspend fun getMainCategories(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE cat_id = :catId")
    suspend fun deleteCategory(catId: Int)

    @Query("SELECT COUNT(*) FROM products WHERE prd_category_id = :catId")
    suspend fun getProductCountByCategory(catId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)
}

@Dao
interface ProductDao {
    @Query("""
        SELECT p.*, c.cat_name, c.cat_subname 
        FROM products p
        JOIN categories c ON p.prd_category_id = c.cat_id
        WHERE (:mainCat IS NULL OR c.cat_name = :mainCat)
          AND (:query IS NULL OR :query = '' OR p.prd_name LIKE '%' || :query || '%' OR p.prd_sku LIKE '%' || :query || '%')
        ORDER BY p.prd_sku ASC
    """)
    fun getAllProducts(mainCat: String?, query: String?): Flow<List<ProductWithCategory>>

    @Query("""
        SELECT p.*, c.cat_name, c.cat_subname 
        FROM products p
        JOIN categories c ON p.prd_category_id = c.cat_id
        WHERE p.prd_is_active = 1
          AND (:mainCat IS NULL OR c.cat_name = :mainCat)
          AND (:query IS NULL OR :query = '' OR p.prd_name LIKE '%' || :query || '%' OR p.prd_sku LIKE '%' || :query || '%')
        ORDER BY p.prd_sku ASC
    """)
    fun getActiveProducts(mainCat: String?, query: String?): Flow<List<ProductWithCategory>>

    @Query("SELECT MAX(prd_sku) FROM products WHERE prd_sku LIKE :prefix || '%' AND length(prd_sku) = 7")
    suspend fun getLastSkuWithPrefix(prefix: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET prd_cost_price = :costPrice WHERE prd_sku = :sku")
    suspend fun updateProductCostPrice(sku: String, costPrice: Double)

    @Query("DELETE FROM products WHERE prd_sku = :sku")
    suspend fun deleteProductBySku(sku: String)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)
}

@Dao
interface PosCartDao {
    @Query("""
        SELECT c.*, p.prd_name, p.prd_image 
        FROM pos_cart c
        JOIN products p ON c.cart_prd_sku = p.prd_sku
        WHERE c.cart_user_id = :userId AND c.cart_status = 0
    """)
    fun getActiveCart(userId: Int): Flow<List<CartItemWithProduct>>

    @Query("SELECT * FROM pos_cart WHERE cart_user_id = :userId AND cart_prd_sku = :sku AND cart_status = 0 LIMIT 1")
    suspend fun getCartItem(userId: Int, sku: String): PosCartEntity?

    @Query("SELECT * FROM pos_cart WHERE cart_id = :cartId LIMIT 1")
    suspend fun getCartItemById(cartId: Int): PosCartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: PosCartEntity)

    @Update
    suspend fun updateCart(cart: PosCartEntity)

    @Query("DELETE FROM pos_cart WHERE cart_id = :cartId")
    suspend fun deleteCartItem(cartId: Int)

    @Query("UPDATE pos_cart SET cart_status = 1 WHERE cart_user_id = :userId AND cart_status = 0")
    suspend fun checkoutCart(userId: Int)
}

@Dao
interface SaleDao {
    @Query("SELECT * FROM sales ORDER BY sls_transaction_date DESC LIMIT 1")
    suspend fun getLastSale(): SaleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItems(items: List<SaleItemEntity>)

    @Query("""
        SELECT s.*, u.usr_username 
        FROM sales s
        JOIN users u ON s.sls_user_id = u.usr_id
        WHERE DATE(s.sls_transaction_date) BETWEEN DATE(:startDate) AND DATE(:endDate)
        ORDER BY s.sls_transaction_date DESC
    """)
    fun getSalesHistory(startDate: String, endDate: String): Flow<List<SaleWithUser>>

    @Query("""
        SELECT s.*, u.usr_username 
        FROM sales s
        JOIN users u ON s.sls_user_id = u.usr_id
        WHERE s.sls_invoice_number = :invoiceNumber
        LIMIT 1
    """)
    suspend fun getSaleByInvoice(invoiceNumber: String): SaleWithUser?

    @Query("""
        SELECT si.*, p.prd_name 
        FROM sale_items si
        JOIN products p ON si.itm_sku = p.prd_sku
        WHERE si.itm_sale_id = :invoiceNumber
    """)
    suspend fun getSaleItemsByInvoice(invoiceNumber: String): List<SaleItemWithProduct>

    @Query("""
        SELECT DATE(sls_transaction_date) as date,
        COUNT(*) as total_invoices,
        SUM(sls_grand_total) as total_revenue,
        SUM((SELECT SUM(itm_quantity * itm_cost_price) FROM sale_items WHERE itm_sale_id = sls_invoice_number)) as total_cost,
        COALESCE(SUM((SELECT SUM(itm_quantity) FROM sale_items WHERE itm_sale_id = sls_invoice_number)), 0) as total_qty
        FROM sales 
        WHERE DATE(sls_transaction_date) BETWEEN DATE(:startDate) AND DATE(:endDate)
        GROUP BY DATE(sls_transaction_date) 
        ORDER BY date DESC
    """)
    fun getProfitReport(startDate: String, endDate: String): Flow<List<ProfitReportRow>>

    @Query("""
        SELECT 
          si.itm_sku, 
          p.prd_name,
          c.cat_name,
          c.cat_subname,
          p.prd_cost_price as cost_price,
          p.prd_selling_price as selling_price,
          SUM(si.itm_quantity) as total_qty,
          SUM(si.itm_quantity * si.itm_cost_price) as total_cost,
          SUM(si.itm_subtotal) as total_sales,
          SUM(si.itm_subtotal - (si.itm_quantity * si.itm_cost_price)) as total_profit,
          COUNT(DISTINCT si.itm_sale_id) as total_trx
        FROM sale_items si
        JOIN products p ON si.itm_sku = p.prd_sku
        JOIN categories c ON p.prd_category_id = c.cat_id
        JOIN sales s ON si.itm_sale_id = s.sls_invoice_number
        WHERE DATE(s.sls_transaction_date) BETWEEN DATE(:startDate) AND DATE(:endDate)
        GROUP BY si.itm_sku, p.prd_name, c.cat_name, c.cat_subname, p.prd_cost_price, p.prd_selling_price
        ORDER BY total_qty DESC
    """)
    fun getSalesByItemReport(startDate: String, endDate: String): Flow<List<ItemSalesReportRow>>

    @Query("""
        SELECT 
          c.cat_name,
          SUM(si.itm_quantity) as total_qty,
          SUM(si.itm_quantity * si.itm_cost_price) as total_cost,
          SUM(si.itm_subtotal) as total_sales,
          SUM(si.itm_subtotal - (si.itm_quantity * si.itm_cost_price)) as total_profit,
          COUNT(DISTINCT si.itm_sale_id) as total_trx
        FROM sale_items si
        JOIN products p ON si.itm_sku = p.prd_sku
        JOIN categories c ON p.prd_category_id = c.cat_id
        JOIN sales s ON si.itm_sale_id = s.sls_invoice_number
        WHERE DATE(s.sls_transaction_date) BETWEEN DATE(:startDate) AND DATE(:endDate)
        GROUP BY c.cat_name
        ORDER BY total_sales DESC
    """)
    fun getSalesByCategoryReport(startDate: String, endDate: String): Flow<List<CategorySalesReportRow>>

    @Query("""
        SELECT 
          c.cat_name,
          c.cat_subname,
          SUM(si.itm_quantity) as total_qty,
          SUM(si.itm_quantity * si.itm_cost_price) as total_cost,
          SUM(si.itm_subtotal) as total_sales,
          SUM(si.itm_subtotal - (si.itm_quantity * si.itm_cost_price)) as total_profit,
          COUNT(DISTINCT si.itm_sale_id) as total_trx
        FROM sale_items si
        JOIN products p ON si.itm_sku = p.prd_sku
        JOIN categories c ON p.prd_category_id = c.cat_id
        JOIN sales s ON si.itm_sale_id = s.sls_invoice_number
        WHERE DATE(s.sls_transaction_date) BETWEEN DATE(:startDate) AND DATE(:endDate)
        GROUP BY c.cat_name, c.cat_subname
        ORDER BY total_sales DESC
    """)
    fun getSalesBySubCategoryReport(startDate: String, endDate: String): Flow<List<SubCategorySalesReportRow>>

    @Query("SELECT COUNT(*) FROM sales WHERE DATE(sls_transaction_date) = DATE(:today)")
    suspend fun getTodayCount(today: String): Int

    @Query("SELECT COUNT(*) FROM sales WHERE DATE(sls_transaction_date) = DATE(:today)")
    fun getTodayCountFlow(today: String): Flow<Int>

    @Query("SELECT COALESCE(SUM(sls_grand_total), 0.0) FROM sales WHERE DATE(sls_transaction_date) = DATE(:today)")
    suspend fun getTodayOmzet(today: String): Double

    @Query("SELECT COALESCE(SUM(sls_grand_total), 0.0) FROM sales WHERE DATE(sls_transaction_date) = DATE(:today)")
    fun getTodayOmzetFlow(today: String): Flow<Double>

    @Query("""
        SELECT COALESCE(SUM(si.itm_quantity), 0)
        FROM sale_items si
        JOIN sales s ON si.itm_sale_id = s.sls_invoice_number
        WHERE si.itm_sku = :sku AND strftime('%Y', s.sls_transaction_date) = CAST(:year AS TEXT)
    """)
    suspend fun getTotalSalesQtyForSku(sku: String, year: Int): Int
}

@Dao
interface PosLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: PosLogEntity)

    @Query("""
        SELECT l.*, u.usr_name, p.prd_name 
        FROM pos_logs l
        JOIN users u ON l.log_user_id = u.usr_id
        JOIN products p ON l.log_prd_sku = p.prd_sku
        WHERE DATE(l.log_timestamp) BETWEEN DATE(:startDate) AND DATE(:endDate)
        ORDER BY l.log_timestamp DESC
    """)
    fun getLogs(startDate: String, endDate: String): Flow<List<PosLogWithDetails>>
}

@Dao
interface AppSettingDao {
    @Query("SELECT * FROM app_settings WHERE set_id = 1 LIMIT 1")
    fun getSettings(): Flow<AppSettingEntity?>

    @Query("SELECT * FROM app_settings WHERE set_id = 1 LIMIT 1")
    suspend fun getSettingsSync(): AppSettingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(setting: AppSettingEntity)
}

// --- INVENTORY DAOS ---

@Dao
interface MasterStockDao {
    @Query("""
        SELECT s.*, p.prd_name, p.prd_cost_price, p.prd_selling_price, c.cat_name
        FROM master_stock s
        JOIN products p ON s.st_prd_sku = p.prd_sku
        JOIN categories c ON p.prd_category_id = c.cat_id
        WHERE s.st_year = :year
          AND (:query IS NULL OR :query = '' OR p.prd_name LIKE '%' || :query || '%' OR p.prd_sku LIKE '%' || :query || '%')
        ORDER BY p.prd_name ASC
    """)
    fun getMasterStockByYear(year: Int, query: String? = null): Flow<List<MasterStockWithProduct>>

    @Query("SELECT * FROM master_stock WHERE st_prd_sku = :sku AND st_year = :year LIMIT 1")
    suspend fun getStockBySkuAndYear(sku: String, year: Int): MasterStockEntity?

    @Query("SELECT * FROM master_stock WHERE st_year = :year")
    suspend fun getAllStockByYear(year: Int): List<MasterStockEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: MasterStockEntity)

    @Update
    suspend fun updateStock(stock: MasterStockEntity)

    @Query("DELETE FROM master_stock WHERE st_year = :year")
    suspend fun deleteStockByYear(year: Int)
}

@Dao
interface InventoryIncomingDao {
    @Query("""
        SELECT i.*, p.prd_name, u.usr_name, c.cat_name, c.cat_subname
        FROM inventory_incoming i
        JOIN products p ON i.inc_prd_sku = p.prd_sku
        JOIN categories c ON p.prd_category_id = c.cat_id
        JOIN users u ON i.inc_user_id = u.usr_id
        WHERE DATE(i.inc_date) BETWEEN DATE(:startDate) AND DATE(:endDate)
        ORDER BY i.inc_date DESC
    """)
    fun getIncomingHistory(startDate: String, endDate: String): Flow<List<InventoryIncomingWithDetails>>

    @Query("""
        SELECT COALESCE(SUM(inc_total_qty), 0)
        FROM inventory_incoming
        WHERE inc_prd_sku = :sku AND strftime('%Y', inc_date) = CAST(:year AS TEXT)
    """)
    suspend fun getTotalIncomingQty(sku: String, year: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncoming(incoming: InventoryIncomingEntity): Long
}

@Dao
interface InventoryDamagedDao {
    @Query("""
        SELECT d.*, p.prd_name, u.usr_name, c.cat_name, c.cat_subname
        FROM inventory_damaged d
        JOIN products p ON d.dmg_prd_sku = p.prd_sku
        JOIN categories c ON p.prd_category_id = c.cat_id
        JOIN users u ON d.dmg_user_id = u.usr_id
        WHERE DATE(d.dmg_date) BETWEEN DATE(:startDate) AND DATE(:endDate)
        ORDER BY d.dmg_date DESC
    """)
    fun getDamagedHistory(startDate: String, endDate: String): Flow<List<InventoryDamagedWithDetails>>

    @Query("""
        SELECT COALESCE(SUM(dmg_qty), 0)
        FROM inventory_damaged
        WHERE dmg_prd_sku = :sku AND strftime('%Y', dmg_date) = CAST(:year AS TEXT)
    """)
    suspend fun getTotalDamagedQty(sku: String, year: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDamaged(damaged: InventoryDamagedEntity): Long
}

@Dao
interface InventoryInternalUseDao {
    @Query("""
        SELECT u.*, p.prd_name, us.usr_name, c.cat_name, c.cat_subname
        FROM inventory_internal_use u
        JOIN products p ON u.use_prd_sku = p.prd_sku
        JOIN categories c ON p.prd_category_id = c.cat_id
        JOIN users us ON u.use_user_id = us.usr_id
        WHERE DATE(u.use_date) BETWEEN DATE(:startDate) AND DATE(:endDate)
        ORDER BY u.use_date DESC
    """)
    fun getInternalUseHistory(startDate: String, endDate: String): Flow<List<InventoryInternalUseWithDetails>>

    @Query("""
        SELECT COALESCE(SUM(use_qty), 0)
        FROM inventory_internal_use
        WHERE use_prd_sku = :sku AND strftime('%Y', use_date) = CAST(:year AS TEXT)
    """)
    suspend fun getTotalInternalUseQty(sku: String, year: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInternalUse(internalUse: InventoryInternalUseEntity): Long
}
