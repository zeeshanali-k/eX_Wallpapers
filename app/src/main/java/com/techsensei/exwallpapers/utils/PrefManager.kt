package com.techsensei.exwallpapers.utils


import android.content.Context
import android.content.SharedPreferences
import com.techsensei.exwallpapers.utils.constants.PrefConstants


class PrefManager (context: Context?){

    // Shared preferences file name
    private val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    private val TAG = "PrefManager"
    var pref: SharedPreferences? = null

    init {
        pref = context!!.getSharedPreferences(PrefConstants.PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setBoolean(PREF_NAME: String?, `val`: Boolean?) {
        val editor = pref!!.edit()
        editor.putBoolean(PREF_NAME, `val`!!)
        editor.apply()
    }

    fun setString(PREF_NAME: String?, VAL: String?) {
        val editor = pref!!.edit()
        editor.putString(PREF_NAME, VAL)
        editor.apply()
    }

    fun setDouble(PREF_NAME: String?, VAL: Double) {
        val editor = pref!!.edit()
        editor.putString(PREF_NAME, VAL.toString())
        editor.apply()
    }

    fun getDouble(PREF_NAME: String?): Double {
        val value = pref!!.getString(PREF_NAME, null)
        return if (value == null || value.isEmpty()) 0.0 else value.toDouble()
    }

    fun setInt(PREF_NAME: String?, VAL: Int) {
        val editor = pref!!.edit()
        editor.putInt(PREF_NAME, VAL)
        editor.apply()
    }

    //    returns default as false
    fun getPrefBoolean(PREF_NAME: String?): Boolean {
        return pref!!.getBoolean(PREF_NAME, false)
    }

}