package com.lingdtkhe.domain.repository

import com.lingdtkhe.domain.model.Profile

interface AuthRepo {
    suspend fun googleAuth(): Result<Profile>
    suspend fun hasProfile(): Result<Profile>
    fun logout()
}