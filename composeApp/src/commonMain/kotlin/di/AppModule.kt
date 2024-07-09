package di

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
    single<DiscountsViewModel> { DiscountsViewModel(get()) }
    factory<ProductionViewModel> { ProductionViewModel() }
    factory<ChartLineViewModel> { ChartLineViewModel() }
}
