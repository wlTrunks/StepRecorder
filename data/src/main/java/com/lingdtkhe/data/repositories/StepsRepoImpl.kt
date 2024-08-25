package com.lingdtkhe.data.repositories

import androidx.paging.PagingData
import com.lingdtkhe.data.sources.StepsSource
import com.lingdtkhe.domain.model.Step
import com.lingdtkhe.domain.repository.StepsRepo
import kotlinx.coroutines.flow.Flow

internal class StepsRepoImpl(
    private val stepsSource: StepsSource
) : StepsRepo {
    override fun startObserveSteps(time: Long): Flow<List<Step>> = stepsSource.observeStepsFromTime(time)

    override fun requestStepsForPeriod(start: Long, end: Long): Flow<PagingData<Step>> =
        stepsSource.getStepsForPeriod(start, end)
}