package com.example.medicalreminder.presentation

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.example.medicalreminder.domain.model.Reminder
import com.example.medicalreminder.domain.usecase.UpdateUseCase
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.Manifest.permission.POST_NOTIFICATIONS
import javax.inject.Inject
import com.example.medicalreminder.R
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import com.example.medicalreminder.BaseApplication


class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateUseCase: UpdateUseCase

    private lateinit var mediaPlayer: MediaPlayer

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_music)

        val reminderJson = intent?.getStringExtra(REMINDER)
        val reminder = Gson().fromJson(reminderJson, Reminder::class.java)

        val doneIntent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, reminderJson)
            action = DONE
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            context, reminder.timeInMillis.toInt(), doneIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val closeIntent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, reminderJson)
            action = REJECT
        }
        val closePendingIntent = PendingIntent.getBroadcast(
            context, reminder.timeInMillis.toInt(), closeIntent, PendingIntent.FLAG_IMMUTABLE
        )

        when (intent.action) {
            DONE -> {
                runBlocking { updateUseCase.invoke(reminder.copy(isTaken = true)) }
                cancelAlarm(context, reminder)
            }

            REJECT -> {
                runBlocking { updateUseCase.invoke(reminder.copy(isTaken = true)) }
                cancelAlarm(context, reminder)
            }

            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val notification = NotificationCompat.Builder(context, BaseApplication.CHANNEL)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Medication Reminder")
                            .setContentText(reminder.name.plus(" ${reminder.dosage}"))
                            .addAction(R.drawable.ic_check, "Done", donePendingIntent)
                            .addAction(R.drawable.ic_close, "Close", closePendingIntent)
                            .build()
                        NotificationManagerCompat.from(context)
                            .notify(1, notification)
                    }
                } else {
                    val notification = NotificationCompat.Builder(context, BaseApplication.CHANNEL)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Medication Reminder")
                        .setContentText(reminder.name.plus(" ${reminder.dosage}"))
                        .addAction(R.drawable.ic_check, "Done", donePendingIntent)
                        .addAction(R.drawable.ic_close, "Close", closePendingIntent)
                        .build()

                    NotificationManagerCompat.from(context)
                        .notify(1, notification)

                }
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.release()
                }
                mediaPlayer.start()
            }
        }
    }

    companion object {
        const val DONE = "DONE"
        const val REJECT = "REJECT"
    }

}