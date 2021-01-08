/*
 * Created by Na1ka on 08.05.20 21:46
 *
 */

package ru.ndevelop.reuser

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication


class App: MultiDexApplication(){
    companion object{

        private var instance:App? = null
        fun applicationContext() : Context{
            return instance!!.applicationContext
        }
    }
    init {
        instance=this
        MultiDex.install(this)
    }



}