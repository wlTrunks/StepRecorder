package com.lingdtkhe.sgooglefitsaver.ui.screen.start

import androidx.compose.runtime.Composable
import com.lingdtkhe.core.ui.screen.BaseScreenComponent
import com.lingdtkhe.core.ui.screen.ScreenAction
import com.lingdtkhe.core.ui.screen.ScreenEvent
import com.lingdtkhe.core.ui.screen.ScreenState
import com.lingdtkhe.domain.model.Profile
import org.koin.core.component.inject

class StartScreen : BaseScreenComponent<StartScreen.State, StartScreen.Action, ScreenEvent, StartScreenModel>() {

    override val screenModel: StartScreenModel by inject()

    @Composable
    override fun renderScreen(state: State) {
        StartScreenContent(state) { action ->
            screenModel.onAction(action)
        }
    }

    data class State(val state: Start) : ScreenState {
        sealed interface Start {
            data object Auth : Start
            data object Loading : Start
            data class ShowProfile(val profile: Profile) : Start
        }
    }

    sealed interface Action : ScreenAction {
        data object OnAuthClick : Action
        data object Logout : Action
        data object OpenStepTrack : Action
    }
}