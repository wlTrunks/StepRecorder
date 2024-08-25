package com.lingdtkhe.sgooglefitsaver.ui.screen.start

import com.lingdtkhe.core.ui.navigation.NavigationComponent
import com.lingdtkhe.core.ui.screen.BaseScreenModel
import com.lingdtkhe.core.ui.screen.ScreenEvent
import com.lingdtkhe.domain.interactors.AuthInteractor
import com.lingdtkhe.sgooglefitsaver.ui.screen.trackstep.TrackStepScreen
import kotlinx.coroutines.launch
import timber.log.Timber

class StartScreenModel(
    private val authInteractor: AuthInteractor,
    private val navigationComponent: NavigationComponent
) : BaseScreenModel<StartScreen.State, StartScreen.Action, ScreenEvent>(StartScreen.State(StartScreen.State.Start.Loading)) {

    init {
        mainScope.launch {
            val profile = authInteractor.hasProfile().getOrNull()
            Timber.d("googleAuth isAuthorized ${profile}")
            val uiState =
                if (profile != null) StartScreen.State.Start.ShowProfile(profile) else StartScreen.State.Start.Auth
            updateState { copy(state = uiState) }
        }
    }

    private fun openTrackStepScreen() {
        navigationComponent.push(TrackStepScreen())
    }

    private fun logout() {
        authInteractor.logout()
        updateState { copy(state = StartScreen.State.Start.Auth) }
    }

    private fun auth() {
        updateState { copy(state = StartScreen.State.Start.Loading) }
        mainScope.launch {
            authInteractor.authorize()
                .onFailure {
                    Timber.d("onFailure ${it.message}")
                    updateState { copy(state = StartScreen.State.Start.Auth) }
                }
                .onSuccess { profile ->
                    Timber.d("onSuccess auth $profile")
                    updateState { copy(state = StartScreen.State.Start.ShowProfile(profile)) }
                }
        }
    }

    override fun onAction(action: StartScreen.Action) {
        when (action) {
            StartScreen.Action.OnAuthClick -> auth()
            StartScreen.Action.Logout -> logout()
            StartScreen.Action.OpenStepTrack -> openTrackStepScreen()
        }
    }
}