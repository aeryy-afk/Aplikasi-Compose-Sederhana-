package com.example.taskapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskapp.model.Task
import com.example.taskapp.model.TaskUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun onInputTextChanged(newText: String) {
        _uiState.update { it.copy(inputText = newText, errorMessage = null) }
    }

    fun addTask() {
        val currentText = _uiState.value.inputText
        if (currentText.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Nama tugas tidak boleh kosong!") }
            return
        }

        val newTask = Task(title = currentText)
        _uiState.update { currentState ->
            currentState.copy(
                tasks = currentState.tasks + newTask,
                inputText = "",
                errorMessage = null
            )
        }
    }

    fun deleteTask(taskId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                tasks = currentState.tasks.filterNot { it.id == taskId }
            )
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            }
            currentState.copy(tasks = updatedTasks)
        }
    }
}