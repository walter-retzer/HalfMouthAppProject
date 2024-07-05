package di

import org.koin.dsl.module
import viewmodel.HomeViewModel

val appModule = module {
    single<HomeViewModel> { HomeViewModel() }
}
