package com.lingdtkhe.domain.repository

interface UserPreferencesRepo {
    suspend fun saveStartRecordTime(time: Long)
    suspend fun getLastRecordTime(): Long?
}