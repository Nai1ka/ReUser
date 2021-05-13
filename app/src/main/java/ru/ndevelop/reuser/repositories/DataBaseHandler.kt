package ru.ndevelop.reuser.repositories

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import ru.ndevelop.reuser.App
import ru.ndevelop.reuser.objects.Tag
import ru.ndevelop.reuser.utils.Action
import ru.ndevelop.reuser.utils.Utils
import ru.ndevelop.reuser.utils.toInt


const val DATABASENAME = "REUSER Database"
const val TAGS_TABLENAME = "TAGS"
const val COL_ID = "ID"
const val COL_NAME = "NAME"
const val COL_ACTION = "TAG_ACTION"

object DataBaseHandler : SQLiteOpenHelper(
    App.applicationContext(), DATABASENAME, null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TAGS_TABLENAME ($COL_ID TEXT, $COL_NAME TEXT, $COL_ACTION TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }


    fun updateIfExistsElseInsert(tagId: String,tagName:String, actions: ArrayList<Action>) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        if (tagId == "") return
        contentValues.put(COL_ID, tagId)

        var actionsString = ""
        actions.forEachIndexed { index, action ->
            actionsString += "${index}-${action.actionType.name}-${action.status.toInt()}-${action.specialData}~"

        }
        contentValues.put(COL_NAME, tagName)
        contentValues.put(COL_ACTION, actionsString)
        val rows = db.update(TAGS_TABLENAME, contentValues, "$COL_ID = ?", arrayOf(tagId))
        if (rows == 0) {
            contentValues.put(COL_ID, tagId)
            db.insert(TAGS_TABLENAME, null, contentValues)
        }
    }

    fun isTagAlreadyExist(tagId: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, tagId)
        val rows = db.update(TAGS_TABLENAME, contentValues, "$COL_ID = ?", arrayOf(tagId))
        return rows > 0
    }


    fun getTagActions(tag: String): ArrayList<Action> {
        val db = this.readableDatabase
        var resultActions = arrayListOf<Action>()
        val query = "SELECT * FROM $TAGS_TABLENAME WHERE $COL_ID=?"
        val tempResult = db.rawQuery(query, arrayOf(tag))
        if (tempResult.count > 0) {
            tempResult.moveToFirst()
            val tempActions = tempResult.getString(tempResult.getColumnIndex(COL_ACTION))
            resultActions = Utils.getActionsFromString(tempActions)

        }
        tempResult.close()
        return resultActions
    }

    fun getTagsList(): ArrayList<Tag> {
        val db = this.readableDatabase
        val result: ArrayList<Tag> = arrayListOf()
        try {
            val query = "Select * from $TAGS_TABLENAME"
            val tempResult = db.rawQuery(query, null)
            if (tempResult.moveToFirst()) {
                do {
                    val tempTag = Tag(
                        tempResult.getString(tempResult.getColumnIndex(COL_NAME)),
                        tempResult.getString(
                            tempResult.getColumnIndex(
                                COL_ID
                            )
                        )
                    )
                    tempTag.actions = Utils.getActionsFromString(
                        tempResult.getString(
                            tempResult.getColumnIndex(
                                COL_ACTION
                            )
                        )
                    )
                    result.add(tempTag)
                } while (tempResult.moveToNext())
            }
            tempResult.close()

        } catch (e: SQLiteException) {

        }
        return result
    }

    fun deleteData(tagId: String) {
        val database = this.writableDatabase
        database.delete(TAGS_TABLENAME, "$COL_ID = ?", arrayOf(tagId))

    }

    fun updateTagName(tagId: String, newName: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, newName) //These Fields should be your String values of actual column names
        db.update(TAGS_TABLENAME, cv, "$COL_ID = ?", arrayOf(tagId))
    }
    fun getTagName(tagId: String):String{
        val db = this.readableDatabase
        var result = ""
        try {
            val query = "SELECT * FROM $TAGS_TABLENAME WHERE $COL_ID=?"
            val tempResult = db.rawQuery(query, arrayOf(tagId))
            if (tempResult.moveToFirst()) {
                val mTempResult = tempResult.getString(
                    tempResult.getColumnIndex(
                        COL_NAME
                    ))
               if(mTempResult!="New Tag") result = mTempResult

            }
            tempResult.close()

        } catch (e: SQLiteException) {

        }
        return result
    }
}