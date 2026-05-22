package com.opentrivia.app.di

import com.opentrivia.app.ui.screen.CatalogViewModel
import com.opentrivia.app.ui.screen.LauncherViewModel
import com.opentrivia.app.ui.screen.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::LauncherViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::CatalogViewModel)
}
