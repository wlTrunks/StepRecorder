package com.lingdtkhe.sgooglefitsaver.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.lingdtkhe.core.ui.activity.InitComponentActivity
import com.lingdtkhe.sgooglefitsaver.ui.theme.GoogleFitTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val initComponentActivity: InitComponentActivity by inject()
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponentActivity.setActivity(this)
        enableEdgeToEdge()
        lifecycleScope.launch {
            var collectScreenStateJob: Job? = null
            viewModel.screen.collectLatest { screen ->
                collectScreenStateJob?.cancel()
                collectScreenStateJob = launch {
                    screen.screenState().collectLatest { state ->
                        setContent {
                            GoogleFitTheme {
                                Scaffold(
                                    modifier = Modifier.fillMaxSize()
                                ) { innerPadding ->
                                    val events = screen.screenEvent().collectAsState(initial = null)
                                    events.value?.let { pair ->
                                        screen.onEvent(pair.second)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    ) {
                                        screen.renderScreen(state)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        initComponentActivity.clear()
    }
}