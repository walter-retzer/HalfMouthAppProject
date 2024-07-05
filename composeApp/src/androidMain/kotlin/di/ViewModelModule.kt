package di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.HomeViewModel
import viewmodel.LoginUserViewModel
import viewmodel.ProfileViewModel
import viewmodel.SignInViewModel


actual val viewModelModule = module{
    viewModelOf(::HomeViewModel)
    viewModelOf(::ChartLineViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::LoginUserViewModel)
    viewModelOf(::SignInViewModel)
}
