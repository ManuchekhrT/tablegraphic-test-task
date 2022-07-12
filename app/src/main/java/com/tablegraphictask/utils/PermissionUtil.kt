package com.tablegraphictask.utils

import android.app.Activity
import android.view.View
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.tablegraphictask.R

object PermissionUtil {

    fun checkPermissions(
        activity: Activity,
        permission: String,
        listener: MultiplePermissionsListener?
    ) {
        checkPermissions(activity, listOf(permission), listener)
    }

    fun checkPermissions(
        activity: Activity,
        permissions: List<String>?,
        listener: MultiplePermissionsListener?
    ) {
        val contentView = activity.findViewById<View>(android.R.id.content)
        Dexter.withContext(activity)
            .withPermissions(permissions)
            .withListener(
                CompositeMultiplePermissionsListener(
                    listener,
                    SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(
                        contentView,
                        R.string.all_permissions_alert_error_message
                    )
                        .withOpenSettingsButton(R.string.action_settings)
                        .build()
                )
            )
            .check()
    }
}