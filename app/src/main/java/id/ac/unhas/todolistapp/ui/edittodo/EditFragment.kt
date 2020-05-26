/*
@file:Suppress("DEPRECATION")

package id.ac.unhas.todolistapp.ui.edittodo

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import kotlinx.android.synthetic.main.edit_fragment.*

class EditFragment : Fragment() {

    private lateinit var viewModel: EditViewModel
    private var todoList: Todo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        */
/*viewModel.observableCurrentTodo.observe(viewLifecycleOwner, Observer { currentTodo ->
            currentTodo?.let { initCurrentTodo(currentTodo)
            }
        })*//*


        viewModel.observableEditStatus.observe(viewLifecycleOwner, Observer { updateStatus ->
            updateStatus?.let { render(updateStatus) }
        })

        update_button.setOnClickListener {
            val id = if (todoList != null) todoList?.id else null
            val todo = edit_title.text.toString()
            val desc = edit_description.text.toString()
            val update = Todo (id = id, todo = todo, desc = desc, createDate = )
            viewModel.updateTodo(update)
        }
    }

    private fun initCurrentTodo(todo: Todo) {
        edit_title.setText(todo.todo)
        edit_description.setText(todo.desc)
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    findNavController().navigate(R.id.action_edit_to_todoList)
                }
            }
            false -> edit_title.error = getString(R.string.error_validating_text)
        }
    }
}
*/
