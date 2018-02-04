package tzengshinfu.orientationchanger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class BootCompletedReceiver : BroadcastReceiver() {
    lateinit var setting: OrientationChangerSetting

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                setting = OrientationChangerSetting(context)

                setting.startService()
            }
        }
    }
}