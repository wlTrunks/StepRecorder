package com.lingdtkhe.sgooglefitsaver.ui.common

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.lingdtkhe.core.ui.activity.InitComponentActivity
import com.lingdtkhe.domain.components.IntentLauncher
import java.util.*

interface IntentLauncherInitializer : InitComponentActivity

/**
 * Реализаци, регистрирует активити лаунчеры
 */
class IntentLauncherImpl : IntentLauncherInitializer, IntentLauncher {

    private var _activity: ComponentActivity? = null
    private val launchers = mutableMapOf<String, ActivityResultLauncher<*>>()

    private fun afterLaunch(key: String) {
        launchers[key]?.unregister()
        launchers.remove(key)
    }

    override fun setActivity(activity: ComponentActivity) {
        _activity = activity
        activity.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_DESTROY -> clear()
                        else -> {
                            /*no op */
                        }
                    }
                }
            }
        )
    }

    override fun clear() {
        launchers.values.forEach { launcher ->
            launcher.unregister()
        }
        launchers.clear()
        _activity = null
    }

    override fun launchIntent(intent: Intent, onResult: (ActivityResult) -> Unit) {
        launchWithResultContract(ActivityResultContracts.StartActivityForResult(), intent) {
            onResult(it)
        }
    }

    override fun <I, O> launchWithResultContract(
        contract: ActivityResultContract<I, O>,
        input: I,
        callback: ActivityResultCallback<O>
    ) {
        val key = UUID.randomUUID().toString()

        val wrappedCallback = ActivityResultCallback<O> {
            afterLaunch(key)
            callback.onActivityResult(it)
        }

        val launcher = _activity
            ?.activityResultRegistry
            ?.register(key, contract, wrappedCallback)

        if (launcher != null) {
            launchers[key] = launcher
            try {
                launcher.launch(input, null)
            } catch (e: ActivityNotFoundException) {
                afterLaunch(key)
            }
        }
    }
}