package com.example.productlist_ip.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productlist_ip.data.Item
import com.example.productlist_ip.data.ItemDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ItemViewModel(private val itemDao: ItemDao) : ViewModel() {

    // Состояние для списка товаров
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> get() = _items

    // Состояние для поискового запроса
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    init {
        // Загружаем все товары при инициализации
        loadAllItems()
    }

    // Загрузка всех товаров
    private fun loadAllItems() {
        viewModelScope.launch {
            itemDao.getAllItems()
                .onStart { Log.d("DB_FLOW", "Start loading from DB") }
                .catch { e -> Log.e("DB_FLOW", "Error loading", e) }
                .collect { items ->
                    Log.d("DB_FLOW", "Loaded ${items.size} items from DB")
                    _items.value = items
                }
            }
    }


    // Поиск товаров по названию
    fun searchItems(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query
            if (query.isBlank()) {
                loadAllItems()  // Если запрос пустой, показываем все товары
            } else {
                itemDao.searchItemsByName("%$query%").collect { items ->
                    _items.value = items
                }
            }
        }
    }

    // Обновление количества товара
    fun updateItemAmount(itemId: Int, newAmount: Int) {
        viewModelScope.launch {
            itemDao.updateItemAmount(itemId, newAmount)
        }
    }

    // Удаление товара
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }
}