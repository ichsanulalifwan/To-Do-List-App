package id.ac.unhas.todolistapp.ui.todolist

import androidx.recyclerview.widget.DiffUtil
import id.ac.unhas.todolistapp.room.todo.Todo

class TodoDiffCallback (private val old: List<Todo>,
                        private val new: List<Todo>) : DiffUtil.Callback() {
    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean {
        return old[oldIndex].todo == new[newIndex].todo
    }

    override fun areContentsTheSame(oldIndex: Int, newIndex: Int): Boolean {
        return old[oldIndex] == new[newIndex]
    }
}