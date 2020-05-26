package id.ac.unhas.todolistapp.ui.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar

import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.ui.todolist.TodoListViewModel
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel

    /*private val args by navArgs<DetailFragmentArgs>()*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.observableTodo.observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo)} ?: renderTodoNotFound()
        })

        editButton.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()
        /*viewModel.getTodo()*/
    }

    private fun render(todo : Todo) {
        todo_nameView.text = String.format(getString(R.string.todo_detail_text), todo.todo)
    }

    private fun renderTodoNotFound() {
        todo_nameView.visibility = View.GONE
        view?.let {
            Snackbar.make(it, R.string.error_loading_data, Snackbar.LENGTH_LONG).show()
        }
    }
}
