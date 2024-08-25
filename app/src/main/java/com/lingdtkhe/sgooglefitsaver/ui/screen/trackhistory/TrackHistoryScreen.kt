package com.lingdtkhe.sgooglefitsaver.ui.screen.trackhistory

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import com.lingdtkhe.core.ui.screen.BaseScreenComponent
import com.lingdtkhe.core.ui.screen.ScreenAction
import com.lingdtkhe.core.ui.screen.ScreenEvent
import com.lingdtkhe.core.ui.screen.ScreenState
import com.lingdtkhe.sgooglefitsaver.ui.screen.model.StepUi
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject

class TrackHistoryScreen : BaseScreenComponent<TrackHistoryScreen.State,
    TrackHistoryScreen.Action,
    TrackHistoryScreen.Event,
    TrackHistoryModel>() {

    override val screenModel: TrackHistoryModel by inject()

    @Composable
    override fun renderScreen(state: State) {
        TrackHistoryContent(state) { action ->
            screenModel.onAction(action)
        }
    }

    @Composable
    override fun onEvent(event: ScreenEvent) {
        TrackHistoryEvent(event)
    }

    data class State(
        val date: String,
        val pagingItems: Flow<PagingData<StepUi>>? = null
    ) : ScreenState

    sealed interface Action : ScreenAction {
        data object GoBack : Action
        data class DateSelected(val time: Long) : Action
        data object CheckRecord : Action
    }

    sealed interface Event : ScreenEvent {
        object DateNotSelected : Event
    }
}