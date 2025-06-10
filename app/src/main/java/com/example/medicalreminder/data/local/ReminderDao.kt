package com.example.medicalreminder.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.medicalreminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: Reminder)

    @Delete()
    suspend fun delete(reminder: Reminder)

    @Update()
    suspend fun update(reminder: Reminder)

    @Query("select * from Reminder order by timeInMillis desc")
    fun getAllReminders(): Flow<List<Reminder>>
}