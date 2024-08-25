package com.lingdtkhe.domain.components

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Интерфейс для запроса активити контрактов
 */
interface IntentLauncher {
    fun launchIntent(intent: Intent, onResult: (ActivityResult) -> Unit = {})
    fun <I, O> launchWithResultContract(
        contract: ActivityResultContract<I, O>,
        input: I,
        callback: ActivityResultCallback<O>
    )
}

suspend fun <I, O> IntentLauncher.launchContract(
    contract: ActivityResultContract<I, O>,
    input: I
): O = suspendCoroutine { continuation ->
    launchWithResultContract(contract, input) {
        continuation.resume(it)
    }
}