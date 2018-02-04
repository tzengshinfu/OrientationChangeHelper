package tzengshinfu.orientationchanger

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.OrientationEventListener


class OrientationChangerService : Service() {
    lateinit var setting: OrientationChangerSetting
    lateinit var orientationSensor: OrientationEventListener
    var leftCount = 0
    var rightCount = 0

    override fun onCreate() {
        super.onCreate()

        setting = OrientationChangerSetting(this@OrientationChangerService)

        setOrientationListener()
    }

    fun setOrientationListener() {
        var threshold = setting.getThreshold()

        orientationSensor = object : OrientationEventListener(applicationContext, 10000 * threshold) {
            override fun onOrientationChanged(orientation: Int) {
                if (setting.getEnabled()) {
                    if (setting.getLock()) {
                        if (setting.isDown(orientation)) {
                            leftCount = 0
                            rightCount = 0

                            setting.setCancelled(false)

                            return
                        }

                        if (setting.isUp(orientation)) {
                            leftCount = 0
                            rightCount = 0

                            setting.setCancelled(false)

                            return
                        }

                        if (setting.isLeft(orientation)) {
                            leftCount++
                            rightCount = 0
                        }

                        if (setting.isRight(orientation)) {
                            leftCount = 0
                            rightCount++
                        }

                        if (leftCount >= threshold || rightCount >= threshold) {
                            if (setting.getPrompting() == false) {
                                if (setting.getCancelled() == false) {
                                    orientationSensor.disable()

                                    var promptActivity = Intent(this@OrientationChangerService, PromptActivity::class.java)
                                    promptActivity.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(promptActivity)
                                }
                            }
                        }
                    } else {
                        if (setting.getChanged()) {
                            if (setting.isDown(orientation)) {
                                setting.setLock(true)

                                setting.setChanged(false)
                            }

                            if (setting.isUp(orientation)) {
                                setting.setLock(true)

                                setting.setChanged(false)
                            }
                        }
                    }
                } else {
                    orientationSensor.disable()
                }
            }
        }

        if (orientationSensor.canDetectOrientation()) {
            orientationSensor.enable()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        orientationSensor.disable()

        setOrientationListener()

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? = null
}