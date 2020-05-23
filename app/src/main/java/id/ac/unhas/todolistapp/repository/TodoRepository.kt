package id.ac.unhas.todolistapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import id.ac.unhas.todolistapp.room.TodoDatabase
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.room.todo.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TodoRepository(application: Application) {

    private val todoDao: TodoDao
    private var todo: LiveData<List<Todo>>

    init {
        val db = TodoDatabase.getDatabase(application.applicationContext)
        todoDao = db!!.todoDao()
        todo = todoDao.loadAllTodo()
    }

    fun getTodo(): LiveData<List<Todo>> {
        return todo
    }

    fun insert(todo: Todo) = runBlocking {
        this.launch(Dispatchers.IO) {
            todoDao.insertTodo(todo)
        }
    }

    fun delete(todo: Todo) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                todoDao.deleteTodo(todo)
            }
        }
    }

    fun update(todo: Todo) = runBlocking {
        this.launch(Dispatchers.IO) {
            todoDao.updateTodo(todo)
        }
    }
}