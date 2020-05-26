package id.ac.unhas.todolistapp.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.ac.unhas.todolistapp.repository.TodoRepository
import id.ac.unhas.todolistapp.room.todo.Todo

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val todo = MutableLiveData<Todo>()
    private val todoRepository = TodoRepository(application)

    val observableTodo: LiveData<Todo>
        get() = todo

    /*fun getTodoData(id : Int){
        todo.value = todoRepository.getTodo(id)
    }*/
}