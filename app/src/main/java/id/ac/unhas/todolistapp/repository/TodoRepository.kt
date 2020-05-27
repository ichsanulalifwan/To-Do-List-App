package id.ac.unhas.todolistapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import id.ac.unhas.todolistapp.room.AppDatabase
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.room.todo.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TodoRepository(application: Application) {

    private val todoDao: TodoDao
    private var todoList: LiveData<List<Todo>>?
    private var sortCreatedDate: LiveData<List<Todo>>
    private var sortDueDate: LiveData<List<Todo>>

    init {
        val db = AppDatabase.getDatabase(application.applicationContext)
        todoDao = db!!.todoDao()
        todoList = todoDao.loadAllTodo()
        sortCreatedDate = todoDao.sortCreated()
        sortDueDate = todoDao.sortDue()
    }

    fun getTodoList(): LiveData<List<Todo>>? {
        return todoList
    }

    fun sortByCreated(): LiveData<List<Todo>>? {
        return sortCreatedDate
    }

    fun sortByDue(): LiveData<List<Todo>>? {
        return sortDueDate
    }

    suspend fun getTodo(id: Int): Todo {
        return todoDao.loadSingle(id)
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