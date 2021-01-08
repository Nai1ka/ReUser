package ru.ndevelop.reuser.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager




object Utils {
     fun byteArrayToHexString(inarray: ByteArray?): String {
        var i: Int
        var `in`: Int
        val hex = arrayOf(
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "A",
            "B",
            "C",
            "D",
            "E",
            "F"
        )
        var out = ""
        var j = 0
        if(inarray!=null)
            while (j < inarray.size) {
                `in` = inarray[j].toInt() and 0xff
                i = `in` shr 4 and 0x0f
                out += hex[i]
                i = `in` and 0x0f
                out += hex[i]
                ++j
            }
        return out
    }
    fun getActionsFromString(actionsString: String):ArrayList<Action>{
        val result:ArrayList<Action> = arrayListOf()
        var tempActions = actionsString.split('~')
        tempActions = tempActions.subList(0, tempActions.size - 1)
        tempActions.forEach{
            val tempAction = it.split('-')
            if(tempAction.size==4) {
                val resultAction = Action(ActionTypes.valueOf(tempAction[1]))
                resultAction.status = tempAction[2] == "1"
                resultAction.specialData = tempAction[3]
                result.add(resultAction)

            }
        }
        return result
    }
    fun getActionsList():ArrayList<Action>{
        val resultArrayList:ArrayList<Action> = arrayListOf()
        for(i in ActionTypes.values()){
            resultArrayList.add(Action(i))
        }
        return resultArrayList
    }
     fun checkCameraPermission(context: Context): Boolean {
        val permission = Manifest.permission.CAMERA
        val res: Int = context.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

}