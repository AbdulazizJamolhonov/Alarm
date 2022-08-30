package developer.abdulaziz.alarm_and_clock.Object

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import developer.abdulaziz.alarm_and_clock.Class.MyAlarm

object MyObject {
    private lateinit var sp: SharedPreferences
    fun init(c: Context) {
        sp = c.getSharedPreferences("my_shared", Context.MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var list: ArrayList<MyAlarm>
        get() = gsonStringToList(sp.getString("keyList", "[]")!!)
        set(value) = sp.edit {
            it.putString("keyList", listToGsonString(value))
        }

    private fun gsonStringToList(gsonString: String): ArrayList<MyAlarm> =
        Gson().fromJson(gsonString, object : TypeToken<ArrayList<MyAlarm>>() {}.type)

    private fun listToGsonString(list: ArrayList<MyAlarm>): String = Gson().toJson(list)
}