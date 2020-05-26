@file:Suppress("DEPRECATION")

package id.ac.unhas.todolistapp.ui.addtodo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import kotlinx.android.synthetic.main.add_todo_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class AddTodoFragment : Fragment() {
    private var todoList: Todo? = null

    private lateinit var listViewModel: AddTodoViewModel
    /*private lateinit var converters : Converters*/

    private var dateFormat = SimpleDateFormat("dd MMM, YYYY", Locale.getDefault())
    private var timeFormat = SimpleDateFormat("hh:mm s", Locale.getDefault())


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_todo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listViewModel = ViewModelProviders.of(this).get(AddTodoViewModel::class.java)
        listViewModel.observableStatus.observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo) }
        })

        add_button.setOnClickListener {
            findNavController().navigate(R.id.action_add_to_todoList)
        }

        btn_date.setOnClickListener{
            val now = Calendar.getInstance()
            val datePicker =
                context?.let { it1 ->
                    DatePickerDialog(
                        it1, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            val selectedDate = Calendar.getInstance()
                            selectedDate.set(Calendar.YEAR, year)
                            selectedDate.set(Calendar.MONTH, month)
                            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            val date = dateFormat.format(selectedDate.time)
                            val id = if (todoList != null) todoList?.id else null
                            val todo = add_todo.text.toString()
                            val desc = add_description.text.toString()
                            val create = System.currentTimeMillis()
                            val due = selectedDate.timeInMillis
                            if (add_todo != null || add_description != null) {
                                val add = Todo(
                                    id = id,
                                    todo = todo,
                                    desc = desc,
                                    createDate = create,
                                    dueDate = due
                                )
                                listViewModel.addTodo(add)
                            }
                            else Toast.makeText(context,"Please Input Title and Description! ", Toast.LENGTH_SHORT).show()
                            add_date.setText(date)
                        },
                        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                }
            datePicker?.show()
        }

        btn_time.setOnClickListener {
            val nowTime = Calendar.getInstance()
            val timePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val time = timeFormat.format(selectedTime.time)
                add_time.setText(time)
            },
                nowTime.get(Calendar.HOUR_OF_DAY), nowTime.get(Calendar.MINUTE), false
            )
            timePicker.show()
        }
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                   Toast.makeText(context,"Added To-Do Successfully", Toast.LENGTH_SHORT).show()
                }
            }
            false -> add_todo.error = getString(R.string.error_validating_text)
        }
    }
}