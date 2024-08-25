package com.lingdtkhe.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface StepsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putStep(step: StepEntity)

    @Query("SELECT * FROM table_steps WHERE create_at BETWEEN :start AND :end ORDER BY create_at DESC")
    fun getStepsForTimePeriod(start: Long, end: Long): PagingSource<Int, StepEntity>

    @Query("SELECT * FROM table_steps WHERE create_at > :start ORDER BY create_at DESC")
    fun observeStepsFromTime(start: Long): Flow<List<StepEntity>>
}