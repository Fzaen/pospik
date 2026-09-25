package com.pos.pik.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "roles")
data class RoleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rol_id") val rolId: Int = 0,
    @ColumnInfo(name = "rol_name") val rolName: String
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "usr_id") val usrId: Int = 0,
    @ColumnInfo(name = "usr_role_id") val usrRoleId: Int,
    @ColumnInfo(name = "usr_name") val usrName: String,
    @ColumnInfo(name = "usr_username") val usrUsername: String,
    @ColumnInfo(name = "usr_password") val usrPassword: String,
    @ColumnInfo(name = "usr_phone") val usrPhone: String? = null,
    @ColumnInfo(name = "usr_is_active") val usrIsActive: Int = 1
)

data class UserWithRole(
    @ColumnInfo(name = "usr_id") val usrId: Int,
    @ColumnInfo(name = "usr_role_id") val usrRoleId: Int,
    @ColumnInfo(name = "usr_name") val usrName: String,
    @ColumnInfo(name = "usr_username") val usrUsername: String,
    @ColumnInfo(name = "usr_password") val usrPassword: String,
    @ColumnInfo(name = "usr_phone") val usrPhone: String?,
    @ColumnInfo(name = "usr_is_active") val usrIsActive: Int,
    @ColumnInfo(name = "rol_name") val rolName: String
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cat_id") val catId: Int = 0,
    @ColumnInfo(name = "cat_name") val catName: String,
    @ColumnInfo(name = "cat_subname") val catSubname: String
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "prd_sku") val prdSku: String,
    @ColumnInfo(name = "prd_category_id") val prdCategoryId: Int,
    @ColumnInfo(name = "prd_name") val prdName: String,
    @ColumnInfo(name = "prd_cost_price") val prdCostPrice: Double,
    @ColumnInfo(name = "prd_selling_price") val prdSellingPrice: Double,
    @ColumnInfo(name = "prd_image") val prdImage: String? = null,
    @ColumnInfo(name = "prd_is_active") val prdIsActive: Int = 1
)

data class ProductWithCategory(
    @ColumnInfo(name = "prd_sku") val prdSku: String,
    @ColumnInfo(name = "prd_category_id") val prdCategoryId: Int,
    @ColumnInfo(name = "prd_name") val prdName: String,
    @ColumnInfo(name = "prd_cost_price") val prdCostPrice: Double,
    @ColumnInfo(name = "prd_selling_price") val prdSellingPrice: Double,
    @ColumnInfo(name = "prd_image") val prdImage: String?,
    @ColumnInfo(name = "prd_is_active") val prdIsActive: Int,
    @ColumnInfo(name = "cat_name") val catName: String,
    @ColumnInfo(name = "cat_subname") val catSubname: String
)

@Entity(tableName = "pos_cart")
data class PosCartEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cart_id") val cartId: Int = 0,
    @ColumnInfo(name = "cart_user_id") val cartUserId: Int,
    @ColumnInfo(name = "cart_prd_sku") val cartPrdSku: String,
    @ColumnInfo(name = "cart_qty") val cartQty: Int,
    @ColumnInfo(name = "cart_price") val cartPrice: Double,
    @ColumnInfo(name = "cart_cost_price") val cartCostPrice: Double,
    @ColumnInfo(name = "cart_subtotal") val cartSubtotal: Double,
    @ColumnInfo(name = "cart_status") val cartStatus: Int = 0
)

data class CartItemWithProduct(
    @ColumnInfo(name = "cart_id") val cartId: Int,
    @ColumnInfo(name = "cart_user_id") val cartUserId: Int,
    @ColumnInfo(name = "cart_prd_sku") val cartPrdSku: String,
    @ColumnInfo(name = "cart_qty") val cartQty: Int,
    @ColumnInfo(name = "cart_price") val cartPrice: Double,
    @ColumnInfo(name = "cart_cost_price") val cartCostPrice: Double,
    @ColumnInfo(name = "cart_subtotal") val cartSubtotal: Double,
    @ColumnInfo(name = "cart_status") val cartStatus: Int,
    @ColumnInfo(name = "prd_name") val prdName: String,
    @ColumnInfo(name = "prd_image") val prdImage: String?
)

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey
    @ColumnInfo(name = "sls_invoice_number") val slsInvoiceNumber: String,
    @ColumnInfo(name = "sls_transaction_date") val slsTransactionDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
    @ColumnInfo(name = "sls_user_id") val slsUserId: Int,
    @ColumnInfo(name = "sls_subtotal") val slsSubtotal: Double,
    @ColumnInfo(name = "sls_discount_amount") val slsDiscountAmount: Double = 0.0,
    @ColumnInfo(name = "sls_grand_total") val slsGrandTotal: Double,
    @ColumnInfo(name = "sls_paid_amount") val slsPaidAmount: Double,
    @ColumnInfo(name = "sls_change_amount") val slsChangeAmount: Double,
    @ColumnInfo(name = "sls_payment_method") val slsPaymentMethod: String = "Tunai",
    @ColumnInfo(name = "sls_total_item") val slsTotalItem: Int
)

data class SaleWithUser(
    @ColumnInfo(name = "sls_invoice_number") val slsInvoiceNumber: String,
    @ColumnInfo(name = "sls_transaction_date") val slsTransactionDate: String,
    @ColumnInfo(name = "sls_user_id") val slsUserId: Int,
    @ColumnInfo(name = "sls_subtotal") val slsSubtotal: Double,
    @ColumnInfo(name = "sls_discount_amount") val slsDiscountAmount: Double,
    @ColumnInfo(name = "sls_grand_total") val slsGrandTotal: Double,
    @ColumnInfo(name = "sls_paid_amount") val slsPaidAmount: Double,
    @ColumnInfo(name = "sls_change_amount") val slsChangeAmount: Double,
    @ColumnInfo(name = "sls_payment_method") val slsPaymentMethod: String,
    @ColumnInfo(name = "sls_total_item") val slsTotalItem: Int,
    @ColumnInfo(name = "usr_username") val usrUsername: String
)

@Entity(tableName = "sale_items")
data class SaleItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itm_id") val itmId: Int = 0,
    @ColumnInfo(name = "itm_sale_id") val itmSaleId: String,
    @ColumnInfo(name = "itm_sku") val itmSku: String,
    @ColumnInfo(name = "itm_discount") val itmDiscount: Double = 0.0,
    @ColumnInfo(name = "itm_cashback") val itmCashback: Double = 0.0,
    @ColumnInfo(name = "itm_quantity") val itmQuantity: Int,
    @ColumnInfo(name = "itm_unit_price") val itmUnitPrice: Double,
    @ColumnInfo(name = "itm_cost_price") val itmCostPrice: Double,
    @ColumnInfo(name = "itm_subtotal") val itmSubtotal: Double
)

data class SaleItemWithProduct(
    @ColumnInfo(name = "itm_id") val itmId: Int,
    @ColumnInfo(name = "itm_sale_id") val itmSaleId: String,
    @ColumnInfo(name = "itm_sku") val itmSku: String,
    @ColumnInfo(name = "itm_discount") val itmDiscount: Double,
    @ColumnInfo(name = "itm_cashback") val itmCashback: Double,
    @ColumnInfo(name = "itm_quantity") val itmQuantity: Int,
    @ColumnInfo(name = "itm_unit_price") val itmUnitPrice: Double,
    @ColumnInfo(name = "itm_cost_price") val itmCostPrice: Double,
    @ColumnInfo(name = "itm_subtotal") val itmSubtotal: Double,
    @ColumnInfo(name = "prd_name") val prdName: String
)

@Entity(tableName = "pos_logs")
data class PosLogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id") val logId: Int = 0,
    @ColumnInfo(name = "log_user_id") val logUserId: Int,
    @ColumnInfo(name = "log_prd_sku") val logPrdSku: String,
    @ColumnInfo(name = "log_action") val logAction: String,
    @ColumnInfo(name = "log_old_qty") val logOldQty: Int,
    @ColumnInfo(name = "log_new_qty") val logNewQty: Int,
    @ColumnInfo(name = "log_description") val logDescription: String? = null,
    @ColumnInfo(name = "log_timestamp") val logTimestamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
)

data class PosLogWithDetails(
    @ColumnInfo(name = "log_id") val logId: Int,
    @ColumnInfo(name = "log_user_id") val logUserId: Int,
    @ColumnInfo(name = "log_prd_sku") val logPrdSku: String,
    @ColumnInfo(name = "log_action") val logAction: String,
    @ColumnInfo(name = "log_old_qty") val logOldQty: Int,
    @ColumnInfo(name = "log_new_qty") val logNewQty: Int,
    @ColumnInfo(name = "log_description") val logDescription: String?,
    @ColumnInfo(name = "log_timestamp") val logTimestamp: String,
    @ColumnInfo(name = "usr_name") val usrName: String,
    @ColumnInfo(name = "prd_name") val prdName: String
)

@Entity(tableName = "app_settings")
data class AppSettingEntity(
    @PrimaryKey
    @ColumnInfo(name = "set_id") val setId: Int = 1,
    @ColumnInfo(name = "set_warung_name") val setWarungName: String = "WARUNG MAKAN PIK",
    @ColumnInfo(name = "set_address") val setAddress: String = "Jl. Pantai Indah Kapuk No. 123",
    @ColumnInfo(name = "set_phone") val setPhone: String = "0812-3456-7890",
    @ColumnInfo(name = "set_default_printer") val setDefaultPrinter: String? = null,
    @ColumnInfo(name = "set_paper_size") val setPaperSize: Int = 80,
    @ColumnInfo(name = "set_margin") val setMargin: Double = 5.0
)

// Data Classes for Reports
data class ProfitReportRow(
    val date: String,
    @ColumnInfo(name = "total_invoices") val totalInvoices: Int,
    @ColumnInfo(name = "total_revenue") val totalRevenue: Double,
    @ColumnInfo(name = "total_cost") val totalCost: Double
)

data class ItemSalesReportRow(
    @ColumnInfo(name = "itm_sku") val itmSku: String,
    @ColumnInfo(name = "prd_name") val prdName: String,
    @ColumnInfo(name = "cat_name") val catName: String,
    @ColumnInfo(name = "cat_subname") val catSubname: String,
    @ColumnInfo(name = "cost_price") val costPrice: Double,
    @ColumnInfo(name = "selling_price") val sellingPrice: Double,
    @ColumnInfo(name = "total_qty") val totalQty: Int,
    @ColumnInfo(name = "total_cost") val totalCost: Double,
    @ColumnInfo(name = "total_sales") val totalSales: Double,
    @ColumnInfo(name = "total_profit") val totalProfit: Double,
    @ColumnInfo(name = "total_trx") val totalTrx: Int
)

data class CategorySalesReportRow(
    @ColumnInfo(name = "cat_name") val catName: String,
    @ColumnInfo(name = "total_qty") val totalQty: Int,
    @ColumnInfo(name = "total_cost") val totalCost: Double,
    @ColumnInfo(name = "total_sales") val totalSales: Double,
    @ColumnInfo(name = "total_profit") val totalProfit: Double,
    @ColumnInfo(name = "total_trx") val totalTrx: Int
)

data class SubCategorySalesReportRow(
    @ColumnInfo(name = "cat_name") val catName: String,
    @ColumnInfo(name = "cat_subname") val catSubname: String,
    @ColumnInfo(name = "total_qty") val totalQty: Int,
    @ColumnInfo(name = "total_cost") val totalCost: Double,
    @ColumnInfo(name = "total_sales") val totalSales: Double,
    @ColumnInfo(name = "total_profit") val totalProfit: Double,
    @ColumnInfo(name = "total_trx") val totalTrx: Int
)

data class TodayStats(
    val count: Int,
    val omzet: Double
)
