package com.lingdtkhe.sgooglefitsaver.ui.screen.trackhistory

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.paging.compose.collectAsLazyPagingItems
import com.lingdtkhe.sgooglefitsaver.R
import com.lingdtkhe.sgooglefitsaver.ui.common.DEFAULT_PADDING
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonButton
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonStepItem
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonText

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackHistoryContent(state: TrackHistoryScreen.State, onAction: (TrackHistoryScreen.Action) -> Unit) {
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = state.date
    AnimatedVisibility(visible = showDatePicker.value) {
        Popup(
            onDismissRequest = { showDatePicker.value = false },
            alignment = Alignment.TopStart
        ) {
            DatePickerModalInput({ time ->
                if (time != null) onAction(TrackHistoryScreen.Action.DateSelected(time))
                datePickerState.selectedDateMillis = time
            }) { showDatePicker.value = false }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBar(
                    title = { CommonText(stringResource(R.string.text_history)) },
                    navigationIcon = {
                        IconButton(onClick = { onAction(TrackHistoryScreen.Action.GoBack) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.size(DEFAULT_PADDING))

            }
        },
        content = {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = DEFAULT_PADDING)
                    .padding(bottom = 100.dp, top = 100.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = DEFAULT_PADDING)
                        .fillMaxWidth()
                        .height(64.dp),
                    value = selectedDate,
                    onValueChange = { },
                    label = { CommonText(stringResource(R.string.text_select_date)) },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker.value = !showDatePicker.value }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                )
                val pagingFLow = state.pagingItems
                if (pagingFLow != null) {
                    val lazyPagingItems = pagingFLow.collectAsLazyPagingItems()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(count = lazyPagingItems.itemCount) { index ->
                            lazyPagingItems[index]?.let { step ->
                                CommonStepItem(step)
                                HorizontalDivider()
                            }
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
                        onAction(TrackHistoryScreen.Action.CheckRecord)
                    }) {
                    CommonText(stringResource(R.string.text_check_record))
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                CommonText(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                CommonText(stringResource(android.R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}