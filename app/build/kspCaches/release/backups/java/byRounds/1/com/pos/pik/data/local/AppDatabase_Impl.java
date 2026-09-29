package com.pos.pik.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile RoleDao _roleDao;

  private volatile UserDao _userDao;

  private volatile CategoryDao _categoryDao;

  private volatile ProductDao _productDao;

  private volatile PosCartDao _posCartDao;

  private volatile SaleDao _saleDao;

  private volatile PosLogDao _posLogDao;

  private volatile AppSettingDao _appSettingDao;

  private volatile MasterStockDao _masterStockDao;

  private volatile InventoryIncomingDao _inventoryIncomingDao;

  private volatile InventoryDamagedDao _inventoryDamagedDao;

  private volatile InventoryInternalUseDao _inventoryInternalUseDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `roles` (`rol_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `rol_name` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`usr_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `usr_role_id` INTEGER NOT NULL, `usr_name` TEXT NOT NULL, `usr_username` TEXT NOT NULL, `usr_password` TEXT NOT NULL, `usr_phone` TEXT, `usr_is_active` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`cat_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cat_name` TEXT NOT NULL, `cat_subname` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `products` (`prd_sku` TEXT NOT NULL, `prd_category_id` INTEGER NOT NULL, `prd_name` TEXT NOT NULL, `prd_cost_price` REAL NOT NULL, `prd_selling_price` REAL NOT NULL, `prd_image` TEXT, `prd_is_active` INTEGER NOT NULL, PRIMARY KEY(`prd_sku`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pos_cart` (`cart_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cart_user_id` INTEGER NOT NULL, `cart_prd_sku` TEXT NOT NULL, `cart_qty` INTEGER NOT NULL, `cart_price` REAL NOT NULL, `cart_cost_price` REAL NOT NULL, `cart_subtotal` REAL NOT NULL, `cart_status` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sales` (`sls_invoice_number` TEXT NOT NULL, `sls_transaction_date` TEXT NOT NULL, `sls_user_id` INTEGER NOT NULL, `sls_subtotal` REAL NOT NULL, `sls_discount_amount` REAL NOT NULL, `sls_grand_total` REAL NOT NULL, `sls_paid_amount` REAL NOT NULL, `sls_change_amount` REAL NOT NULL, `sls_payment_method` TEXT NOT NULL, `sls_total_item` INTEGER NOT NULL, PRIMARY KEY(`sls_invoice_number`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sale_items` (`itm_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `itm_sale_id` TEXT NOT NULL, `itm_sku` TEXT NOT NULL, `itm_discount` REAL NOT NULL, `itm_cashback` REAL NOT NULL, `itm_quantity` INTEGER NOT NULL, `itm_unit_price` REAL NOT NULL, `itm_cost_price` REAL NOT NULL, `itm_subtotal` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pos_logs` (`log_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `log_user_id` INTEGER NOT NULL, `log_prd_sku` TEXT NOT NULL, `log_action` TEXT NOT NULL, `log_old_qty` INTEGER NOT NULL, `log_new_qty` INTEGER NOT NULL, `log_description` TEXT, `log_timestamp` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_settings` (`set_id` INTEGER NOT NULL, `set_warung_name` TEXT NOT NULL, `set_address` TEXT NOT NULL, `set_phone` TEXT NOT NULL, `set_default_printer` TEXT, `set_paper_size` INTEGER NOT NULL, `set_margin` REAL NOT NULL, PRIMARY KEY(`set_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `master_stock` (`st_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `st_prd_sku` TEXT NOT NULL, `st_year` INTEGER NOT NULL, `st_initial_qty` INTEGER NOT NULL, `st_incoming_qty` INTEGER NOT NULL, `st_sales_qty` INTEGER NOT NULL, `st_damaged_qty` INTEGER NOT NULL, `st_internal_use_qty` INTEGER NOT NULL, `st_final_qty` INTEGER NOT NULL, `st_last_updated` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `inventory_incoming` (`inc_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `inc_date` TEXT NOT NULL, `inc_user_id` INTEGER NOT NULL, `inc_prd_sku` TEXT NOT NULL, `inc_package_qty` INTEGER NOT NULL, `inc_fraction` INTEGER NOT NULL, `inc_total_qty` INTEGER NOT NULL, `inc_total_cost` REAL NOT NULL, `inc_unit_cost` REAL NOT NULL, `inc_note` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `inventory_damaged` (`dmg_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dmg_date` TEXT NOT NULL, `dmg_user_id` INTEGER NOT NULL, `dmg_prd_sku` TEXT NOT NULL, `dmg_qty` INTEGER NOT NULL, `dmg_reason` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `inventory_internal_use` (`use_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `use_date` TEXT NOT NULL, `use_user_id` INTEGER NOT NULL, `use_prd_sku` TEXT NOT NULL, `use_qty` INTEGER NOT NULL, `use_note` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '51d968aacbbf3aa3b3ba9ac2fd5ef98e')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `roles`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `products`");
        db.execSQL("DROP TABLE IF EXISTS `pos_cart`");
        db.execSQL("DROP TABLE IF EXISTS `sales`");
        db.execSQL("DROP TABLE IF EXISTS `sale_items`");
        db.execSQL("DROP TABLE IF EXISTS `pos_logs`");
        db.execSQL("DROP TABLE IF EXISTS `app_settings`");
        db.execSQL("DROP TABLE IF EXISTS `master_stock`");
        db.execSQL("DROP TABLE IF EXISTS `inventory_incoming`");
        db.execSQL("DROP TABLE IF EXISTS `inventory_damaged`");
        db.execSQL("DROP TABLE IF EXISTS `inventory_internal_use`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRoles = new HashMap<String, TableInfo.Column>(2);
        _columnsRoles.put("rol_id", new TableInfo.Column("rol_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoles.put("rol_name", new TableInfo.Column("rol_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRoles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRoles = new TableInfo("roles", _columnsRoles, _foreignKeysRoles, _indicesRoles);
        final TableInfo _existingRoles = TableInfo.read(db, "roles");
        if (!_infoRoles.equals(_existingRoles)) {
          return new RoomOpenHelper.ValidationResult(false, "roles(com.pos.pik.data.local.RoleEntity).\n"
                  + " Expected:\n" + _infoRoles + "\n"
                  + " Found:\n" + _existingRoles);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(7);
        _columnsUsers.put("usr_id", new TableInfo.Column("usr_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("usr_role_id", new TableInfo.Column("usr_role_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("usr_name", new TableInfo.Column("usr_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("usr_username", new TableInfo.Column("usr_username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("usr_password", new TableInfo.Column("usr_password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("usr_phone", new TableInfo.Column("usr_phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("usr_is_active", new TableInfo.Column("usr_is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.pos.pik.data.local.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(3);
        _columnsCategories.put("cat_id", new TableInfo.Column("cat_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("cat_name", new TableInfo.Column("cat_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("cat_subname", new TableInfo.Column("cat_subname", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.pos.pik.data.local.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsProducts = new HashMap<String, TableInfo.Column>(7);
        _columnsProducts.put("prd_sku", new TableInfo.Column("prd_sku", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("prd_category_id", new TableInfo.Column("prd_category_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("prd_name", new TableInfo.Column("prd_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("prd_cost_price", new TableInfo.Column("prd_cost_price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("prd_selling_price", new TableInfo.Column("prd_selling_price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("prd_image", new TableInfo.Column("prd_image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("prd_is_active", new TableInfo.Column("prd_is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProducts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProducts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProducts = new TableInfo("products", _columnsProducts, _foreignKeysProducts, _indicesProducts);
        final TableInfo _existingProducts = TableInfo.read(db, "products");
        if (!_infoProducts.equals(_existingProducts)) {
          return new RoomOpenHelper.ValidationResult(false, "products(com.pos.pik.data.local.ProductEntity).\n"
                  + " Expected:\n" + _infoProducts + "\n"
                  + " Found:\n" + _existingProducts);
        }
        final HashMap<String, TableInfo.Column> _columnsPosCart = new HashMap<String, TableInfo.Column>(8);
        _columnsPosCart.put("cart_id", new TableInfo.Column("cart_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosCart.put("cart_user_id", new TableInfo.Column("cart_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosCart.put("cart_prd_sku", new TableInfo.Column("cart_prd_sku", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosCart.put("cart_qty", new TableInfo.Column("cart_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosCart.put("cart_price", new TableInfo.Column("cart_price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosCart.put("cart_cost_price", new TableInfo.Column("cart_cost_price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosCart.put("cart_subtotal", new TableInfo.Column("cart_subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosCart.put("cart_status", new TableInfo.Column("cart_status", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPosCart = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPosCart = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPosCart = new TableInfo("pos_cart", _columnsPosCart, _foreignKeysPosCart, _indicesPosCart);
        final TableInfo _existingPosCart = TableInfo.read(db, "pos_cart");
        if (!_infoPosCart.equals(_existingPosCart)) {
          return new RoomOpenHelper.ValidationResult(false, "pos_cart(com.pos.pik.data.local.PosCartEntity).\n"
                  + " Expected:\n" + _infoPosCart + "\n"
                  + " Found:\n" + _existingPosCart);
        }
        final HashMap<String, TableInfo.Column> _columnsSales = new HashMap<String, TableInfo.Column>(10);
        _columnsSales.put("sls_invoice_number", new TableInfo.Column("sls_invoice_number", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_transaction_date", new TableInfo.Column("sls_transaction_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_user_id", new TableInfo.Column("sls_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_subtotal", new TableInfo.Column("sls_subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_discount_amount", new TableInfo.Column("sls_discount_amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_grand_total", new TableInfo.Column("sls_grand_total", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_paid_amount", new TableInfo.Column("sls_paid_amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_change_amount", new TableInfo.Column("sls_change_amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_payment_method", new TableInfo.Column("sls_payment_method", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("sls_total_item", new TableInfo.Column("sls_total_item", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSales = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSales = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSales = new TableInfo("sales", _columnsSales, _foreignKeysSales, _indicesSales);
        final TableInfo _existingSales = TableInfo.read(db, "sales");
        if (!_infoSales.equals(_existingSales)) {
          return new RoomOpenHelper.ValidationResult(false, "sales(com.pos.pik.data.local.SaleEntity).\n"
                  + " Expected:\n" + _infoSales + "\n"
                  + " Found:\n" + _existingSales);
        }
        final HashMap<String, TableInfo.Column> _columnsSaleItems = new HashMap<String, TableInfo.Column>(9);
        _columnsSaleItems.put("itm_id", new TableInfo.Column("itm_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("itm_sale_id", new TableInfo.Column("itm_sale_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("itm_sku", new TableInfo.Column("itm_sku", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("itm_discount", new TableInfo.Column("itm_discount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("itm_cashback", new TableInfo.Column("itm_cashback", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("itm_quantity", new TableInfo.Column("itm_quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("itm_unit_price", new TableInfo.Column("itm_unit_price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("itm_cost_price", new TableInfo.Column("itm_cost_price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaleItems.put("itm_subtotal", new TableInfo.Column("itm_subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSaleItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSaleItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSaleItems = new TableInfo("sale_items", _columnsSaleItems, _foreignKeysSaleItems, _indicesSaleItems);
        final TableInfo _existingSaleItems = TableInfo.read(db, "sale_items");
        if (!_infoSaleItems.equals(_existingSaleItems)) {
          return new RoomOpenHelper.ValidationResult(false, "sale_items(com.pos.pik.data.local.SaleItemEntity).\n"
                  + " Expected:\n" + _infoSaleItems + "\n"
                  + " Found:\n" + _existingSaleItems);
        }
        final HashMap<String, TableInfo.Column> _columnsPosLogs = new HashMap<String, TableInfo.Column>(8);
        _columnsPosLogs.put("log_id", new TableInfo.Column("log_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosLogs.put("log_user_id", new TableInfo.Column("log_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosLogs.put("log_prd_sku", new TableInfo.Column("log_prd_sku", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosLogs.put("log_action", new TableInfo.Column("log_action", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosLogs.put("log_old_qty", new TableInfo.Column("log_old_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosLogs.put("log_new_qty", new TableInfo.Column("log_new_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosLogs.put("log_description", new TableInfo.Column("log_description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPosLogs.put("log_timestamp", new TableInfo.Column("log_timestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPosLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPosLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPosLogs = new TableInfo("pos_logs", _columnsPosLogs, _foreignKeysPosLogs, _indicesPosLogs);
        final TableInfo _existingPosLogs = TableInfo.read(db, "pos_logs");
        if (!_infoPosLogs.equals(_existingPosLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "pos_logs(com.pos.pik.data.local.PosLogEntity).\n"
                  + " Expected:\n" + _infoPosLogs + "\n"
                  + " Found:\n" + _existingPosLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsAppSettings = new HashMap<String, TableInfo.Column>(7);
        _columnsAppSettings.put("set_id", new TableInfo.Column("set_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("set_warung_name", new TableInfo.Column("set_warung_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("set_address", new TableInfo.Column("set_address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("set_phone", new TableInfo.Column("set_phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("set_default_printer", new TableInfo.Column("set_default_printer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("set_paper_size", new TableInfo.Column("set_paper_size", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("set_margin", new TableInfo.Column("set_margin", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppSettings = new TableInfo("app_settings", _columnsAppSettings, _foreignKeysAppSettings, _indicesAppSettings);
        final TableInfo _existingAppSettings = TableInfo.read(db, "app_settings");
        if (!_infoAppSettings.equals(_existingAppSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "app_settings(com.pos.pik.data.local.AppSettingEntity).\n"
                  + " Expected:\n" + _infoAppSettings + "\n"
                  + " Found:\n" + _existingAppSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsMasterStock = new HashMap<String, TableInfo.Column>(10);
        _columnsMasterStock.put("st_id", new TableInfo.Column("st_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_prd_sku", new TableInfo.Column("st_prd_sku", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_year", new TableInfo.Column("st_year", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_initial_qty", new TableInfo.Column("st_initial_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_incoming_qty", new TableInfo.Column("st_incoming_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_sales_qty", new TableInfo.Column("st_sales_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_damaged_qty", new TableInfo.Column("st_damaged_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_internal_use_qty", new TableInfo.Column("st_internal_use_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_final_qty", new TableInfo.Column("st_final_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterStock.put("st_last_updated", new TableInfo.Column("st_last_updated", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMasterStock = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMasterStock = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMasterStock = new TableInfo("master_stock", _columnsMasterStock, _foreignKeysMasterStock, _indicesMasterStock);
        final TableInfo _existingMasterStock = TableInfo.read(db, "master_stock");
        if (!_infoMasterStock.equals(_existingMasterStock)) {
          return new RoomOpenHelper.ValidationResult(false, "master_stock(com.pos.pik.data.local.MasterStockEntity).\n"
                  + " Expected:\n" + _infoMasterStock + "\n"
                  + " Found:\n" + _existingMasterStock);
        }
        final HashMap<String, TableInfo.Column> _columnsInventoryIncoming = new HashMap<String, TableInfo.Column>(10);
        _columnsInventoryIncoming.put("inc_id", new TableInfo.Column("inc_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_date", new TableInfo.Column("inc_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_user_id", new TableInfo.Column("inc_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_prd_sku", new TableInfo.Column("inc_prd_sku", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_package_qty", new TableInfo.Column("inc_package_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_fraction", new TableInfo.Column("inc_fraction", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_total_qty", new TableInfo.Column("inc_total_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_total_cost", new TableInfo.Column("inc_total_cost", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_unit_cost", new TableInfo.Column("inc_unit_cost", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryIncoming.put("inc_note", new TableInfo.Column("inc_note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInventoryIncoming = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInventoryIncoming = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoInventoryIncoming = new TableInfo("inventory_incoming", _columnsInventoryIncoming, _foreignKeysInventoryIncoming, _indicesInventoryIncoming);
        final TableInfo _existingInventoryIncoming = TableInfo.read(db, "inventory_incoming");
        if (!_infoInventoryIncoming.equals(_existingInventoryIncoming)) {
          return new RoomOpenHelper.ValidationResult(false, "inventory_incoming(com.pos.pik.data.local.InventoryIncomingEntity).\n"
                  + " Expected:\n" + _infoInventoryIncoming + "\n"
                  + " Found:\n" + _existingInventoryIncoming);
        }
        final HashMap<String, TableInfo.Column> _columnsInventoryDamaged = new HashMap<String, TableInfo.Column>(6);
        _columnsInventoryDamaged.put("dmg_id", new TableInfo.Column("dmg_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryDamaged.put("dmg_date", new TableInfo.Column("dmg_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryDamaged.put("dmg_user_id", new TableInfo.Column("dmg_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryDamaged.put("dmg_prd_sku", new TableInfo.Column("dmg_prd_sku", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryDamaged.put("dmg_qty", new TableInfo.Column("dmg_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryDamaged.put("dmg_reason", new TableInfo.Column("dmg_reason", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInventoryDamaged = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInventoryDamaged = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoInventoryDamaged = new TableInfo("inventory_damaged", _columnsInventoryDamaged, _foreignKeysInventoryDamaged, _indicesInventoryDamaged);
        final TableInfo _existingInventoryDamaged = TableInfo.read(db, "inventory_damaged");
        if (!_infoInventoryDamaged.equals(_existingInventoryDamaged)) {
          return new RoomOpenHelper.ValidationResult(false, "inventory_damaged(com.pos.pik.data.local.InventoryDamagedEntity).\n"
                  + " Expected:\n" + _infoInventoryDamaged + "\n"
                  + " Found:\n" + _existingInventoryDamaged);
        }
        final HashMap<String, TableInfo.Column> _columnsInventoryInternalUse = new HashMap<String, TableInfo.Column>(6);
        _columnsInventoryInternalUse.put("use_id", new TableInfo.Column("use_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryInternalUse.put("use_date", new TableInfo.Column("use_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryInternalUse.put("use_user_id", new TableInfo.Column("use_user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryInternalUse.put("use_prd_sku", new TableInfo.Column("use_prd_sku", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryInternalUse.put("use_qty", new TableInfo.Column("use_qty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventoryInternalUse.put("use_note", new TableInfo.Column("use_note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInventoryInternalUse = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInventoryInternalUse = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoInventoryInternalUse = new TableInfo("inventory_internal_use", _columnsInventoryInternalUse, _foreignKeysInventoryInternalUse, _indicesInventoryInternalUse);
        final TableInfo _existingInventoryInternalUse = TableInfo.read(db, "inventory_internal_use");
        if (!_infoInventoryInternalUse.equals(_existingInventoryInternalUse)) {
          return new RoomOpenHelper.ValidationResult(false, "inventory_internal_use(com.pos.pik.data.local.InventoryInternalUseEntity).\n"
                  + " Expected:\n" + _infoInventoryInternalUse + "\n"
                  + " Found:\n" + _existingInventoryInternalUse);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "51d968aacbbf3aa3b3ba9ac2fd5ef98e", "12d8489f2eb11bb59ec4951cb9ec5b8e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "roles","users","categories","products","pos_cart","sales","sale_items","pos_logs","app_settings","master_stock","inventory_incoming","inventory_damaged","inventory_internal_use");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `roles`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `products`");
      _db.execSQL("DELETE FROM `pos_cart`");
      _db.execSQL("DELETE FROM `sales`");
      _db.execSQL("DELETE FROM `sale_items`");
      _db.execSQL("DELETE FROM `pos_logs`");
      _db.execSQL("DELETE FROM `app_settings`");
      _db.execSQL("DELETE FROM `master_stock`");
      _db.execSQL("DELETE FROM `inventory_incoming`");
      _db.execSQL("DELETE FROM `inventory_damaged`");
      _db.execSQL("DELETE FROM `inventory_internal_use`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RoleDao.class, RoleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProductDao.class, ProductDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PosCartDao.class, PosCartDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SaleDao.class, SaleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PosLogDao.class, PosLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AppSettingDao.class, AppSettingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MasterStockDao.class, MasterStockDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(InventoryIncomingDao.class, InventoryIncomingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(InventoryDamagedDao.class, InventoryDamagedDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(InventoryInternalUseDao.class, InventoryInternalUseDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RoleDao roleDao() {
    if (_roleDao != null) {
      return _roleDao;
    } else {
      synchronized(this) {
        if(_roleDao == null) {
          _roleDao = new RoleDao_Impl(this);
        }
        return _roleDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public ProductDao productDao() {
    if (_productDao != null) {
      return _productDao;
    } else {
      synchronized(this) {
        if(_productDao == null) {
          _productDao = new ProductDao_Impl(this);
        }
        return _productDao;
      }
    }
  }

  @Override
  public PosCartDao posCartDao() {
    if (_posCartDao != null) {
      return _posCartDao;
    } else {
      synchronized(this) {
        if(_posCartDao == null) {
          _posCartDao = new PosCartDao_Impl(this);
        }
        return _posCartDao;
      }
    }
  }

  @Override
  public SaleDao saleDao() {
    if (_saleDao != null) {
      return _saleDao;
    } else {
      synchronized(this) {
        if(_saleDao == null) {
          _saleDao = new SaleDao_Impl(this);
        }
        return _saleDao;
      }
    }
  }

  @Override
  public PosLogDao posLogDao() {
    if (_posLogDao != null) {
      return _posLogDao;
    } else {
      synchronized(this) {
        if(_posLogDao == null) {
          _posLogDao = new PosLogDao_Impl(this);
        }
        return _posLogDao;
      }
    }
  }

  @Override
  public AppSettingDao appSettingDao() {
    if (_appSettingDao != null) {
      return _appSettingDao;
    } else {
      synchronized(this) {
        if(_appSettingDao == null) {
          _appSettingDao = new AppSettingDao_Impl(this);
        }
        return _appSettingDao;
      }
    }
  }

  @Override
  public MasterStockDao masterStockDao() {
    if (_masterStockDao != null) {
      return _masterStockDao;
    } else {
      synchronized(this) {
        if(_masterStockDao == null) {
          _masterStockDao = new MasterStockDao_Impl(this);
        }
        return _masterStockDao;
      }
    }
  }

  @Override
  public InventoryIncomingDao inventoryIncomingDao() {
    if (_inventoryIncomingDao != null) {
      return _inventoryIncomingDao;
    } else {
      synchronized(this) {
        if(_inventoryIncomingDao == null) {
          _inventoryIncomingDao = new InventoryIncomingDao_Impl(this);
        }
        return _inventoryIncomingDao;
      }
    }
  }

  @Override
  public InventoryDamagedDao inventoryDamagedDao() {
    if (_inventoryDamagedDao != null) {
      return _inventoryDamagedDao;
    } else {
      synchronized(this) {
        if(_inventoryDamagedDao == null) {
          _inventoryDamagedDao = new InventoryDamagedDao_Impl(this);
        }
        return _inventoryDamagedDao;
      }
    }
  }

  @Override
  public InventoryInternalUseDao inventoryInternalUseDao() {
    if (_inventoryInternalUseDao != null) {
      return _inventoryInternalUseDao;
    } else {
      synchronized(this) {
        if(_inventoryInternalUseDao == null) {
          _inventoryInternalUseDao = new InventoryInternalUseDao_Impl(this);
        }
        return _inventoryInternalUseDao;
      }
    }
  }
}
