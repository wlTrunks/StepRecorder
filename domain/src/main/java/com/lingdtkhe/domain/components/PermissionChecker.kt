package com.lingdtkhe.domain.components

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * Поверка пермишенов
 */
interface PermissionChecker {
    fun checkSelfPermission(permission: String): Boolean
}

class PermissionCheckerImpl(
    private val context: Context,
) : PermissionChecker {

    override fun checkSelfPermission(permission: String): Boolean =
        PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context, permission)
}