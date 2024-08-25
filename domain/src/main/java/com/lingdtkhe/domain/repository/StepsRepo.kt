package com.lingdtkhe.domain.repository

import androidx.paging.PagingData
import com.lingdtkhe.domain.model.Step
import kotlinx.coroutines.flow.Flow

interface StepsRepo {
    fun startObserveSteps(time: Long): Flow<List<Step>>
    fun requestStepsForPeriod(start: Long, end: Long): Flow<PagingData<Step>>
}