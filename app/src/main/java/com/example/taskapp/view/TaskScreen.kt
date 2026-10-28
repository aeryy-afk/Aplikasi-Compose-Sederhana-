package com.example.taskapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskapp.model.Task
import com.example.taskapp.model.TaskUiState
import com.example.taskapp.viewmodel.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (isWideScreen) {
            Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                InputSection(
                    uiState = uiState,
                    onValueChange = viewModel::onInputTextChanged,
                    onAddTask = viewModel::addTask,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                TaskListSection(
                    uiState = uiState,
                    onDelete = viewModel::deleteTask,
                    onToggle = viewModel::toggleTaskCompletion,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                InputSection(
                    uiState = uiState,
                    onValueChange = viewModel::onInputTextChanged,
                    onAddTask = viewModel::addTask,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TaskListSection(
                    uiState = uiState,
                    onDelete = viewModel::deleteTask,
                    onToggle = viewModel::toggleTaskCompletion,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }
        }
    }
}

@Composable
fun InputSection(
    uiState: TaskUiState,
    onValueChange: (String) -> Unit,
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = uiState.inputText,
            onValueChange = onValueChange,
            label = { Text("Nama Tugas") },
            isError = uiState.errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onAddTask,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Tugas")
        }
    }
}

@Composable
fun TaskListSection(
    uiState: TaskUiState,
    onDelete: (String) -> Unit,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.tasks.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada tugas. Yay!", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(uiState.tasks, key = { it.id }) { task ->
                TaskItem(task = task, onDelete = { onDelete(task.id) }, onToggle = { onToggle(task.id) })
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onDelete: () -> Unit, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })
            Text(
                text = task.title,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                style = if (task.isCompleted) MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                ) else MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus")
            }
        }
    }
}