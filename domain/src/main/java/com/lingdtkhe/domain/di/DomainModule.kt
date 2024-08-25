package com.lingdtkhe.domain.di

import com.lingdtkhe.domain.components.PermissionChecker
import com.lingdtkhe.domain.components.PermissionCheckerImpl
import com.lingdtkhe.domain.interactors.AuthInteractor
import com.lingdtkhe.domain.interactors.TrackStepSensorServiceInteractor
import com.lingdtkhe.domain.interactors.TrackStepServiceLauncherInteractor
import com.lingdtkhe.domain.usecases.GetHistoryForDateUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DomainModule = module {
    factoryOf(::AuthInteractor)
    factoryOf(::TrackStepServiceLauncherInteractor)
    factoryOf(::TrackStepSensorServiceInteractor)
    factoryOf(::GetHistoryForDateUseCase)

    singleOf(::PermissionCheckerImpl) bind PermissionChecker::class
}