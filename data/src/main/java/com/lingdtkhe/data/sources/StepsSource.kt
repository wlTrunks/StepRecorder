package com.lingdtkhe.data.sources

import androidx.paging.PagingData
import com.lingdtkhe.domain.model.Step
import kotlinx.coroutines.flow.Flow

internal interface StepsSource {
    suspend fun saveStep(step: Int, time: Long)
    fun getStepsForPeriod(start: Long, end: Long): Flow<PagingData<Step>>
    fun observeStepsFromTime(start: Long): Flow<List<Step>>
}