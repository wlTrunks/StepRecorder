package com.lingdtkhe.sgooglefitsaver.ui.common.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lingdtkhe.sgooglefitsaver.R
import com.lingdtkhe.sgooglefitsaver.ui.common.DEFAULT_PADDING
import com.lingdtkhe.sgooglefitsaver.ui.screen.model.StepUi

@Composable
fun CommonStepItem(step: StepUi) {
    Column(
        Modifier
            .padding(vertical = DEFAULT_PADDING)
            .height(56.dp)
            .fillMaxWidth()
    ) {
        CommonText(text = stringResource(R.string.text_step_number, step.step))
        CommonText(text = stringResource(R.string.text_at, step.time))
    }
    HorizontalDivider()
}