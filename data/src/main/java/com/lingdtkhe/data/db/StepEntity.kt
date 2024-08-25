package com.lingdtkhe.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lingdtkhe.domain.model.Step

@Entity(tableName = "table_steps")
internal data class StepEntity(
    @PrimaryKey
    @ColumnInfo("create_at") val createAt: Long,
    @ColumnInfo("step") val step: Int,
)

internal fun StepEntity.toDomain() = Step(step, createAt)