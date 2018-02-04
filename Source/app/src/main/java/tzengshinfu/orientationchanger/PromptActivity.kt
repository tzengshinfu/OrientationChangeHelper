package tzengshinfu.orientationchanger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_prompt.*


class PromptActivity : AppCompatActivity() {
    lateinit var setting: OrientationChangerSetting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prompt)

        setting = OrientationChangerSetting(this@PromptActivity)

        button_OK.setOnClickListener {
            setting.setLock(false)

            setting.setChanged(true)

            moveTaskToBack(true)
        }

        button_Cancel.setOnClickListener {
            setting.setCancelled(true)

            moveTaskToBack(true)
        }
    }

    override fun onResume() {
        super.onResume()

        setting.setPrompting(true)
    }

    override fun onPause() {
        super.onPause()

        setting.setPrompting(false)

        setting.startService()
    }
}