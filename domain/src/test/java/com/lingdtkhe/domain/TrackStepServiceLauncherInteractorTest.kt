package com.lingdtkhe.domain

import android.Manifest
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.lingdtkhe.domain.components.IntentLauncher
import com.lingdtkhe.domain.components.PermissionChecker
import com.lingdtkhe.domain.interactors.TrackStepServiceLauncherInteractor
import com.lingdtkhe.domain.repository.StepsRepo
import com.lingdtkhe.domain.repository.UserPreferencesRepo
import com.lingdtkhe.domain.service.ServiceComponent
import com.lingdtkhe.domain.service.ServiceStatus
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

class TrackStepServiceLauncherInteractorTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var rule = CoroutineTestRule()

    private val intentLauncher: IntentLauncher = mock()
    private val permissionChecker: PermissionChecker = mock()
    private val serviceComponent: ServiceComponent = mock()
    private val stepsRepo: StepsRepo = mock()
    private val serviceStatus: ServiceStatus = mock()
    private val userPreferencesRepo: UserPreferencesRepo = mock()
    private val trackStepServiceLauncherInteractor = TrackStepServiceLauncherInteractor(
        intentLauncher,
        permissionChecker,
        serviceComponent,
        stepsRepo,
        serviceStatus,
        userPreferencesRepo
    )

    @Test
    fun start_record_no_Location_Notification_Permissions_failed() = runTest {
        `when`(permissionChecker.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(false)
        `when`(permissionChecker.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)).thenReturn(false)
        `when`(serviceComponent.startService()).thenReturn(false)
        `when`(
            intentLauncher.launchWithResultContract(
                ActivityResultContracts.RequestPermission(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) { false }
        ).thenAnswer {
            ActivityResultCallback<Boolean> { false }
        }
        `when`(
            intentLauncher.launchWithResultContract(
                ActivityResultContracts.RequestPermission(),
                Manifest.permission.POST_NOTIFICATIONS
            ) { false }
        ).thenAnswer {
            ActivityResultCallback<Boolean> { false }
        }
        val result = trackStepServiceLauncherInteractor.startTrack(0L)
        assert(result.isFailure)
    }
}