package com.example.productlist_ip.di


import com.example.productlist_ip.data.ItemsDb
import com.example.productlist_ip.viewmodel.ItemViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ItemsDb.getDatabase(androidContext()) }
    single { get<ItemsDb>().itemDao() }
    viewModel { ItemViewModel(get()) }
}