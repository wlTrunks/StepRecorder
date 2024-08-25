package com.lingdtkhe.domain.interactors

import com.lingdtkhe.domain.repository.TrackStepSensorRepo
import com.lingdtkhe.domain.service.ServiceStatus

class TrackStepSensorServiceInteractor(
    private val serviceStatus: ServiceStatus,
    private val trackStepSensorRepo: TrackStepSensorRepo
) {
    fun startService() {
        serviceStatus.setServiceRun(true)
        trackStepSensorRepo.startRecord()
    }

    fun stopService() {
        serviceStatus.setServiceRun(false)
        trackStepSensorRepo.stopRecord()
    }
}