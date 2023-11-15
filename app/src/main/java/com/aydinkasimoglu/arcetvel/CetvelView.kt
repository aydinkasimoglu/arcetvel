package com.aydinkasimoglu.arcetvel

import android.opengl.GLSurfaceView
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aydinkasimoglu.arcetvel.common.helpers.SnackbarHelper
import com.aydinkasimoglu.arcetvel.common.helpers.TapHelper
import com.google.ar.core.Config

/** Contains UI elements for CetvelAR. */
class CetvelView(private val activity: MainActivity) : DefaultLifecycleObserver {
    val root: View = View.inflate(activity, R.layout.activity_main, null)
    val surfaceView: GLSurfaceView = root.findViewById(R.id.surface_view)

    private val session
        get() = activity.arCoreSessionHelper.session

    val snackbarHelper = SnackbarHelper()
    val tapHelper = TapHelper(activity).also { surfaceView.setOnTouchListener(it) }

    override fun onResume(owner: LifecycleOwner) {
        surfaceView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        surfaceView.onPause()
    }

    /**
     * Shows a pop-up dialog on the first tap in com.aydinkasimoglu.arcetvel.CetvelRenderer, determining whether the user wants
     * to enable depth-based occlusion. The result of this dialog can be retrieved with
     * DepthSettings.useDepthForOcclusion().
     */
    fun showOcclusionDialogIfNeeded() {
        val session = session ?: return
        val isDepthSupported = session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)
        if (!activity.depthSettings.shouldShowDepthEnableDialog() || !isDepthSupported) {
            return // Don't need to show dialog.
        }

        // Asks the user whether they want to use depth-based occlusion.
        AlertDialog.Builder(activity)
            .setTitle(R.string.options_title_with_depth)
            .setMessage(R.string.depth_use_explanation)
            .setPositiveButton(R.string.button_text_enable_depth) { _, _ ->
                activity.depthSettings.setUseDepthForOcclusion(true)
            }
            .setNegativeButton(R.string.button_text_disable_depth) { _, _ ->
                activity.depthSettings.setUseDepthForOcclusion(false)
            }
            .show()
    }
}
