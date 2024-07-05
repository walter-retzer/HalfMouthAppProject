package di

import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.HomeViewModel
import viewmodel.LoginUserViewModel
import viewmodel.ProfileViewModel
import viewmodel.SignInViewModel

val appModule = module {
    single<HomeViewModel> { HomeViewModel() }
    single<ChartLineViewModel> { ChartLineViewModel() }
    single<ProfileViewModel> { ProfileViewModel() }
    single<LoginUserViewModel> { LoginUserViewModel() }
    single<SignInViewModel> { SignInViewModel() }
}
