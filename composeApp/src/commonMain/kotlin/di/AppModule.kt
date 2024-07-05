package di

import org.koin.dsl.module
import viewmodel.ChartLineViewModel
import viewmodel.HomeViewModel
import viewmodel.ProfileViewModel

val appModule = module {
    single<HomeViewModel> { HomeViewModel() }
    single<ChartLineViewModel> { ChartLineViewModel() }
    single<ProfileViewModel> { ProfileViewModel() }
}
