@file:Suppress("DEPRECATION")

package id.ac.unhas.todolistapp.ui.addtodo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
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

@Suppress("SENSELESS_COMPARISON")
class AddTodoFragment : Fragment() {

    private var todoList: Todo? = null
    private lateinit var listViewModel: AddTodoViewModel
    private var dateFormat = SimpleDateFormat("dd MMM, YYYY", Locale.getDefault())
    private var timeFormat = SimpleDateFormat("hh:mm s", Locale.getDefault())
    private var initialValue = ContentValues()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_todo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listViewModel = ViewModelProviders.of(this).get(AddTodoViewModel::class.java)

        add_button.setOnClickListener {
            val id = if (todoList != null) todoList?.id else null
            val title = add_todo.text.toString()
            val desc = add_description.text.toString()
            val create = System.currentTimeMillis()
            val dueDate = initialValue.get("add_dueDate")
            val dueTime = initialValue.get("add_dueTime")
            val update = System.currentTimeMillis()
            if (title != null && desc != null && dueDate != null && dueTime != null) {
            val add = Todo(
                id = id,
                todo = title,
                desc = desc,
                createDate = create,
                dueDate = dueDate as Long,
                dueTime =  dueTime as Long,
                updateDate = update
            )
                listViewModel.addTodo(add)
            } else Toast.makeText(context,"Please Enter Data Correctly!", Toast.LENGTH_SHORT).show()

            listViewModel.observableStatus.observe(viewLifecycleOwner, Observer { todo ->
                todo?.let { check(todo) }
            })
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
                            add_date.setText(date)
                            initialValue.put("add_dueDate", selectedDate.timeInMillis)
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
                initialValue.put("add_dueTime", selectedTime.timeInMillis)
            },
                nowTime.get(Calendar.HOUR_OF_DAY), nowTime.get(Calendar.MINUTE), false
            )
            timePicker.show()
        }
    }

    private fun check(status: Boolean) {
        when (status) {
            true ->  {
                findNavController().navigate(R.id.action_add_to_todoList)
                Toast.makeText(context,"Successfully Add To-Do", Toast.LENGTH_SHORT).show()
            }
            false -> Toast.makeText(context,"Failed to Add To-Do", Toast.LENGTH_SHORT).show()
        }
    }
}