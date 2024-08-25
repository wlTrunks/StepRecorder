package com.lingdtkhe.data.di

import androidx.room.Room
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.lingdtkhe.data.db.SGoogleFitDatabase
import com.lingdtkhe.data.repositories.AuthRepoImpl
import com.lingdtkhe.data.repositories.StepsRepoImpl
import com.lingdtkhe.data.repositories.TrackStepSensorRepoImpl
import com.lingdtkhe.data.repositories.UserPreferencesRepoImpl
import com.lingdtkhe.data.sources.LocalDbStepsSource
import com.lingdtkhe.data.sources.StepsSource
import com.lingdtkhe.domain.repository.AuthRepo
import com.lingdtkhe.domain.repository.StepsRepo
import com.lingdtkhe.domain.repository.TrackStepSensorRepo
import com.lingdtkhe.domain.repository.UserPreferencesRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DataModule = module {
    single {
        FitnessOptions.builder()
            .addDataType(DataType.TYPE_LOCATION_SAMPLE)
            .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .build()
    }
    singleOf(::AuthRepoImpl) bind AuthRepo::class
    singleOf(::TrackStepSensorRepoImpl) bind TrackStepSensorRepo::class
    singleOf(::StepsRepoImpl) bind StepsRepo::class

    single { Room.databaseBuilder(get(), SGoogleFitDatabase::class.java, "db-steps").build() }
    single { get<SGoogleFitDatabase>().stepsDao() }
    singleOf(::LocalDbStepsSource) bind StepsSource::class
    singleOf(::UserPreferencesRepoImpl) bind UserPreferencesRepo::class
}