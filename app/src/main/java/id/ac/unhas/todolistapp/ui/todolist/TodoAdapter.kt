package id.ac.unhas.todolistapp.ui.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo

typealias ClickListener = (Todo) -> Unit

class TodoAdapter (
    private val clickListener: ClickListener
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var todoList = emptyList<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false) as ViewGroup
        val viewHolder = TodoViewHolder(itemContainer)
        itemContainer.setOnClickListener {
            clickListener(todoList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount() = todoList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current = todoList[position]
        holder.tvTodo.text = current.todo
    }

   /* internal fun setTodo(todo: List<Todo>) {
        this.todoList = todo
        notifyDataSetChanged()
    }*/

    internal fun updateTodo(todoList: List<Todo>) {
        val diffResult = calculateDiff(
            TodoDiffCallback(
                this.todoList,
                todoList
            )
        )
        this.todoList = todoList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class TodoViewHolder(itemViewGroup: ViewGroup) : RecyclerView.ViewHolder(itemViewGroup) {
        val tvTodo: TextView = itemViewGroup.findViewById(R.id.todo_name_text_view)
    }
}
