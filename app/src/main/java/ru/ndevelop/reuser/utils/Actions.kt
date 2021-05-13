package ru.ndevelop.reuser.utils


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import ru.ndevelop.reuser.R
import java.io.Serializable



data class Action(val actionType: ActionTypes) : Serializable {
    var status: Boolean = false
    var specialData: String = ""

}

interface Acting {
    fun performAction(context: Context, status: Boolean = false, specialData: String = "")
}

enum class ActionTypes(
    val actionName: String,
    val isTwoStatuses: Boolean,
    val icon: Int,
    var permissions: Array<String> = arrayOf()
) : Acting { //status: true - включено false - выключено
    CAMERA("Открыть камеру", false, icon = R.drawable.ic_baseline_camera_alt_24,permissions = arrayOf(Manifest.permission.CAMERA)) {

        override fun performAction(context: Context, status: Boolean, specialData: String) {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            context.startActivity(intent)
        }
    },
    FLASHLIGHT("Фонарик", true, icon = R.drawable.ic_baseline_flash_on_24,permissions = arrayOf(Manifest.permission.CAMERA)) {
        override fun performAction(context: Context, status: Boolean, specialData: String) {
            //TODO("Not yet implemented")
        }
    },
    SOUND("Звук", true, icon = R.drawable.ic_baseline_volume_up_24) {
        override fun performAction(context: Context, status: Boolean, specialData: String) {
           // TODO("Not yet implemented")
        }
    },
    WIFI("WI-FI", true, icon = R.drawable.ic_baseline_wifi_24,permissions = arrayOf(Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.ACCESS_WIFI_STATE)) {
        override fun performAction(context: Context, status: Boolean, specialData: String) {
            val wifiManager =
                context.applicationContext.getSystemService(
                    Context.WIFI_SERVICE
                ) as WifiManager
            wifiManager.isWifiEnabled = status
        }
    },
    BLUETOOTH("Bluetooth", true, icon = R.drawable.ic_baseline_bluetooth_24){
        override fun performAction(context: Context, status: Boolean, specialData: String) {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter.isEnabled) {
                mBluetoothAdapter.disable()
            }
        }
    },
    SITE("Открыть сайт", false, icon = R.drawable.ic_baseline_open_in_browser_24) {
        override fun performAction(context: Context, status: Boolean, specialData: String) {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(specialData))
            context.startActivity(browserIntent)
        }
    },
    APPLICATION("Открыть приложение", false, icon = R.drawable.ic_baseline_smartphone_24) {
        override fun performAction(context: Context, status: Boolean, specialData: String) {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(specialData)
            launchIntent?.let { intent -> context.startActivity(intent) }
        }
    },
    DELAY("Подождать", false, icon = R.drawable.ic_baseline_timer_24) {
        override fun performAction(context: Context, status: Boolean, specialData: String) {
            Thread.sleep(specialData.toLong() * 1000)
        }
    }

}
