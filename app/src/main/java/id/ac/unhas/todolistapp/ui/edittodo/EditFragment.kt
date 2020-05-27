@file:Suppress("DEPRECATION")

package id.ac.unhas.todolistapp.ui.edittodo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import kotlinx.android.synthetic.main.edit_fragment.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("SENSELESS_COMPARISON")
class EditFragment : Fragment() {

    private lateinit var viewModel: EditViewModel
    private val args by navArgs<EditFragmentArgs>()
    private var dateFormat = SimpleDateFormat("dd MMM, YYYY", Locale.getDefault())
    private var timeFormat = SimpleDateFormat("hh:mm s", Locale.getDefault())
    private var initialValue = ContentValues()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        viewModel.observableCurrentTodo.observe(viewLifecycleOwner, Observer { currentTodo ->
            currentTodo?.let { initCurrentTodo(currentTodo) } ?: todoNotFound()
        })
        viewModel.getTodoData(args.todoId)

        update_button.setOnClickListener {
            viewModel.observableEditStatus.observe(viewLifecycleOwner, Observer { editStatus ->
                editStatus?.let { check(editStatus) }
            })

            val id = args.todoId
            val title = update_title.text.toString()
            val desc = update_description.text.toString()
            val create = initialValue.get("created_date")
            val dueDate = initialValue.get("update_dueDate")
            val dueTIme = initialValue.get("update_dueTime")
            val update = System.currentTimeMillis()
            if(title != null && desc!= null && dueDate != null && dueTIme != null) {
                val add = Todo(
                    id = id,
                    todo = title,
                    desc = desc,
                    createDate = create as Long,
                    dueDate = dueDate as Long,
                    dueTime = dueTIme as Long,
                    updateDate = update
                )
                viewModel.updateTodo(add)
            } else Toast.makeText(context,"Please Enter Data Correctly!", Toast.LENGTH_SHORT).show()
        }

        btn_updateDate.setOnClickListener {
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
                            update_dueDate.setText(date)
                            initialValue.put("update_dueDate", selectedDate.timeInMillis)
                        },
                        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                }
            datePicker?.show()
        }

        btn_updateTime.setOnClickListener{
            val nowTime = Calendar.getInstance()
            val timePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val time = timeFormat.format(selectedTime.time)
                update_dueTime.setText(time)
                initialValue.put("update_dueTime", selectedTime.timeInMillis)
            },
                nowTime.get(Calendar.HOUR_OF_DAY), nowTime.get(Calendar.MINUTE), false
            )
            timePicker.show()
        }
    }

    private fun initCurrentTodo(todo: Todo) {
        createdDateView.text = dateFormat.format(todo.createDate)
        createdTimeView.text = timeFormat.format(todo.createDate)
        update_title.setText(todo.todo)
        update_description.setText(todo.desc)
        update_dueDate.setText(dateFormat.format(todo.dueDate))
        update_dueTime.setText(timeFormat.format(todo.dueTime))
        initialValue.put("created_date", todo.createDate)
    }

    private fun check(status: Boolean) {
        when (status) {
            true -> {
                findNavController().navigate(R.id.action_edit_to_todoList)
                Toast.makeText(context,"Successfully Update To-Do ", Toast.LENGTH_SHORT).show()
            }
            false -> Toast.makeText(context,"Failed to Update To-Do", Toast.LENGTH_SHORT).show()
        }
    }

    private fun todoNotFound() {
        view?.let {
            Snackbar.make(it, R.string.error_loading_data, Snackbar.LENGTH_LONG).show()
        }
    }
}
