package com.lingdtkhe.sgooglefitsaver.ui.screen.trackstep

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import com.lingdtkhe.core.ui.screen.BaseScreenComponent
import com.lingdtkhe.core.ui.screen.ScreenAction
import com.lingdtkhe.core.ui.screen.ScreenEvent
import com.lingdtkhe.core.ui.screen.ScreenState
import com.lingdtkhe.sgooglefitsaver.ui.screen.model.StepUi
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject

class TrackStepScreen :
    BaseScreenComponent<TrackStepScreen.State, TrackStepScreen.Action, ScreenEvent, TrackStepScreenModel>() {

    override val screenModel: TrackStepScreenModel by inject()

    @Composable
    override fun renderScreen(state: State) {
        TrackStepContent(state) { action ->
            screenModel.onAction(action)
        }
    }

    data class State(
        val stepsStateFlow: StateFlow<List<StepUi>>? = null,
        val isStartTrackRecording: Boolean = false,
        val lazyListState: LazyListState = LazyListState()
    ) : ScreenState

    sealed interface Action : ScreenAction {
        data object StartRecord : Action
        data object StopRecord : Action
        data object GoBack : Action
        data object OpenHistory : Action
    }
}