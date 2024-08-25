package com.lingdtkhe.domain.service

/**
 * Ручка для работы с сервисом
 */
interface ServiceComponent {
    suspend fun startService(): Boolean
    suspend fun stopService(): Boolean
}