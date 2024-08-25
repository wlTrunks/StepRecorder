package com.lingdtkhe.sgooglefitsaver.ui.screen.trackstep

import com.lingdtkhe.core.ui.navigation.NavigationComponent
import com.lingdtkhe.core.ui.screen.BaseScreenModel
import com.lingdtkhe.core.ui.screen.ScreenEvent
import com.lingdtkhe.domain.interactors.TrackStepServiceLauncherInteractor
import com.lingdtkhe.sgooglefitsaver.ui.screen.mappers.StepMapper
import com.lingdtkhe.sgooglefitsaver.ui.screen.trackhistory.TrackHistoryScreen
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackStepScreenModel(
    private val trackStepServiceLauncherInteractor: TrackStepServiceLauncherInteractor,
    private val navigationComponent: NavigationComponent,
    private val stepMapper: StepMapper
) : BaseScreenModel<TrackStepScreen.State, TrackStepScreen.Action, ScreenEvent>(
    TrackStepScreen.State(isStartTrackRecording = trackStepServiceLauncherInteractor.isServiceRunning())
) {

    init {
        mainScope.launch {
            if (trackStepServiceLauncherInteractor.isServiceRunning()) {
                trackStepServiceLauncherInteractor.getLastRecordTime()?.let { time ->
                    startTrackRecord(time)
                }
            }
        }
    }

    private fun navBack() {
        navigationComponent.pop()
    }

    private fun stopTrackRecord() {
        mainScope.launch {
            val result = trackStepServiceLauncherInteractor.stopTrack()
            updateState {
                copy(
                    isStartTrackRecording = result,
                    stepsStateFlow = null
                )
            }
        }
    }

    private fun startTrackRecord(time: Long) {
        mainScope.launch {
            trackStepServiceLauncherInteractor.startTrack(time)
                .onSuccess { result ->
                    val stepsStateFlow = result.map { stepMapper.mapToUi(it) }.stateIn(mainScope)
                    updateState {
                        copy(
                            isStartTrackRecording = true,
                            stepsStateFlow = stepsStateFlow
                        )
                    }
                }
        }
    }

    override fun onAction(action: TrackStepScreen.Action) {
        when (action) {
            TrackStepScreen.Action.StartRecord -> startTrackRecord(System.currentTimeMillis())
            TrackStepScreen.Action.StopRecord -> stopTrackRecord()
            TrackStepScreen.Action.GoBack -> navBack()
            TrackStepScreen.Action.OpenHistory -> navigationComponent.push(TrackHistoryScreen())
        }
    }
}
