package com.lingdtkhe.sgooglefitsaver

import com.lingdtkhe.core.ui.navigation.NavigationComponent
import com.lingdtkhe.domain.CoroutineTestRule
import com.lingdtkhe.domain.interactors.TrackStepServiceLauncherInteractor
import com.lingdtkhe.domain.model.Step
import com.lingdtkhe.sgooglefitsaver.ui.screen.mappers.StepMapper
import com.lingdtkhe.sgooglefitsaver.ui.screen.trackstep.TrackStepScreen
import com.lingdtkhe.sgooglefitsaver.ui.screen.trackstep.TrackStepScreenModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

internal class TrackStepScreenModelTest {
    @get:Rule
    var rule = CoroutineTestRule()

    private val trackStepServiceLauncherInteractor: TrackStepServiceLauncherInteractor = mock()
    private val navigationComponent: NavigationComponent = mock()
    private val stepMapper: StepMapper = StepMapper()
    private val screenModel by lazy {
        TrackStepScreenModel(
            trackStepServiceLauncherInteractor,
            navigationComponent,
            stepMapper
        )
    }

    @Test
    fun `start track record success`() = runTest {
        val time = System.currentTimeMillis()
        val stepsList = listOf(Step(1, time))
        val flowSteps = flowOf(stepsList)
        `when`(trackStepServiceLauncherInteractor.startTrack(any())).thenReturn(Result.success(flowSteps))
        screenModel.onAction(TrackStepScreen.Action.StartRecord)
        val state = screenModel.state.value
        verify(trackStepServiceLauncherInteractor, atLeastOnce()).startTrack(any())
        assert(state.isStartTrackRecording)
        assert(state.stepsStateFlow?.value == stepMapper.mapToUi(stepsList))
    }

    @Test
    fun `stop track record success`() = runTest {
        `when`(trackStepServiceLauncherInteractor.stopTrack()).thenReturn(false)
        screenModel.onAction(TrackStepScreen.Action.StopRecord)
        val state = screenModel.state.value
        verify(trackStepServiceLauncherInteractor, atLeastOnce()).stopTrack()
        assert(!state.isStartTrackRecording)
        assert(state.stepsStateFlow?.value == null)
    }
}