package di

import network.ApiServiceImpl
import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.DiscountsViewModel
import viewmodel.HomeViewModel
import viewmodel.LoginUserViewModel
import viewmodel.ProductionViewModel
import viewmodel.ProfileViewModel
import viewmodel.SignInViewModel

val appModule = module {
    single<LoginUserViewModel> { LoginUserViewModel() }
    single<SignInViewModel> { SignInViewModel() }
    single<HomeViewModel> { HomeViewModel() }
    single<ProfileViewModel> { ProfileViewModel() }
    factory<ProductionViewModel> { ProductionViewModel(get()) }
    factory<ChartLineViewModel> { ChartLineViewModel(get()) }
    single<DiscountsViewModel> { DiscountsViewModel(get()) }

}
