package di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.HomeViewModel


actual val viewModelModule = module{
    viewModelOf(::HomeViewModel)
}
