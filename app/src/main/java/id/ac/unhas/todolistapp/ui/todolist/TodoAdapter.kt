package id.ac.unhas.todolistapp.ui.todolist

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

typealias ClickListener = (Todo) -> Unit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TodoAdapter (
    private val clickListener: ClickListener
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var dateFormat = SimpleDateFormat("EEEE, dd MMM YYYY", Locale.getDefault())
    private var todoList = listOf<Todo>()
    private var todoFilterList: List<Todo> = arrayListOf()

    init {
        todoFilterList = todoList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(itemContainer)
    }

    override fun getItemCount() = todoFilterList.size

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current = todoFilterList[position]
        val create = dateFormat.format(converter(current.createDate))
        val dueDate = dateFormat.format(converter(current.dueDate))
        val update = dateFormat.format(converter(current.updateDate))
        holder.tvTodo.text = current.todo
        holder.tvDesc.text = current.desc
        holder.tvDueDate.text = dueDate

        if(current.createDate == current.updateDate){
            holder.tvCreated.text = create
            holder.tvAddView.visibility = View.VISIBLE
            holder.tvUpdatedView.visibility = View.GONE
        } else {
            holder.tvUpdate.text = update
            holder.tvAddView.visibility = View.GONE
            holder.tvUpdatedView.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            clickListener(todoFilterList[holder.adapterPosition])
        }
    }

    internal fun setTodo(todo: List<Todo>) {
        this.todoList = todo
        this.todoFilterList = todo
        notifyDataSetChanged()
    }


    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraints: CharSequence?): FilterResults {
                val charSearch = constraints.toString().toLowerCase(Locale.ROOT).trim()
                todoFilterList = if (charSearch.isEmpty()){
                    todoList
                } else {
                    val resultList = arrayListOf<Todo>()
                    for (item in todoList) {
                        if (item.todo?.toLowerCase(Locale.ROOT)?.contains(charSearch)!!) {
                            resultList.add(item)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = todoFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraints: CharSequence?, results: FilterResults) {
                todoFilterList = results.values as ArrayList<Todo>
                notifyDataSetChanged()
            }
        }
    }

    private fun converter(value: Long?): Date? {
            return value?.let { Date(it) }
        }

    fun getTodoAt(position : Int): Todo {
        return todoList[position]
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodo: TextView = itemView.findViewById(R.id.todo_name_text_view)
        val tvDesc: TextView = itemView.findViewById(R.id.todo_description)
        val tvCreated: TextView = itemView.findViewById(R.id.addDate_text_view)
        val tvDueDate: TextView = itemView.findViewById(R.id.dueDate_text_view)
        val tvUpdate: TextView = itemView.findViewById(R.id.updateDate_text_view)
        val tvAddView: TextView = itemView.findViewById(R.id.add_date_text_view)
        val tvUpdatedView: TextView = itemView.findViewById(R.id.update_date_text_view)
    }
}
