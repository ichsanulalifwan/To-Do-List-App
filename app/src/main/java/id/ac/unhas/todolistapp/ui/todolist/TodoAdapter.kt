package id.ac.unhas.todolistapp.ui.todolist

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.time.Converters
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

typealias ClickListener = (Todo) -> Unit

class TodoAdapter (
    private val clickListener: ClickListener
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var dateFormat = SimpleDateFormat("dd MMM, YYYY", Locale.getDefault())

    private var todoList = listOf<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(itemContainer)
    }

    override fun getItemCount() = todoList.size

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current = todoList[position]
        val create = dateFormat.format(converter(current.createDate))
        val due = dateFormat.format(converter(current.dueDate))
        holder.tvTodo.text = current.todo
        holder.tvDesc.text = current.desc
        holder.tvCreated.text = create
        holder.tvDue.text = due

        holder.itemView.setOnClickListener {
            clickListener(todoList[holder.adapterPosition])
        }
    }

    internal fun setTodo(todo: List<Todo>) {
        this.todoList = todo
        notifyDataSetChanged()
    }

    fun converter(value: Long?): Date? {
            return value?.let { Date(it) }
        }

    fun getTodoAt(position : Int): Todo {
        return todoList[position]
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodo: TextView = itemView.findViewById(R.id.todo_name_text_view)
        val tvDesc: TextView = itemView.findViewById(R.id.todo_description)
        val tvCreated: TextView = itemView.findViewById(R.id.addDate_text_view)
        val tvDue: TextView = itemView.findViewById(R.id.dueDate_text_view)
    }
}
