package id.ac.unhas.todolistapp.ui.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import kotlinx.android.synthetic.main.todo_list_fragment.*

class TodoListFragment : Fragment () {

    private val clickListener: ClickListener = this::onTodoClicked
    private val rvAdapter = TodoAdapter(clickListener)
    private lateinit var todoListViewModel: TodoListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        todoListViewModel = ViewModelProvider(this).get(TodoListViewModel::class.java)
        todoListViewModel.getTodo().observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo) }
        })


        fab.setOnClickListener {
            findNavController().navigate(R.id.action_todoList_to_add)
        }

    }

    override fun onResume() {
        super.onResume()
        todoListViewModel.getTodo()
    }

    private fun render(todoList: List<Todo>) {
        rvAdapter.updateTodo(todoList)
        if (todoList.isEmpty()) {
            todoRecyclerView.visibility = View.GONE
            todoNotFound.visibility = View.VISIBLE
        } else {
            todoRecyclerView.visibility = View.VISIBLE
            todoNotFound.visibility = View.GONE
        }
    }

    private fun onTodoClicked(todo: Todo) {/*
        val action = TodoListFragmen.actionNotesToNoteDetail(todo.id)
        findNavController().navigate(action)*/
    }

    private fun setupRecyclerView() {
        todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
        todoRecyclerView.adapter = rvAdapter
        todoRecyclerView.setHasFixedSize(true)
    }
}