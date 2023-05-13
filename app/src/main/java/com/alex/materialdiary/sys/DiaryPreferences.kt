package com.alex.materialdiary.sys

import android.content.Context

class DiaryPreferences(context: Context) {
    val context: Context
    var preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    companion object {
        val GUID = "uuid"
        val PDA = "pda"
        val PDA_GUID = "pda_guid"

        private var i: DiaryPreferences? = null

        fun getInstance(): DiaryPreferences {
            return i as DiaryPreferences
        }
    }
    init {
        this.context = context.applicationContext
        if (i == null) i = this
    }

    fun set(name: String, value: String){
        val editor = preferences.edit()
        editor.putString(name, value)
        editor.apply()
    }
    fun get(name: String): String {
        return preferences.getString(name, "")!!
    }
    fun contains(name: String): Boolean{
        return preferences.contains(name)
    }
}