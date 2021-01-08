package ru.ndevelop.reuser.utils

import ru.ndevelop.reuser.R
import java.io.Serializable

/*open class Action {
    open val name:String = "New Tag"
    var status: Boolean = false
    open val isTwoStatuses:Boolean = false
    open var specialData = ""
    open val icon:Int = 0
    open val ordinal:Int = 0
}

class OpenCamera : Action() {
    override val name: String = "Открыть камеру"
    override val isTwoStatuses: Boolean = false
    override val icon: Int = R.drawable.ic_baseline_camera_alt_24
    override val ordinal:Int = 0
}

class Flashlight : Action() {
    override val name: String = "Включить выключить фонарик"
    override val isTwoStatuses: Boolean = true
   override  val icon: Int = R.drawable.ic_baseline_flash_on_24
     override val ordinal:Int = 1
}

class Sound : Action() {
    override val name: String = "Включить/выключить звук"
    override val isTwoStatuses: Boolean = true
    override val icon: Int = R.drawable.ic_baseline_volume_up_24
     override val ordinal:Int = 2
}

class Wifi : Action() {
    override val name: String = "Включить/выключить WI-FI"
    override val isTwoStatuses: Boolean = true
    override val icon: Int = R.drawable.ic_baseline_wifi_24
     override val ordinal:Int = 3
}

class Site : Action() {
    override val name: String = "Открыть сайт"
    override val isTwoStatuses: Boolean = false
    override val icon: Int = R.drawable.ic_baseline_open_in_browser_24
    override var specialData: String = ""
     override val ordinal:Int = 4
}

class Application : Action() {
    override val name: String = "Открыть приложение"
    override val isTwoStatuses: Boolean = false
    override val icon: Int = R.drawable.ic_baseline_smartphone_24
    override var specialData: String = ""
     override val ordinal:Int = 5
}*/
data class Action(val actionType:ActionTypes):Serializable{
    var status:Boolean = false
    var specialData:String = ""
}
enum class ActionTypes(val actionName:String,val isTwoStatuses:Boolean, val icon:Int ){ //status: true - включено false - выключено
    CAMERA("Открыть камеру",false, icon = R.drawable.ic_baseline_camera_alt_24),
    FLASHLIGHT("Включить выключить фонарик",true, icon = R.drawable.ic_baseline_flash_on_24),
    SOUND("Включить/выключить звук",true, icon = R.drawable.ic_baseline_volume_up_24),
    WIFI("Включить/выключить WI-FI",true, icon = R.drawable.ic_baseline_wifi_24),
    SITE("Открыть сайт",false,icon = R.drawable.ic_baseline_open_in_browser_24),
    APPLICATION("Открыть приложение",false,icon = R.drawable.ic_baseline_smartphone_24)
}

/*
enum class ActionClasses(val actionClass:Action){
    CAMERA(OpenCamera()),
    FLASHLIGHT(Flashlight()),
    SOUND(Sound()),
    WIFI(Wifi()),
    SITE(Site()),
    APPLICATION(Application()),
}
*/
