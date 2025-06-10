package com.example.medicalreminder.domain.usecase

import com.example.medicalreminder.domain.repository.ReminderRepository
import javax.inject.Inject


class GetAllReminderUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    operator fun invoke() = reminderRepository.getAllReminders()

}