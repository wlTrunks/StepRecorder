package com.lingdtkhe.sgooglefitsaver.ui.screen.mappers

import com.lingdtkhe.domain.model.Step
import com.lingdtkhe.sgooglefitsaver.ui.screen.model.StepUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StepMapper {
    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss MM/dd/yy", Locale.getDefault())

    fun mapToUi(steps: List<Step>): List<StepUi> = steps
        .map { step -> step.toUi() }

    fun Step.toUi(): StepUi = StepUi(
        step,
        simpleDateFormat.format(Date(time))
    )
}