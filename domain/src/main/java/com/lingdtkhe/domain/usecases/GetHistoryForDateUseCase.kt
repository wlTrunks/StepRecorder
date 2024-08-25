package com.lingdtkhe.domain.usecases

import com.lingdtkhe.domain.repository.StepsRepo
import java.util.concurrent.TimeUnit

/**
 * Получения данных по шагам за выбранный день из БД
 */
class GetHistoryForDateUseCase(
    private val stepsRepo: StepsRepo
) {
    operator fun invoke(date: Long) =
        stepsRepo.requestStepsForPeriod(date - TimeUnit.DAYS.toMillis(1), date)
}