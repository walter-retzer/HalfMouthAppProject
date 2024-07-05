package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import viewmodel.HomeViewModel

actual val viewModelModule = module{
    singleOf(::HomeViewModel)
}
