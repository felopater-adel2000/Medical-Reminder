package com.example.medicalreminder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medicalreminder.domain.model.Reminder

@Database(entities = [Reminder::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun getReminderDao(): ReminderDao

    companion object {
        @Volatile
        private var _instance: ReminderDatabase? = null
        fun getInstance(context: Context): ReminderDatabase {
            if(_instance == null) synchronized(this) {
                _instance = Room.databaseBuilder(context, ReminderDatabase::class.java, "reminder_database")
                    .build()
            }
            return _instance!!
        }
    }
}