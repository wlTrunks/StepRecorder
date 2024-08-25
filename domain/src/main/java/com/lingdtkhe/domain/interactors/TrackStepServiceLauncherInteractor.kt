package com.lingdtkhe.domain.interactors

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import com.lingdtkhe.domain.components.IntentLauncher
import com.lingdtkhe.domain.components.PermissionChecker
import com.lingdtkhe.domain.components.launchContract
import com.lingdtkhe.domain.model.Step
import com.lingdtkhe.domain.repository.StepsRepo
import com.lingdtkhe.domain.repository.UserPreferencesRepo
import com.lingdtkhe.domain.service.ServiceComponent
import com.lingdtkhe.domain.service.ServiceStatus
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class TrackStepServiceLauncherInteractor(
    private val intentLauncher: IntentLauncher,
    private val permissionChecker: PermissionChecker,
    private val serviceComponent: ServiceComponent,
    private val stepsRepo: StepsRepo,
    private val serviceStatus: ServiceStatus,
    private val userPreferencesRepo: UserPreferencesRepo
) {

    suspend fun startTrack(time: Long): Result<Flow<List<Step>>> {
        val hasPermissionLocation = permissionChecker.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val vesrionMoreTIRAMISU = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        val hasPermissionNotification = if (vesrionMoreTIRAMISU) {
            permissionChecker.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true
        }
        val started = if (hasPermissionLocation && hasPermissionNotification) {
            if (isServiceRunning()) return Result.success(stepsRepo.startObserveSteps(time))
            serviceComponent.startService()
        } else {
            val isLocationGranted = if (hasPermissionLocation.not() &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
            ) {
                intentLauncher.launchContract(
                    ActivityResultContracts.RequestPermission(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                true
            }
            val isNotificationGranted = if (hasPermissionNotification.not() && vesrionMoreTIRAMISU) {
                intentLauncher.launchContract(
                    ActivityResultContracts.RequestPermission(),
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                true
            }
            Timber.d("isNotificationGranted $isNotificationGranted isLocationGranted $isLocationGranted")
            if (isNotificationGranted && isLocationGranted) {
                serviceComponent.startService()
            } else {
                false
            }
        }
        if (!started) return Result.failure(Exception("Can't start service"))
        userPreferencesRepo.saveStartRecordTime(time)
        return Result.success(stepsRepo.startObserveSteps(time))
    }

    suspend fun stopTrack() = serviceComponent.stopService()

    fun isServiceRunning(): Boolean = serviceStatus.isRunning()

    suspend fun getLastRecordTime(): Long? = userPreferencesRepo.getLastRecordTime()
}