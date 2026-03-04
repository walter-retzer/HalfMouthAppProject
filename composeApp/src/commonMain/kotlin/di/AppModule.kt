package di

import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.DigitalInputViewModel
import viewmodel.HomeViewModel
import viewmodel.LoginUserViewModel
import viewmodel.ProductionViewModel
import viewmodel.ProfileViewModel
import viewmodel.SetpointAdjustViewModel
import viewmodel.SignInViewModel
import viewmodel.TemperatureViewModel


val appModule = module {
    single<LoginUserViewModel> { LoginUserViewModel() }
    single<SignInViewModel> { SignInViewModel() }
    single<HomeViewModel> { HomeViewModel() }
    single<ProfileViewModel> { ProfileViewModel() }
    factory<ProductionViewModel> { ProductionViewModel(get()) }
    factory<ChartLineViewModel> { ChartLineViewModel(get()) }
    factory<SetpointAdjustViewModel> { SetpointAdjustViewModel(get()) }
    factory<TemperatureViewModel> { TemperatureViewModel(get()) }
    factory<DigitalInputViewModel> { DigitalInputViewModel(get()) }
}
