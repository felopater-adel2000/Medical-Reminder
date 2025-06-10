package com.example.medicalreminder.domain.usecase

import com.example.medicalreminder.domain.model.Reminder
import com.example.medicalreminder.domain.repository.ReminderRepository
import javax.inject.Inject


class InsertUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) = reminderRepository.insert(reminder)

}