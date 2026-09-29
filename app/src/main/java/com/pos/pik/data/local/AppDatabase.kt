package com.pos.pik.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        AppSettingEntity::class,
        MasterStockEntity::class,
        InventoryIncomingEntity::class,
        InventoryDamagedEntity::class,
        InventoryInternalUseEntity::class
    ],
    version = 2,
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
    abstract fun masterStockDao(): MasterStockDao
    abstract fun inventoryIncomingDao(): InventoryIncomingDao
    abstract fun inventoryDamagedDao(): InventoryDamagedDao
    abstract fun inventoryInternalUseDao(): InventoryInternalUseDao

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
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun closeAndResetDatabase() {
            synchronized(this) {
                try {
                    INSTANCE?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                INSTANCE = null
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
                RoleEntity(rolId = 2, rolName = "Kasir"),
                RoleEntity(rolId = 3, rolName = "Logistik")
            )
            db.roleDao().insertAll(roles)

            // Seed Users
            val users = listOf(
                UserEntity(usrId = 1, usrRoleId = 1, usrName = "ADMINISTRATOR", usrUsername = "admin", usrPassword = "123", usrPhone = "08123456789", usrIsActive = 1),
                UserEntity(usrId = 2, usrRoleId = 2, usrName = "KASIR PIK", usrUsername = "kasir", usrPassword = "123", usrPhone = "08987654321", usrIsActive = 1),
                UserEntity(usrId = 3, usrRoleId = 3, usrName = "LOGISTIK PIK", usrUsername = "logistik", usrPassword = "123", usrPhone = "08111112222", usrIsActive = 1)
            )
            db.userDao().insertAll(users)

            // Seed App Settings (Default Settings)
            db.appSettingDao().updateSettings(
                AppSettingEntity(
                    setId = 1,
                    setWarungName = "POS PIK",
                    setAddress = "",
                    setPhone = "",
                    setDefaultPrinter = null,
                    setPaperSize = 80,
                    setMargin = 5.0
                )
            )
        }
    }
}
