package id.ac.unhas.todolistapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.room.todo.TodoDao

@Database(entities = [Todo::class], exportSchema = false, version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        private var instance: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase? {
            if (instance == null) {
                synchronized(TodoDatabase::class) {
                    instance = Room.databaseBuilder(
                            context,
                            TodoDatabase::class.java,
                            "TODO_DB"
                        )
                        .build()
                }
            }
            return instance
        }
    }
}