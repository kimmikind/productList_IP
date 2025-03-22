package com.example.productlist_ip.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // автогенерация ID
    val name: String,
    val time: Long,
    val tags: String,
    val amount: Int
)