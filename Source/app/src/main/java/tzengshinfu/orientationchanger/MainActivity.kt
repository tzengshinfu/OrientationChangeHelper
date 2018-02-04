package tzengshinfu.orientationchanger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {
    private val writeSettingsPermissionRequestCode = 1234
    lateinit var setting: OrientationChangerSetting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestWriteSettingPermission(R.string.claimPermission)

        setting = OrientationChangerSetting(this@MainActivity)

        setting.startService()

        numberPicker_OrientationChangeThreshold.minValue = 1
        numberPicker_OrientationChangeThreshold.maxValue = 10
        numberPicker_OrientationChangeThreshold.value = setting.getThreshold()
        checkBox_Enable.isChecked = setting.getEnabled()

        button_SaveSetting.setOnClickListener {
            setting.setEnabled(checkBox_Enable.isChecked)
            setting.setThreshold(numberPicker_OrientationChangeThreshold.value)

            toast(R.string.saveOK)

            finish()
        }
    }

    private fun requestWriteSettingPermission(claimPermissionTextId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this@MainActivity) == false) {
                toast(getString(claimPermissionTextId))

                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + this@MainActivity.packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivityForResult(intent, writeSettingsPermissionRequestCode)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == writeSettingsPermissionRequestCode) {
            requestWriteSettingPermission(R.string.claimPermissionAgain)
        }
    }

    override fun onResume() {
        super.onResume()

        setting.setPrompting(false)
    }
}