package net.dzikoysk.presenceofmind.task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.UUID

class TaskService(
    val taskRepository: TaskRepository = InMemoryTaskRepository()
) {

    private val tasks = mutableStateListOf<Task>()

    init {
        tasks.addAll(taskRepository.loadOrderedTasks())
    }

    fun saveTask(task: Task) {
        when (val index = tasks.indexOfFirst { it.id == task.id }) {
            -1 -> tasks.add(task)
            else -> {
                tasks.removeAt(index)
                tasks.add(index, task)
            }
        }
        forceTasksSave()
    }

    fun moveTasks(from: Int, to: Int) {
        tasks.add(to, tasks.removeAt(from))
    }

    fun forceTasksSave() {
        taskRepository.saveOrderedTasks(tasks)
    }

    fun deleteTask(id: UUID) {
        tasks.removeIf { it.id == id }
        forceTasksSave()
    }

    fun findAllTasks(): List<Task> =
        taskRepository.loadOrderedTasks()

    fun getObservableListOfAllTasks(): SnapshotStateList<Task> =
        tasks

}

fun TaskService.createDefaultTasks() {
    saveTask(Task(description = "One-time task ~ Events", metadata = OneTimeMetadata()))
    saveTask(Task(
        description = "Repetitive task ~ Habits",
        metadata = RepetitiveMetadata(
            intervalInDays = 1,
            expectedAttentionInMinutes = 75
        )
    ))
    saveTask(Task(description = "Long-term task ~ Notes", metadata = LongTermMetadata))
    saveTask(Task(description = "Complex long-term task ~ Lists", metadata = LongTermMetadata, subtasks = listOf(
        SubTask(description = "To do", done = false),
        SubTask(description = "Done", done = true),
    )))
}