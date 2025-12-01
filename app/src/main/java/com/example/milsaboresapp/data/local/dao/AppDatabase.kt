package com.example.milsaboresapp.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.milsaboresapp.data.local.dao.ProductDao
import com.example.milsaboresapp.data.local.entity.ProductoEntity
import com.example.milsaboresapp.data.local.entity.UserEntity

@Database(
    entities = [
        ProductoEntity::class,  // ok aunque no tenga DAO
        UserEntity::class
    ],
    version = 6,              // aumentada por cambios en UserEntity
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Solo el DAO que realmente existe
    abstract fun userDao(): UserDao
    // DAO para productos (a agregar)
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "milsabores.db"
                )
                    // En desarrollo: borra y recrea la BD al cambiar versión
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            seedAdmin(db)
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            seedAdmin(db)
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun seedAdmin(db: SupportSQLiteDatabase) {
            // Inserta la cuenta admin solo si no existe previamente
            db.execSQL(
                """
                INSERT INTO users (
                    run,
                    firstName,
                    lastName,
                    email,
                    phone,
                    birthDate,
                    region,
                    commune,
                    address,
                    promoCode,
                    acceptsPromotions,
                    password,
                    role,
                    photoUri
                )
                SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                WHERE NOT EXISTS (
                    SELECT 1 FROM users WHERE email = ?
                )
                """.trimIndent(),
                arrayOf<Any?>(
                    "",
                    "Admin",
                    "Duoc",
                    "admin@duoc.cl",
                    "",
                    "",
                    "",
                    "",
                    "",
                    null,
                    0,
                    "1234",
                    "ADMIN",
                    null,
                    "admin@duoc.cl"
                )
            )

            // Refuerza los datos críticos del administrador por si ya existía
            db.execSQL(
                """
                UPDATE users
                SET
                    firstName = ?,
                    lastName = ?,
                    password = ?,
                    role = ?,
                    acceptsPromotions = ?,
                    promoCode = NULL,
                    photoUri = NULL
                WHERE email = ?
                """.trimIndent(),
                arrayOf<Any?>(
                    "Admin",
                    "Duoc",
                    "1234",
                    "ADMIN",
                    0,
                    "admin@duoc.cl"
                )
            )
        }
    }
}
