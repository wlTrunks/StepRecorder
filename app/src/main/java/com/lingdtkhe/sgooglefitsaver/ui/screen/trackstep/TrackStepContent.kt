package com.lingdtkhe.sgooglefitsaver.ui.screen.trackstep

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lingdtkhe.sgooglefitsaver.R
import com.lingdtkhe.sgooglefitsaver.ui.common.DEFAULT_PADDING
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonButton
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonStepItem
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonText
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackStepContent(state: TrackStepScreen.State, onAction: (TrackStepScreen.Action) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { CommonText(stringResource(R.string.text_step_track_record)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(TrackStepScreen.Action.GoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(TrackStepScreen.Action.OpenHistory) }) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.ic_calendar),
                            contentDescription = "To calendar"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
            ) {
                CommonText(text = stringResource(R.string.text_step_list))
                Spacer(modifier = Modifier.size(DEFAULT_PADDING))
                if (state.stepsStateFlow != null) {
                    val steps = state.stepsStateFlow.collectAsState()
                    val lazyListState = state.lazyListState
                    LaunchedEffect(key1 = steps.value) {
                        snapshotFlow { lazyListState.firstVisibleItemIndex }
                            .collectLatest { index ->
                                if (index <= 1 && lazyListState.isScrollInProgress.not()) {
                                    lazyListState.animateScrollToItem(0)
                                }
                            }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = DEFAULT_PADDING),
                        state = lazyListState,
                        reverseLayout = true
                    ) {
                        itemsIndexed(
                            items = steps.value,
                            key = { _, step -> step.step },
                        ) { _, step ->
                            CommonStepItem(step)
                            HorizontalDivider()
                        }
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .fillMaxWidth()
            ) {
                CommonButton(
                    modifier = Modifier
                        .padding(horizontal = DEFAULT_PADDING)
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    onAction = {
                        onAction(
                            if (state.isStartTrackRecording) {
                                TrackStepScreen.Action.StopRecord
                            } else {
                                TrackStepScreen.Action.StartRecord
                            }
                        )
                    }) {
                    val buttonText = stringResource(
                        if (state.isStartTrackRecording) {
                            R.string.text_stop_record
                        } else {
                            R.string.text_start_record
                        }
                    )
                    CommonText(buttonText)
                }
            }
        },
    )
}