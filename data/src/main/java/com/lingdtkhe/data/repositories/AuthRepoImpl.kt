package com.lingdtkhe.data.repositories

import android.app.Activity
import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.lingdtkhe.domain.components.IntentLauncher
import com.lingdtkhe.domain.components.launchContract
import com.lingdtkhe.domain.model.Profile
import com.lingdtkhe.domain.repository.AuthRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class AuthRepoImpl(
    private val context: Context,
    private val fitnessOptions: FitnessOptions,
    private val intentLauncher: IntentLauncher,
) : AuthRepo {
    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

    private fun getClient() = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder()
            .addExtension(fitnessOptions)
            .requestProfile()
            .build()
    )

    private suspend fun getProfile(): Profile {
        val name = getGoogleAccount().run { displayName ?: familyName } ?: throw Exception("Google Account empty")
        val steps = getDailySteps()
        return Profile(name, steps)
    }


    private suspend fun getDailySteps() = withContext(Dispatchers.IO) {
        runCatching {
            val result = Fitness.getHistoryClient(context, getGoogleAccount())
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .await()
            val total = when {
                result.dataPoints.isEmpty() -> 0
                else -> result.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
            }
            Timber.d("Total steps: $total")
            total
        }.getOrThrow()
    }

    override suspend fun hasProfile(): Result<Profile> = kotlin.runCatching {
        if (GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)) {
            getProfile()
        } else {
            throw Exception("Not Authorized")
        }
    }

    override fun logout() {
        getClient().signOut()
    }

    override suspend fun googleAuth(): Result<Profile> = kotlin.runCatching {
        val activityResult = intentLauncher.launchContract(
            ActivityResultContracts.StartActivityForResult(),
            getClient().signInIntent
        )
        if (activityResult.resultCode == Activity.RESULT_OK) {
            getProfile()
        } else {
            throw Exception("Google auth failed")
        }
    }
}
