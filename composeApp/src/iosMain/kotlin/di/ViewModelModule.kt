package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.HomeViewModel
import viewmodel.LoginUserViewModel
import viewmodel.ProductionViewModel
import viewmodel.ProfileViewModel
import viewmodel.SignInViewModel

actual val viewModelModule = module{
    singleOf(::HomeViewModel)
    singleOf(::ChartLineViewModel)
    singleOf(::ProfileViewModel)
    singleOf(::LoginUserViewModel)
    singleOf(::SignInViewModel)
    singleOf(::ProductionViewModel)
}
