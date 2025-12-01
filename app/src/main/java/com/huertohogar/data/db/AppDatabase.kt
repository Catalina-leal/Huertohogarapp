package com.huertohogar.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.huertohogar.data.model.*
import com.huertohogar.data.repository.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Nuestra CLASE PRINCIPAL DE LA BASE DE DATOS
@Database(
    entities = [
        CartItem::class,
        Product::class,
        User::class,
        Order::class,
        OrderItem::class,
        Review::class,
        BlogPost::class,
        StoreLocation::class
    ],
    version = 3, // Incrementamos para agregar campos isActive y role
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun cartDao(): CartDao
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun reviewDao(): ReviewDao
    abstract fun blogPostDao(): BlogPostDao
    abstract fun storeLocationDao(): StoreLocationDao

    //  PATRÓN SINGLETON: con este patron estamos asegurando que solo exista una instancia.
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        private val databaseScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huertohogar_database"
                )
                    .fallbackToDestructiveMigration() // esta linea de codigo es util en desarrollo
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    
    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            databaseScope.launch {
                val dbInstance = INSTANCE
                if (dbInstance != null) {
                    populateDatabase(dbInstance)
                }
            }
        }
        
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // Verificamos y poblamos la base de datos de forma asíncrona
            databaseScope.launch {
                val dbInstance = INSTANCE
                if (dbInstance != null) {
                    try {
                        val productDao = dbInstance.productDao()
                        val count = productDao.getProductCount()
                        if (count == 0) {
                            populateDatabase(dbInstance)
                        }
                    } catch (e: Exception) {
                        // Registramos error pero no bloqueamos
                        android.util.Log.e("AppDatabase", "Error checking product count", e)
                    }
                }
            }
        }
        
        private suspend fun populateDatabase(database: AppDatabase) {
            val productDao = database.productDao()
            val cartDao = database.cartDao()
            
            // Obtenemos productos iniciales del repositorio
            val repository = HomeRepository(cartDao, productDao)
            val initialProducts = repository.getInitialProducts()
            productDao.insertAll(initialProducts)
        }
    }
}