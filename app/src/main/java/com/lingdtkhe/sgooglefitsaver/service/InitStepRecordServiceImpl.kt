package com.lingdtkhe.sgooglefitsaver.service

import com.lingdtkhe.domain.service.InitServiceComponent
import com.lingdtkhe.domain.service.ServiceProvider

class InitStepRecordServiceImpl : InitServiceComponent<TrackStepSensorService>,
    ServiceProvider<TrackStepSensorService> {
    private var _service: TrackStepSensorService? = null

    override fun setService(service: TrackStepSensorService) {
        _service = service
    }

    override fun clear() {
        _service = null
    }

    @Throws(Exception::class)
    override fun provideService(): TrackStepSensorService =
        _service ?: throw Exception("TrackStepSensorService not init")
}