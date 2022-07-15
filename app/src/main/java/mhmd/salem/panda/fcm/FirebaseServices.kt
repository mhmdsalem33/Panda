package mhmd.salem.panda.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mhmd.salem.panda.R
import mhmd.salem.panda.activites.MainActivity


const val channelId   = "notification_channel"
const val channelName = "mhmd.salem.panda.fcm"

class FirebaseServices :FirebaseMessagingService(){
    // 1 -> Generate Notification
    // 2 -> attach the Notification created with custom layout
    // 3 -> show to Notification


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null)
        {
            generateNotification(remoteMessage.notification!!.title!! , remoteMessage.notification!!.body!!)
        }
    }


    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String):RemoteViews{
        val remoteView =  RemoteViews("mhmd.salem.panda.fcm", R.layout.notification_row)
            remoteView.setTextViewText(R.id.notification_title    , title)
            remoteView.setTextViewText(R.id.notification_Message  , message)
            remoteView.setImageViewResource(R.id.img_notification , R.drawable.foodpanadalogo)
        return remoteView
    }

    fun generateNotification(title :String , message :String){
        val intent = Intent(this , MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)



        val pendingIntent = PendingIntent.getActivity(this , 0 , intent , PendingIntent.FLAG_ONE_SHOT)


        //Channel id &&  Chanel Name
        var builder :NotificationCompat.Builder = NotificationCompat.Builder(applicationContext , channelId)
            .setSmallIcon(R.drawable.foodpanadalogo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)


        builder = builder.setContent(getRemoteView(title , message))

        val notificationManager    = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O) // O Mean Orio version
        {
            val notificationChannel = NotificationChannel(channelId , channelName ,  NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0 , builder.build())


    }




}