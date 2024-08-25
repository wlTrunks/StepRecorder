package com.lingdtkhe.sgooglefitsaver.di

import com.lingdtkhe.core.ui.activity.ActivityProvider
import com.lingdtkhe.core.ui.activity.InitComponentActivity
import com.lingdtkhe.core.ui.navigation.NavigationComponent
import com.lingdtkhe.core.ui.navigation.NavigationComponentImpl
import com.lingdtkhe.core.ui.navigation.NavigationComponentInitialize
import com.lingdtkhe.domain.components.IntentLauncher
import com.lingdtkhe.domain.service.InitServiceComponent
import com.lingdtkhe.domain.service.ServiceComponent
import com.lingdtkhe.domain.service.ServiceProvider
import com.lingdtkhe.domain.service.ServiceStatus
import com.lingdtkhe.sgooglefitsaver.service.InitStepRecordServiceImpl
import com.lingdtkhe.sgooglefitsaver.service.StepRecordServiceComponentImpl
import com.lingdtkhe.sgooglefitsaver.service.StepRecordServiceStatusImpl
import com.lingdtkhe.sgooglefitsaver.service.TrackStepSensorService
import com.lingdtkhe.sgooglefitsaver.ui.InitComponentActivityImpl
import com.lingdtkhe.sgooglefitsaver.ui.MainViewModel
import com.lingdtkhe.sgooglefitsaver.ui.common.IntentLauncherImpl
import com.lingdtkhe.sgooglefitsaver.ui.common.IntentLauncherInitializer
import com.lingdtkhe.sgooglefitsaver.ui.screen.mappers.DateMapper
import com.lingdtkhe.sgooglefitsaver.ui.screen.mappers.StepMapper
import com.lingdtkhe.sgooglefitsaver.ui.screen.start.StartScreenModel
import com.lingdtkhe.sgooglefitsaver.ui.screen.trackhistory.TrackHistoryModel
import com.lingdtkhe.sgooglefitsaver.ui.screen.trackstep.TrackStepScreenModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val UiModule = module {
    singleOf(::InitComponentActivityImpl) {
        bind<InitComponentActivity>()
        bind<ActivityProvider>()
        createdAtStart()
    }
    singleOf(::InitStepRecordServiceImpl) {
        bind<InitServiceComponent<TrackStepSensorService>>()
        bind<ServiceProvider<TrackStepSensorService>>()
    }
    singleOf(::IntentLauncherImpl) {
        bind<IntentLauncher>()
        bind<IntentLauncherInitializer>()
    }

    singleOf(::NavigationComponentImpl) {
        bind<NavigationComponentInitialize>()
        bind<NavigationComponent>()
    }

    viewModelOf(::MainViewModel)

    factoryOf(::StartScreenModel)
    factoryOf(::TrackStepScreenModel)
    factoryOf(::TrackHistoryModel)

    singleOf(::StepRecordServiceComponentImpl) bind ServiceComponent::class
    singleOf(::StepRecordServiceStatusImpl) bind ServiceStatus::class

    singleOf(::StepMapper)
    singleOf(::DateMapper)
}