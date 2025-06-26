package io.mohammedalaamorsi.nyt.di


import io.mohammedalaamorsi.nyt.data.repository.NewsRepositoryImp
import io.mohammedalaamorsi.nyt.domain.NewsRepository
import io.mohammedalaamorsi.nyt.domain.usecase.GetPopularNewsUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val domainModule = module {
    singleOf(::NewsRepositoryImp) { bind<NewsRepository>() }
    factoryOf(::GetPopularNewsUseCase)

}
