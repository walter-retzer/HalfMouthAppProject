package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.HomeViewModel
import viewmodel.ProfileViewModel

actual val viewModelModule = module{
    singleOf(::HomeViewModel)
    singleOf(::ChartLineViewModel)
    singleOf(::ProfileViewModel)
}
