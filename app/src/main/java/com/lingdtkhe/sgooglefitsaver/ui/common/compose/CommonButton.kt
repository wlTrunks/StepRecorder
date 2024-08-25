package com.lingdtkhe.sgooglefitsaver.ui.common.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    onAction: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = { onAction() },
        modifier = modifier
            .width(300.dp)
            .height(45.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(),
        elevation = ButtonDefaults.buttonElevation(6.dp),
        content = content
    )
}