package com.lingdtkhe.data.sources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.lingdtkhe.data.db.StepEntity
import com.lingdtkhe.data.db.StepsDao
import com.lingdtkhe.data.db.toDomain
import com.lingdtkhe.domain.model.Step
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LocalDbStepsSource(
    private val stepsDao: StepsDao
) : StepsSource {
    override suspend fun saveStep(step: Int, time: Long) =
        stepsDao.putStep(
            StepEntity(
                createAt = time,
                step = step
            )
        )


    override fun getStepsForPeriod(start: Long, end: Long): Flow<PagingData<Step>> =
        Pager(
            PagingConfig(pageSize = 20),
            0
        ) { stepsDao.getStepsForTimePeriod(start, end) }
            .flow
            .map { data ->
                data.map { step -> step.toDomain() }
            }


    override fun observeStepsFromTime(start: Long): Flow<List<Step>> = stepsDao.observeStepsFromTime(start)
        .map { steps -> steps.map { it.toDomain() } }
}