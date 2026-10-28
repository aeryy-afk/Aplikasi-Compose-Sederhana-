package com.example.taskapp.model

import java.util.UUID

// Model Data
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val isCompleted: Boolean = false
)

// UI State
data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val inputText: String = "",
    val errorMessage: String? = null
)