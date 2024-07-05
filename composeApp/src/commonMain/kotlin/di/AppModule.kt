package di

import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.HomeViewModel

val appModule = module {
    single<HomeViewModel> { HomeViewModel() }
    single<ChartLineViewModel> { ChartLineViewModel() }
}
