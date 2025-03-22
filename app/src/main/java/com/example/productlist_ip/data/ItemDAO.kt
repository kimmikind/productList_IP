package com.example.productlist_ip.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    // Просмотр списка всех товаров
    @Query("SELECT * FROM item")
    fun getAllItems(): Flow<List<Item>>

    // Поиск товаров по названию
    @Query("SELECT * FROM item WHERE name LIKE :searchQuery")
    fun searchItemsByName(searchQuery: String): Flow<List<Item>>

    // Получение товара по ID (для отображения всех полей в карточке)
    @Query("SELECT * FROM item WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): Item?

    // Редактирование количества товара
    @Query("UPDATE item SET amount = :newAmount WHERE id = :itemId")
    suspend fun updateItemAmount(itemId: Int, newAmount: Int)

    // Удаление товара по ID
    @Delete
    suspend fun deleteItem(item: Item)

    // Добавление начальных значений
    @Insert
    suspend fun insertAll(vararg items: Item)


}