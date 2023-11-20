package com.aydinkasimoglu.arcetvel

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class UnitSettingsFragment : DialogFragment() {
    companion object {
        const val SHARED_PREFERENCES_ID = "SHARED_PREFERENCES_UNIT_OPTIONS"
        const val SHARED_PREFERENCES_USE_METRIC = "use_metric"
        const val SHARED_PREFERENCES_USE_IMPERIAL = "use_imperial"

        var useMetric = true
            private set
        var useImperial = false
            private set
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        sharedPreferences = activity?.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE)!!
        useMetric = sharedPreferences.getBoolean(SHARED_PREFERENCES_USE_METRIC, true)
        useImperial = sharedPreferences.getBoolean(SHARED_PREFERENCES_USE_IMPERIAL, false)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val options = resources.getStringArray(R.array.unit_options_array)

            builder.setTitle(R.string.settings_title_with_units)
            builder.setSingleChoiceItems(
                options,
                if (useMetric) 0 else 1
            ) { dialog, which ->
                setUseMetric(which == 0)
                setUseImperial(which == 1)

                dialog.dismiss()
            }
            builder.setNegativeButton(R.string.cancel) {_, _ -> run {} }


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setUseMetric(enable: Boolean) {
        if (enable == useMetric) {
            return
        }

        useMetric = enable

        val editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFERENCES_USE_METRIC, useMetric)
        editor.apply()
    }

    private fun setUseImperial(enable: Boolean) {
        if (enable == useImperial) {
            return
        }

        useImperial = enable

        val editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFERENCES_USE_IMPERIAL, useImperial)
        editor.apply()
    }
}