package com.lingdtkhe.domain.service

/**
 * Состояние сервиса
 */
interface ServiceStatus {
    fun setServiceRun(isRunning: Boolean)
    fun isRunning(): Boolean
}