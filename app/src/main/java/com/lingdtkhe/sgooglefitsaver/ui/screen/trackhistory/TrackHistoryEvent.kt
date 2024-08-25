package com.lingdtkhe.sgooglefitsaver.ui.screen.trackhistory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.lingdtkhe.core.ui.screen.ScreenEvent
import com.lingdtkhe.sgooglefitsaver.R
import com.lingdtkhe.sgooglefitsaver.ui.common.DEFAULT_PADDING
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonButton
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonText

@Composable
fun TrackHistoryEvent(event: ScreenEvent) {
    val isVisible = remember { mutableStateOf(true) }
    when (event) {
        TrackHistoryScreen.Event.DateNotSelected -> isVisible.value = true
    }
    DateNotSelected(isVisible)
}

@Composable
fun DateNotSelected(isVisible: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = isVisible.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = DEFAULT_PADDING)
        ) {
            Popup(
                alignment = Alignment.Center
            ) {
                Column(
                    Modifier
                        .background(Color.Gray, RoundedCornerShape(12.dp))
                        .height(120.dp)
                        .width(300.dp)
                ) {
                    Spacer(modifier = Modifier.size(DEFAULT_PADDING))
                    CommonText(
                        text = stringResource(R.string.text_date_not_selected),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(DEFAULT_PADDING))
                    CommonButton(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(DEFAULT_PADDING),
                        onAction = { isVisible.value = false }) {
                        CommonText(stringResource(android.R.string.ok))
                    }
                }
            }
        }
    }
}