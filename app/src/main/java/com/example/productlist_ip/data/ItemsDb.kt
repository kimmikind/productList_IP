package com.example.productlist_ip.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemsDb : RoomDatabase() {

    abstract fun itemDao(): ItemDao  // метод для доступа к DAO

    companion object {
        @Volatile
        private var INSTANCE: ItemsDb? = null

        // Создание или получение экземпляра базы данных
        fun getDatabase(context: Context): ItemsDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemsDb::class.java,
                    "items_db"  // имя базы данных
                ).addCallback(callback)  // добавляем Callback
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Callback для инициализации начальных данных
        private val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d("Database", "Database created")
                CoroutineScope(Dispatchers.IO).launch {
                    val itemDao = INSTANCE?.itemDao()
                    if (itemDao != null) {
                        itemDao.insertAll(
                            Item(name = "Iphone 13", time = 1633046400000, tags = "Телефон, Новый, Распродажа", amount = 15),
                            Item(name = "Samsung Galaxy S21", time = 1633132800000, tags = "Телефон, Хит", amount = 30),
                            Item(name = "PlayStation 5", time = 1633219200000, tags = "Игровая приставка, Акция, Распродажа", amount = 7),
                            Item(name = "LG OLED TV", time = 1633305600000, tags = "Телевизор, Эксклюзив, Ограниченный", amount = 22),
                            Item(name = "Apple Watch Series 7", time = 1633392000000, tags = "Часы, Новый, Рекомендуем", amount = 0),
                            Item(name = "Xiaomi Mi 11", time = 1633478400000, tags = "Телефон, Скидка, Распродажа", amount = 5),
                            Item(name = "MacBook Air M1", time = 1633564800000, tags = "Ноутбук, Тренд", amount = 12),
                            Item(name = "Amazon Kindle Paperwhite", time = 1633651200000, tags = "Электронная книга, Последний шанс, Ограниченный", amount = 18),
                            Item(name = "Fitbit Charge 5", time = 1633737600000, tags = "", amount = 27),
                            Item(name = "GoPro Hero 10", time = 1633382400000, tags = "Камера, Эксклюзив", amount = 25)
                        )
                    }


                }
            }
        }
    }
}