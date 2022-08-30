package developer.abdulaziz.alarm_and_clock.Broadcast

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import developer.abdulaziz.alarm_and_clock.Class.MyAlarm
import developer.abdulaziz.alarm_and_clock.Object.MyObject
import developer.abdulaziz.alarm_and_clock.R
import java.lang.Exception
import java.time.LocalTime

class MyAlarmReceiver : BroadcastReceiver() {
    @SuppressLint("LaunchActivityFromNotification")
    override fun onReceive(context: Context, intent: Intent) {
        val media = RingtoneManager.getRingtone(
            context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        )
        media.play()
        MyObject.init(context)
        val myList = MyObject.list
        myList.add(MyAlarm(LocalTime.now().toString().substring(0, 5), false))
        MyObject.list = myList
        val pending =
            PendingIntent.getBroadcast(
                context,
                1,
                Intent(context, MyAlarmReceiver2::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val builder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("Budilnikni toxtatishni hohlasangiz shuni ustiga bosing")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Budilnikni toxtatishni hohlasangiz shuni ustiga bosing"
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1, builder.build())
    }
}

class MyAlarmReceiver2 : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm was stopped", Toast.LENGTH_SHORT).show()
        throw Exception("Alarm was stopped !!!")
    }
}