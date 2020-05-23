package id.ac.unhas.todolistapp.ui.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.repository.TodoRepository

class TodoListViewModel(application: Application) : AndroidViewModel(application){

    private val todoRepository = TodoRepository(application)
    private val todoList : LiveData<List<Todo>> = todoRepository.getTodo()

    fun addTodo(todo: Todo) {
        todoRepository.insert(todo)
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