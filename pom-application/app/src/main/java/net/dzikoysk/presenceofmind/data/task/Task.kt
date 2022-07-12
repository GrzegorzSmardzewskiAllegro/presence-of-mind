package net.dzikoysk.presenceofmind.data.task

import androidx.compose.ui.graphics.Color
import net.dzikoysk.presenceofmind.data.attributes.*
import java.io.Serializable
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val categories: List<UUID> = emptyList(),
    val doneDate: Long? = null,
    val doneCount: Int = 0,
    val open: Boolean = true,
    /* Attributes */
    val checklistAttribute: ChecklistAttribute? = null,
    val eventAttribute: EventAttribute? = null,
    val repetitiveAttribute: RepetitiveAttribute? = null,
    val pomodoroAttribute: PomodoroAttribute? = null,
) : Serializable

val Task.attributes: Collection<Attribute>
    get() = listOfNotNull(
        checklistAttribute,
        eventAttribute,
        repetitiveAttribute,
        pomodoroAttribute
    )

inline fun <reified A : Attribute> Task.getAttribute(): A? =
    attributes.find { it is A } as A?

fun Task.getAccentColor(): Color =
    attributes
        .sortedBy { it.getPriority() }
        .firstNotNullOfOrNull { it.getDefaultAccentColor() }
        ?: Color(0xFFC3EEFF)

fun Task.isConcealable(): Boolean =
    attributes.any { it.isConcealable() }

fun Task.isOpen(): Boolean =
    open || attributes.find { it.isRunning() } != null

fun Task.isDone(): Boolean =
    doneDate != null