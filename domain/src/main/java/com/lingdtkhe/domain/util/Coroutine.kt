package com.lingdtkhe.domain.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

fun CoroutineDispatcher.provideCoroutineScope(tag: String?, job: Job = SupervisorJob()): CoroutineScope {
    val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, tag)
    }
    return CoroutineScope(job + this + handler)
}