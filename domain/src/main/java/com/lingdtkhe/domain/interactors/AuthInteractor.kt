package com.lingdtkhe.domain.interactors

import com.lingdtkhe.domain.model.Profile
import com.lingdtkhe.domain.repository.AuthRepo

class AuthInteractor(private val authRepo: AuthRepo) {

    suspend fun hasProfile(): Result<Profile> = authRepo.hasProfile()

    suspend fun authorize(): Result<Profile> = authRepo.googleAuth()

    fun logout() = authRepo.logout()
}