package com.example.milsaboresapp.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.milsaboresapp.data.local.entity.ProductoEntity
import com.example.milsaboresapp.data.local.entity.UserEntity

@Database(
    entities = [
        ProductoEntity::class,  // ok aunque no tenga DAO
        UserEntity::class
    ],
    version = 5,              // versión subida porque cambiamos UserEntity
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Solo el DAO que realmente existe
    abstract fun userDao(): UserDao

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
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
