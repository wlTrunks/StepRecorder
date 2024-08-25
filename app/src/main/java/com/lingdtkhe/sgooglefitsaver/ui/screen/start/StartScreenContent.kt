package com.lingdtkhe.sgooglefitsaver.ui.screen.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lingdtkhe.domain.model.Profile
import com.lingdtkhe.sgooglefitsaver.R
import com.lingdtkhe.sgooglefitsaver.ui.common.DEFAULT_PADDING
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonButton
import com.lingdtkhe.sgooglefitsaver.ui.common.compose.CommonText

@Composable
fun StartScreenContent(state: StartScreen.State, onAction: (StartScreen.Action) -> Unit) {
    when (state.state) {
        StartScreen.State.Start.Auth -> Auth(onAction)
        StartScreen.State.Start.Loading -> Loading()
        is StartScreen.State.Start.ShowProfile -> ShowProfile(state.state.profile, onAction)
    }
}

@Composable
private fun Auth(onAction: (StartScreen.Action) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)
    ) {
        CommonButton(
            onAction = { onAction(StartScreen.Action.OnAuthClick) },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = "Google icon",
                    tint = Color.Unspecified,
                )
                CommonText(stringResource(R.string.text_access_using_google))
            }
        }
    }
}

@Composable
private fun Loading() {
    val strokeWidthDp = 5.dp
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .drawBehind {
                    drawCircle(
                        Color.Red,
                        radius = size.width / 2 - strokeWidthDp.toPx() / 2,
                        style = Stroke(strokeWidthDp.toPx())
                    )
                },
            color = Color.LightGray,
            strokeWidth = strokeWidthDp
        )
    }
}

@Composable
private fun ShowProfile(profile: Profile, onAction: (StartScreen.Action) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(top = 120.dp)
                .align(Alignment.TopCenter)
        ) {
            CommonText(text = profile.userName)
            Spacer(Modifier.size(DEFAULT_PADDING))
            CommonText(text = stringResource(R.string.text_daily_steps, profile.dailySteps))
        }
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            CommonButton(
                onAction = { onAction(StartScreen.Action.OpenStepTrack) },
            ) {
                CommonText(stringResource(R.string.text_to_step_track))
            }
            Spacer(Modifier.size(DEFAULT_PADDING))
            CommonButton(
                onAction = { onAction(StartScreen.Action.Logout) },
            ) {
                CommonText(stringResource(R.string.text_logout))
            }
        }
    }
}