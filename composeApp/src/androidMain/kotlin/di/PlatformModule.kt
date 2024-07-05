package di

import network.ApiServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    factory { ClientHttp().build() }
    factory<ApiServiceImpl> { ApiServiceImpl() }
}