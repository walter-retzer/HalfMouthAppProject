package di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.DiscountsViewModel
import viewmodel.HomeViewModel
import viewmodel.LoginUserViewModel
import viewmodel.ProductionViewModel
import viewmodel.ProfileViewModel
import viewmodel.SignInViewModel

actual val viewModelModule = module{
    singleOf(::LoginUserViewModel)
    singleOf(::SignInViewModel)
    singleOf(::HomeViewModel)
    singleOf(::ProfileViewModel)
    singleOf(::DiscountsViewModel)
    factoryOf(::ProductionViewModel)
    factoryOf(::ChartLineViewModel)
}
