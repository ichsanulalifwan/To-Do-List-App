package id.ac.unhas.todolistapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.repository.TodoRepository

class TodoViewModel(application: Application) : AndroidViewModel(application){

    private val todoRepository = TodoRepository(application)
    private val status = MutableLiveData<Boolean>()
    private val todoList : LiveData<List<Todo>> = todoRepository.getTodo()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addTodo(todo: Todo) {
        status.value = try{
            todoRepository.insert(todo)
            true
        } catch (e: IllegalArgumentException){
            false
        }
    }

    fun getTodo(): LiveData<List<Todo>> {
        return todoList
    }

    fun deleteTodo(todo: Todo) {
        todoRepository.delete(todo)
    }

    fun updateTodo(todo: Todo) {
        todoRepository.update(todo)
    }
}