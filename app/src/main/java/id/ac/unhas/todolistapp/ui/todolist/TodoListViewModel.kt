package id.ac.unhas.todolistapp.ui.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.repository.TodoRepository

class TodoListViewModel(application: Application) : AndroidViewModel(application){

    private val todoRepository = TodoRepository(application)
    private val todoList : LiveData<List<Todo>>? = todoRepository.getTodoList()
    private val sortCreated : LiveData<List<Todo>>? = todoRepository.sortByCreated()
    private val sortDue : LiveData<List<Todo>>? = todoRepository.sortByDue()

    fun getTodo(): LiveData<List<Todo>>? {
        return todoList
    }

    fun sortCreated(): LiveData<List<Todo>>? {
        return sortCreated
    }

    fun sortDue(): LiveData<List<Todo>>? {
        return sortDue
    }

    fun deleteTodo(todo: Todo) {
        todoRepository.delete(todo)
    }
}