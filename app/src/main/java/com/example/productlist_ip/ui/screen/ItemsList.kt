package com.example.productlist_ip.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.productlist_ip.data.Item

@Composable
fun ItemsList(
    items: List<Item> = emptyList(),
    onItemsClick: (Item) -> Unit = {},
    onQuantityChange: (Item, Int) -> Unit = { _, _ -> },
    onDeleteProduct: (Item) -> Unit = {}
) {
    LazyColumn(modifier = Modifier) {
        items(
            items = items,
            key = { item -> item.id }
        ) { item ->
            ItemCard(
                item = item,
                onQuantityChange = { newQuantity ->
                    onQuantityChange(item, newQuantity)
                },
                onDelete = { onDeleteProduct(item) }
            )
        }
    }

}