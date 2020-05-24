package id.ac.unhas.todolistapp.ui.todolist

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import id.ac.unhas.todolistapp.ui.TodoViewModel
import kotlinx.android.synthetic.main.todo_list_fragment.*

class TodoListFragment : Fragment () {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var deleteIcon: Drawable
    private val clickListener: ClickListener = this::onTodoClicked
    private val rvAdapter = TodoAdapter(clickListener)
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        deleteIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete_24px) }!!

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.getTodo().observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo) }
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val alert = AlertDialog.Builder(viewHolder.itemView.context)
                alert.setTitle("Are you sure to delete this?")
                alert.setPositiveButton("Yes") { dialog, _ ->
                    todoViewModel.deleteTodo(rvAdapter.getTodoAt(viewHolder.adapterPosition))
                    dialog.dismiss()
                }
                alert.setNegativeButton("No") { dialog, _ ->
                    todoViewModel.getTodo().observe(viewLifecycleOwner, Observer { todo ->
                        todo?.let { render(todo) }
                    })
                    dialog.dismiss()
                }
                alert.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
                if (dX > 0) {
                    swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin,
                        itemView.left + iconMargin + deleteIcon.intrinsicWidth, itemView.bottom - iconMargin)
                } else {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin,
                        itemView.right - iconMargin, itemView.bottom - iconMargin)
                }
                swipeBackground.draw(c)

                c.save()

                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(todoRecyclerView)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_todoList_to_add)
        }
    }

    override fun onResume() {
        super.onResume()
        todoViewModel.getTodo().observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo) }
        })
    }

    private fun render(todoList: List<Todo>) {
        rvAdapter.setTodo(todoList)
        if (todoList.isEmpty()) {
            todoRecyclerView.visibility = View.GONE
            todoNotFound.visibility = View.VISIBLE
        } else {
            todoRecyclerView.visibility = View.VISIBLE
            todoNotFound.visibility = View.GONE
        }
    }

    private fun onTodoClicked(todo: Todo) {/*
        val action = TodoListFragmen.actionNotesToNoteDetail(todo.id)
        findNavController().navigate(action)*/
    }

    private fun setupRecyclerView() {
        todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
        todoRecyclerView.adapter = rvAdapter
        todoRecyclerView.setHasFixedSize(true)
    }
}