package com.capa1.switchcontrol.ui.permissions

import android.Manifest
import android.os.Build

object PermissionUtils {
    val permissions =
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
}