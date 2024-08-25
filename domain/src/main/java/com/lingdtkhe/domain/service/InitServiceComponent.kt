package com.lingdtkhe.domain.service

import android.app.Service

/**
 * Инициализация сервиса
 */
interface InitServiceComponent<S : Service> {
    fun setService(service: S)
    fun clear()
}