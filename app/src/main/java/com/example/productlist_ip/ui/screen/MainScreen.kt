package com.example.productlist_ip.ui.screen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.lightColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productlist_ip.ui.theme.Blue40
import com.example.productlist_ip.ui.theme.Grey40
import com.example.productlist_ip.ui.theme.ProductList_IPTheme
import com.example.productlist_ip.ui.theme.Purple40
import com.example.productlist_ip.ui.theme.Purple80
import com.example.productlist_ip.ui.theme.PurpleGrey80
import com.example.productlist_ip.viewmodel.ItemViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: ItemViewModel = koinViewModel()) {
    val items by viewModel.items.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Добавим отладочный вывод
    LaunchedEffect(items) {
        Log.d("MainScreen", "Current items count: ${items.size}")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text ="Список товаров", fontSize = 20.sp)
                    }
                },
                backgroundColor = Blue40,
                contentColor = Color.White,
                modifier = Modifier.padding(top = 30.dp)



            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // Поисковая строка
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { newQuery ->
                        viewModel.searchItems(newQuery)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Список товаров
                ItemsList(
                    items = items,
                    onQuantityChange = { item, newQuantity ->
                        viewModel.updateItemAmount(item.id, newQuantity)
                    },
                    onDeleteProduct = { item ->
                        viewModel.deleteItem(item)
                    }
                )
            }
        }
    )
}
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusState = remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Анимация прозрачности для placeholder
    val placeholderAlpha by animateFloatAsState(
        targetValue = if (!focusState.value) 1f else 0f,  // Прозрачность 1, если нет фокуса, иначе 0
        animationSpec = tween(durationMillis = 600)
    )
    var flag =0
    Box(modifier = modifier) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width =
                        when {
                            focusState.value -> 2.dp
                            else -> 1.dp
                             },
                    color = when {
                        focusState.value -> Purple40
                        else -> Color.Gray
                    },
                    shape = RoundedCornerShape(4.dp)
                )
                .onFocusChanged { focusState.value = it.isFocused }
                .focusRequester(focusRequester),
            placeholder = {
                if (!focusState.value && query.isEmpty()) {  // Показываем placeholder только если нет фокуса
                    Text(text ="Поиск товаров",
                        modifier = Modifier.alpha(placeholderAlpha))
                }
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onQueryChange("")
                            focusRequester.requestFocus()  // Возвращаем фокус на TextField
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Очистить"
                        )
                    }
                }
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusState.value = false
                },

            )
        )

        // Текст "Поиск товаров" на обводке
        if (focusState.value) {
            Box(
                modifier = Modifier
                    .offset(y = (-12).dp)  // Смещение текста вверх, чтобы он был на границе обводки
                    .padding(start = 16.dp)
                    .background(
                        color = Grey40,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 0.dp, vertical = 0.dp)  // Отступы для фона
            ) {
                Text(
                    text = "Поиск товаров",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    ProductList_IPTheme {
        MainScreen()
    }
}