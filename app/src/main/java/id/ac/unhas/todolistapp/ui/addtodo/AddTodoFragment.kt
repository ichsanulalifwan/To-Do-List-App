package id.ac.unhas.todolistapp.ui.addtodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import kotlinx.android.synthetic.main.add_todo_fragment.*

@Suppress("DEPRECATION")
class AddTodoFragment : Fragment() {
    private var todoList: Todo? = null

    private lateinit var viewModel: AddTodoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_todo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddTodoViewModel::class.java)
        viewModel.observableStatus.observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo) }
        })

        add_button.setOnClickListener {
            val id = if (todoList != null) todoList?.id else null
            val todo = add_todo.text.toString()
            val add = Todo (id = id, todo = todo )
            viewModel.addTodo(add)

            Toast.makeText(context, todo, Toast.LENGTH_SHORT).show()
        }
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    Navigation.findNavController(it).popBackStack()
                }
            }
            false -> add_todo.error = getString(R.string.error_validating_text)
        }
    }
}
