package com.example.medicalreminder.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalreminder.domain.model.Reminder
import com.example.medicalreminder.domain.usecase.DeleteUseCase
import com.example.medicalreminder.domain.usecase.GetAllReminderUseCase
import com.example.medicalreminder.domain.usecase.InsertUseCase
import com.example.medicalreminder.domain.usecase.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertUseCase: InsertUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val updateUseCase: UpdateUseCase,
    private val getAllReminderUseCase: GetAllReminderUseCase
) : ViewModel() {

    val uiState = getAllReminderUseCase.invoke().map { UiState(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState())

    fun insert(reminder: Reminder) = viewModelScope.launch {
        insertUseCase.invoke(reminder)
    }

    fun update(reminder: Reminder) = viewModelScope.launch {
        updateUseCase.invoke(reminder)
    }

    fun delete(reminder: Reminder) = viewModelScope.launch {
        deleteUseCase.invoke(reminder)
    }

}

data class UiState(
    val data: List<Reminder> = emptyList()
)
