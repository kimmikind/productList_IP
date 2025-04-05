package com.example.productlist_ip.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productlist_ip.R
import com.example.productlist_ip.data.Item
import com.example.productlist_ip.ui.theme.Purple40
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun ItemCard(
    item: Item,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    var quantity by remember { mutableStateOf(item.amount) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Обработка тегов - убираем скобки и кавычки
    val processedTags = remember(item.tags) {
        item.tags
            .removeSurrounding("[", "]")  // Удаляем квадратные скобки
            .split(", ")                  // Разделяем по запятым
            .map { it.removeSurrounding("\"") } // Удаляем кавычки у каждого тега
            .filter { it.isNotBlank() }   // Фильтруем пустые теги
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок с кнопками (остаётся без изменений)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, "Редактировать", tint = Color(0xFF6200EE))
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Удалить", tint = Color(0xFFD32F2F))
                    }
                }
            }

            // Отображаем теги только если они есть
            if (processedTags.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    processedTags.forEach { tag ->
                        Surface(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Chip(
                                onClick = {},
                                colors = ChipDefaults.chipColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .wrapContentSize()
                                    .height(30.dp)
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color.DarkGray,
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Остальная часть карточки (количество и дата) без изменений
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "На складе",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.DarkGray
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Column {
                    Text(
                        text = "Дата добавления",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = formatTime(item.time),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.DarkGray
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }

    // Диалог редактирования количества товара
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },  // Закрыть диалог
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Редактирование",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(text = "Количество товара", modifier = Modifier.padding(top = 8.dp))
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        // Кнопка "-"
                        IconButton(
                            onClick = { quantity-- },
                            modifier = Modifier.size(48.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Purple40
                            )
                        ) {
                            Icon(
                                painterResource(R.drawable.baseline_remove_circle_outline_24),
                                contentDescription = "Уменьшить количество",
                                Modifier.size(30.dp)
                            )
                        }

                        // Текущее количество
                        Text(
                            text = "$quantity",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleLarge,

                        )

                        // Кнопка "+"
                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.size(48.dp),  // Размер кнопки
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Purple40

                            )
                        ) {
                            Icon(
                                painterResource(R.drawable.baseline_add_circle_outline_24),
                                contentDescription = "Увеличить количество",
                                Modifier.size(30.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onQuantityChange(quantity)  // Сохраняем изменения
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Purple40
                    )
                ) {
                    Text(text = "Принять")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showEditDialog = false },  // Закрываем диалог без изменений
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Purple40
                    )
                ) {
                    Text(text = "Отмена")
                }
            }
        )
    }

    // Диалог подтверждения удаления товара
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },  // Закрыть диалог
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Предупреждение",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(text = "Удаление товара", modifier = Modifier.padding(top = 8.dp))
                }
            },
            text = { Text(text = "Вы действительно хотите удалить выбранный товар?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()  // Удаляем товар
                        showDeleteDialog = false  // Закрываем диалог
                    }
                ) {
                    Text(text = "Да", color = Purple40)

                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }  // Закрываем диалог без удаления
                ) {
                    Text(text = "Нет", color = Purple40)
                }
            }
        )
    }
}



// метод для форматирования времени
fun formatTime(time: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return dateFormat.format(Date(time))
}