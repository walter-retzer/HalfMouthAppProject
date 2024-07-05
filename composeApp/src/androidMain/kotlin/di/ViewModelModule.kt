package di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.HomeViewModel


actual val viewModelModule = module{
    viewModelOf(::HomeViewModel)
    viewModelOf(::ChartLineViewModel)
}
