package di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.DiscountsViewModel
import viewmodel.HomeViewModel
import viewmodel.LoginUserViewModel
import viewmodel.ProductionViewModel
import viewmodel.ProfileViewModel
import viewmodel.SignInViewModel


actual val viewModelModule = module{
    viewModelOf(::LoginUserViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::DiscountsViewModel)
    viewModelOf(::ProductionViewModel)
    viewModelOf(::ChartLineViewModel)
}
