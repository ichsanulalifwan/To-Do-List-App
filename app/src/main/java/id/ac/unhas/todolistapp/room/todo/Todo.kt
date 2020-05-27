package id.ac.unhas.todolistapp.room.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "todo") val todo: String?,
    @ColumnInfo(name = "desc") val desc: String?,
    @ColumnInfo(name = "created_date") val createDate: Long?,
    @ColumnInfo(name = "due_date") val dueDate: Long?,
    @ColumnInfo(name = "due_time") val dueTime: Long?,
    @ColumnInfo(name = "update_date") val updateDate: Long?
)