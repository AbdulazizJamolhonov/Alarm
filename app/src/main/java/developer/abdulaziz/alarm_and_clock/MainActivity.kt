package developer.abdulaziz.alarm_and_clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import developer.abdulaziz.alarm_and_clock.Broadcast.MyAlarmReceiver
import developer.abdulaziz.alarm_and_clock.Class.MyAlarm
import developer.abdulaziz.alarm_and_clock.Object.MyObject
import developer.abdulaziz.alarm_and_clock.databinding.ActivityMainBinding
import java.time.LocalTime
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendar: Calendar
    private lateinit var myList: ArrayList<MyAlarm>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            MyObject.init(this@MainActivity)
            myList = MyObject.list
            if (myList.isEmpty()) {
                myList.add(MyAlarm(LocalTime.now().toString().substring(0, 5), false))
                MyObject.list = myList
            }
            textTime.text = MyObject.list[MyObject.list.lastIndex].time
            textTime.setOnClickListener {
                val t = textTime.text.toString()
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(t.substring(0, t.indexOf(':')).toInt())
                    .setMinute(t.substring(t.indexOf(':') + 1, t.length).toInt())
                    .setTitleText("Select Alarm Time")
                    .setPositiveButtonText("Select")
                    .setNegativeButtonText("Cancel")
                    .build()
                picker.show(supportFragmentManager, "tag")
                picker.addOnPositiveButtonClickListener {

                    calendar = Calendar.getInstance()
                    calendar[Calendar.HOUR_OF_DAY] = picker.hour
                    calendar[Calendar.MINUTE] = picker.minute
                    calendar[Calendar.SECOND] = 0

                    textTime.text = "${picker.hour}:${picker.minute}"
                }
                picker.addOnNegativeButtonClickListener { picker.dismiss() }
            }
            isAlarm.isChecked = MyObject.list[MyObject.list.lastIndex].isPlay!!
            isAlarm.setOnCheckedChangeListener { _, b ->
                if (b) {
                    val pendingIntent = PendingIntent.getBroadcast(
                        this@MainActivity,
                        0,
                        Intent(this@MainActivity, MyAlarmReceiver::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(
                            calendar.timeInMillis,
                            pendingIntent
                        ), pendingIntent
                    )
                    myList.add(MyAlarm(LocalTime.now().toString().substring(0, 5), true))
                    MyObject.list = myList
                } else myList.add(MyAlarm(LocalTime.now().toString().substring(0, 5), false))
                MyObject.list = myList
            }
        }
    }
}