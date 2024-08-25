package com.lingdtkhe.domain.service

import android.app.Service

interface ServiceProvider<S : Service> {
    fun provideService(): S
}