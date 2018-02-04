package tzengshinfu.orientationchanger

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings


class OrientationChangerSetting(context: Context) {
    private var appSettings: SharedPreferences
    private var application: Context = context

    init {
        appSettings = application.getSharedPreferences(context.packageName, 0)
    }

    fun getThreshold(): Int = appSettings.getInt(PreferenceName.Threshold.value, 1)


    fun setThreshold(value: Int) {
        val appSettingsEditor = appSettings.edit()
        appSettingsEditor.putInt(PreferenceName.Threshold.value, value)
        appSettingsEditor.commit()
    }

    fun getEnabled(): Boolean = appSettings.getBoolean(PreferenceName.IsEnabled.value, false)

    fun setEnabled(value: Boolean) {
        val appSettingsEditor = appSettings.edit()
        appSettingsEditor.putBoolean(PreferenceName.IsEnabled.value, value)
        appSettingsEditor.commit()
    }

    fun getLock(): Boolean =
            if (Settings.System.getInt(application.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 0) true else false

    fun setLock(value: Boolean) {
        var value = if (value) 0 else 1
        android.provider.Settings.System.putInt(application.contentResolver, Settings.System.ACCELEROMETER_ROTATION, value)
    }

    fun getChanged(): Boolean = appSettings.getBoolean(PreferenceName.IsChanged.value, false)

    fun setChanged(value: Boolean) {
        val appSettingsEditor = appSettings.edit()
        appSettingsEditor.putBoolean(PreferenceName.IsChanged.value, value)
        appSettingsEditor.commit()
    }

    fun setPrompting(value: Boolean) {
        val appSettingsEditor = appSettings.edit()
        appSettingsEditor.putBoolean(PreferenceName.IsPrompting.value, value)
        appSettingsEditor.commit()
    }

    fun getPrompting(): Boolean = appSettings.getBoolean(PreferenceName.IsPrompting.value, false)

    fun getCancelled(): Boolean = appSettings.getBoolean(PreferenceName.IsCancelled.value, false)

    fun setCancelled(value: Boolean) {
        val appSettingsEditor = appSettings.edit()
        appSettingsEditor.putBoolean(PreferenceName.IsCancelled.value, value)
        appSettingsEditor.commit()
    }

    fun startService() {
        var service = Intent(application, OrientationChangerService::class.java)
        application.startService(service)
    }

    fun isDown(orientation: Int): Boolean = if (orientation <= 45 || orientation >= 316) true else false
    fun isLeft(orientation: Int): Boolean = if (orientation in 46..135) true else false
    fun isRight(orientation: Int): Boolean = if (orientation in 226..315) true else false
    fun isUp(orientation: Int): Boolean = if (orientation in 136..225) true else false

}