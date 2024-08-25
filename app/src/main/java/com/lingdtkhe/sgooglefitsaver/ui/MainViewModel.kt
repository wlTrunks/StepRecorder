package com.lingdtkhe.sgooglefitsaver.ui

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingdtkhe.core.ui.navigation.NavigationComponent
import com.lingdtkhe.core.ui.screen.ScreenComponent
import com.lingdtkhe.core.ui.screen.ScreenState
import com.lingdtkhe.domain.components.IntentLauncher
import com.lingdtkhe.domain.components.PermissionChecker
import com.lingdtkhe.domain.components.launchContract
import com.lingdtkhe.sgooglefitsaver.ui.screen.start.StartScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class MainViewModel(
    private val intentLauncher: IntentLauncher,
    private val navigationComponent: NavigationComponent,
    private val permissionChecker: PermissionChecker
) : ViewModel() {

    val screen: Flow<ScreenComponent<ScreenState>> = navigationComponent.screen.filterIsInstance()

    init {
        requestPermission()
        navigationComponent.replaceAll(StartScreen())
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            permissionChecker.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION).not()
        ) {
            viewModelScope.launch {
                intentLauncher.launchContract(
                    ActivityResultContracts.RequestPermission(),
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
            }
        }
    }
}