package com.example.productlist_ip

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.productlist_ip.data.Item
import com.example.productlist_ip.data.ItemDao
import com.example.productlist_ip.data.ItemsDb
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemDAOTest {
    private lateinit var database: ItemsDb
    private lateinit var dao: ItemDao
    private val TEST_DB_NAME = "items_db"

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Удаляем предыдущую тестовую БД, если существует
        context.deleteDatabase(TEST_DB_NAME)

        // Создаем файловую БД
        database = Room.databaseBuilder(
            context,
            ItemsDb::class.java,
            TEST_DB_NAME
        )
            .allowMainThreadQueries()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("TestDB", "Test database created at: ${context.getDatabasePath(TEST_DB_NAME)}")
                }
            })
            .build()

        dao = database.itemDao()
    }

    @Test
    fun testDataIsReadFromFileDatabase() {
        runBlocking {
            //Подготовка тестовых данных
            val testItems = listOf(
                Item(name = "Test Product 1", time = System.currentTimeMillis(), tags = "test", amount = 10),
                Item(name = "Test Product 2", time = System.currentTimeMillis(), tags = "test", amount = 20)
            )
            dao.insertAll(*testItems.toTypedArray())

            // заново открываем БД
            database.close()
            database = Room.databaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ItemsDb::class.java,
                TEST_DB_NAME
            ).allowMainThreadQueries().build()
            dao = database.itemDao()

            // Проверяем чтение данных
            val loadedItems = dao.getAllItems().first()

            // Проверки
            assertEquals("Количество загруженных элементов", testItems.size, loadedItems.size)
            assertTrue("Данные должны содержать первый тестовый элемент",
                loadedItems.any { it.name == "Test Product 1" })
            assertTrue("Данные должны содержать второй тестовый элемент",
                loadedItems.any { it.name == "Test Product 2" })

            // Проверка файла БД
            val dbFile = ApplicationProvider.getApplicationContext<Context>()
                .getDatabasePath(TEST_DB_NAME)
            assertTrue("Файл БД должен существовать", dbFile.exists())
            Log.d("TestDB", "Database file size: ${dbFile.length()} bytes")
        }
    }

    @After
    fun tearDown() {
        database.close()
        ApplicationProvider.getApplicationContext<Context>()
            .deleteDatabase(TEST_DB_NAME)
    }
}