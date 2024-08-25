package com.lingdtkhe.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StepEntity::class], version = 1)
internal abstract class SGoogleFitDatabase : RoomDatabase() {
    abstract fun stepsDao(): StepsDao
}