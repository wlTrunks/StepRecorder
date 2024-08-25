package com.lingdtkhe.sgooglefitsaver.ui.screen.trackhistory

import androidx.paging.cachedIn
import androidx.paging.map
import com.lingdtkhe.core.ui.navigation.NavigationComponent
import com.lingdtkhe.core.ui.screen.BaseScreenModel
import com.lingdtkhe.domain.usecases.GetHistoryForDateUseCase
import com.lingdtkhe.sgooglefitsaver.ui.screen.mappers.DateMapper
import com.lingdtkhe.sgooglefitsaver.ui.screen.mappers.StepMapper
import kotlinx.coroutines.flow.map

class TrackHistoryModel(
    private val navigationComponent: NavigationComponent,
    private val dateMapper: DateMapper,
    private val stepMapper: StepMapper,
    private val getHistoryForDateUseCase: GetHistoryForDateUseCase
) : BaseScreenModel<TrackHistoryScreen.State, TrackHistoryScreen.Action, TrackHistoryScreen.Event>(
    TrackHistoryScreen.State("")
) {
    private var selectedDate: Long? = null

    private fun selectedDate(time: Long?) {
        selectedDate = time
        updateState {
            copy(date = time?.run { dateMapper.map(time) } ?: "")
        }
    }

    private fun getRecordsForDate() {
        val date = selectedDate
        if (date == null) {
            sendEvent(TrackHistoryScreen.Event.DateNotSelected)
        } else {
            val pager = getHistoryForDateUseCase(date)
            val pagers = pager.map { page ->
                page.map { step -> with(stepMapper) { step.toUi() } }
            }.cachedIn(mainScope)
            updateState { copy(pagingItems = pagers) }
        }
    }

    override fun onAction(action: TrackHistoryScreen.Action) {
        when (action) {
            TrackHistoryScreen.Action.GoBack -> navigationComponent.pop()
            is TrackHistoryScreen.Action.DateSelected -> selectedDate(action.time)
            TrackHistoryScreen.Action.CheckRecord -> getRecordsForDate()
        }
    }
}