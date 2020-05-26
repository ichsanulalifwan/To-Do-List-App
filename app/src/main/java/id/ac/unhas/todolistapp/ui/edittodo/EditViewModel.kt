package id.ac.unhas.todolistapp.ui.edittodo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.ac.unhas.todolistapp.repository.TodoRepository
import id.ac.unhas.todolistapp.room.todo.Todo

class EditViewModel(application: Application) : AndroidViewModel(application) {

    private val currentTodo = MutableLiveData<List<Todo>>()
    private val editStatus = MutableLiveData<Boolean>()
    private val todoRepository = TodoRepository(application)

    val observableCurrentTodo: LiveData<List<Todo>>
        get() = currentTodo

    val observableEditStatus: LiveData<Boolean>
        get() = editStatus

    fun updateTodo(todo: Todo) {
        editStatus.value = try {
            todoRepository.update(todo)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    /*fun initTodo(id: Int) {
        currentTodo.value = todoRepository.getTodo(id)
    }*/
}