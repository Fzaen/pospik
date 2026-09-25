package com.pos.pik.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        RoleEntity::class,
        UserEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        PosCartEntity::class,
        SaleEntity::class,
        SaleItemEntity::class,
        PosLogEntity::class,
        AppSettingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roleDao(): RoleDao
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun posCartDao(): PosCartDao
    abstract fun saleDao(): SaleDao
    abstract fun posLogDao(): PosLogDao
    abstract fun appSettingDao(): AppSettingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pos_pik_database.db"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database)
                }
            }
        }

        suspend fun populateInitialData(db: AppDatabase) {
            // Seed Roles
            val roles = listOf(
                RoleEntity(rolId = 1, rolName = "Admin"),
                RoleEntity(rolId = 2, rolName = "Kasir")
            )
            db.roleDao().insertAll(roles)

            // Seed Users
            val users = listOf(
                UserEntity(usrId = 1, usrRoleId = 1, usrName = "ADMINISTRATOR", usrUsername = "admin", usrPassword = "123", usrPhone = "08123456789", usrIsActive = 1),
                UserEntity(usrId = 2, usrRoleId = 2, usrName = "KASIR PIK", usrUsername = "kasir", usrPassword = "123", usrPhone = "08987654321", usrIsActive = 1)
            )
            db.userDao().insertAll(users)

            // Seed Categories
            val categories = listOf(
                CategoryEntity(catId = 1, catName = "Makanan", catSubname = "Soto & Sop"),
                CategoryEntity(catId = 2, catName = "Makanan", catSubname = "Nasi & Olahan"),
                CategoryEntity(catId = 3, catName = "Minuman", catSubname = "Dingin"),
                CategoryEntity(catId = 4, catName = "Minuman", catSubname = "Hangat"),
                CategoryEntity(catId = 5, catName = "Cemilan", catSubname = "Gorengan")
            )
            db.categoryDao().insertAll(categories)

            // Seed Products
            val products = listOf(
                ProductEntity(prdSku = "10001", prdCategoryId = 1, prdName = "SOTO BANJAR KUIN", prdCostPrice = 15000.0, prdSellingPrice = 22000.0, prdImage = "file:///android_asset/img/Soto_Banjar.jpg", prdIsActive = 1),
                ProductEntity(prdSku = "10002", prdCategoryId = 1, prdName = "NASI SOP BANJAR", prdCostPrice = 16000.0, prdSellingPrice = 24000.0, prdImage = "file:///android_asset/img/nasi_sop_Banjar.jpg", prdIsActive = 1),
                ProductEntity(prdSku = "10003", prdCategoryId = 2, prdName = "RAWON SAPI KHAS", prdCostPrice = 18000.0, prdSellingPrice = 25000.0, prdImage = "file:///android_asset/img/rawon_banjar.jpg", prdIsActive = 1),
                ProductEntity(prdSku = "10004", prdCategoryId = 2, prdName = "SATE BANJAR", prdCostPrice = 18000.0, prdSellingPrice = 25000.0, prdImage = "file:///android_asset/img/sate_banjar.jpg", prdIsActive = 1),
                ProductEntity(prdSku = "20001", prdCategoryId = 3, prdName = "ES TEH MANIS", prdCostPrice = 2000.0, prdSellingPrice = 5000.0, prdImage = "file:///android_asset/img/es_teh.jpeg", prdIsActive = 1),
                ProductEntity(prdSku = "20002", prdCategoryId = 3, prdName = "ES JERUK PERAS", prdCostPrice = 3000.0, prdSellingPrice = 8000.0, prdImage = "file:///android_asset/img/es_jeruk.jpeg", prdIsActive = 1),
                ProductEntity(prdSku = "20003", prdCategoryId = 3, prdName = "AIR ES", prdCostPrice = 1000.0, prdSellingPrice = 2000.0, prdImage = "file:///android_asset/img/air_es.jpeg", prdIsActive = 1),
                ProductEntity(prdSku = "20004", prdCategoryId = 3, prdName = "AIR PUTIH", prdCostPrice = 1000.0, prdSellingPrice = 3000.0, prdImage = "file:///android_asset/img/air_putih.jpeg", prdIsActive = 1),
                ProductEntity(prdSku = "20005", prdCategoryId = 3, prdName = "LE MINERALE 600ML", prdCostPrice = 2500.0, prdSellingPrice = 5000.0, prdImage = "file:///android_asset/img/leminerale_600ml.jpeg", prdIsActive = 1),
                ProductEntity(prdSku = "20006", prdCategoryId = 3, prdName = "PROF 600ML", prdCostPrice = 2500.0, prdSellingPrice = 5000.0, prdImage = "file:///android_asset/img/prof_600ml.jpeg", prdIsActive = 1),
                ProductEntity(prdSku = "20007", prdCategoryId = 3, prdName = "SIRUP MANIS", prdCostPrice = 3000.0, prdSellingPrice = 7000.0, prdImage = "file:///android_asset/img/sirup.jpeg", prdIsActive = 1),
                ProductEntity(prdSku = "30001", prdCategoryId = 5, prdName = "KERUPUK UDANG", prdCostPrice = 1000.0, prdSellingPrice = 3000.0, prdImage = "file:///android_asset/img/kerupuk_udang.jpeg", prdIsActive = 1),
                ProductEntity(prdSku = "30002", prdCategoryId = 5, prdName = "KACANG PUTIH", prdCostPrice = 1000.0, prdSellingPrice = 3000.0, prdImage = "file:///android_asset/img/kacang_putih.jpeg", prdIsActive = 1)
            )
            db.productDao().insertAll(products)

            // Seed App Settings
            db.appSettingDao().updateSettings(
                AppSettingEntity(
                    setId = 1,
                    setWarungName = "WARUNG MAKAN PIK",
                    setAddress = "Jl. Pantai Indah Kapuk No. 123, Jakarta",
                    setPhone = "0812-3456-7890",
                    setDefaultPrinter = null,
                    setPaperSize = 80,
                    setMargin = 5.0
                )
            )
        }
    }
}
