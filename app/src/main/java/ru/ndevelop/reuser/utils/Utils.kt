package ru.ndevelop.reuser.utils

object Utils {
     fun ByteArrayToHexString(inarray: ByteArray?): String {
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
        var j: Int = 0
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
    fun getActionsFromString(actionsString:String):ArrayList<Action>{
        val result:ArrayList<Action> = arrayListOf()
        var tempActions = actionsString.split('~')
        tempActions = tempActions.subList(0,tempActions.size-1)
        tempActions.forEach{
            val tempAction = it.split('-')
            if(tempAction.size==4) {
                val resultAction = Action.valueOf(tempAction[1])
                resultAction.status = tempAction[2] == "1"
                resultAction.specialData = tempAction[3]
                result.add(resultAction)

            }
        }
        return result
    }

}