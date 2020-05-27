package id.ac.unhas.todolistapp.ui.todolist

import android.app.SearchManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import id.ac.unhas.todolistapp.R
import id.ac.unhas.todolistapp.room.todo.Todo
import kotlinx.android.synthetic.main.todo_list_fragment.*

@Suppress("UNREACHABLE_CODE")
class TodoListFragment : Fragment () {

    private lateinit var todoListViewModel: TodoListViewModel
    private lateinit var deleteIcon: Drawable
    private val clickListener: ClickListener = this::onTodoClicked
    private val rvAdapter = TodoAdapter(clickListener)
    private var swipeBackground: PaintDrawable = PaintDrawable()
    @RequiresApi(Build.VERSION_CODES.M)
    private var colorBackground = Color.parseColor("#FF0000")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_list_fragment, container, false)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(bottomAppBar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val serarchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search)?.actionView as androidx.appcompat.widget.SearchView
        searchView.setSearchableInfo(serarchManager.getSearchableInfo(requireActivity().componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                rvAdapter.getFilter().filter(query)
                Toast.makeText(context, query, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvAdapter.getFilter().filter(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_sortCreated -> {
                todoListViewModel.sortCreated()?.observe(viewLifecycleOwner, Observer { todo ->
                    todo?.let { render(todo) }
                })
                Toast.makeText(context, "Sorted By Created Date", Toast.LENGTH_SHORT).show()
            }

            R.id.action_sortDue -> {
                todoListViewModel.sortDue()?.observe(viewLifecycleOwner, Observer { todo ->
                    todo?.let { render(todo) }
                })
                Toast.makeText(context, "Sorted By Due Date", Toast.LENGTH_SHORT).show()
            }
            R.id.search -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        deleteIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete_24px) }!!

        todoListViewModel = ViewModelProvider(this).get(TodoListViewModel::class.java)
        todoListViewModel.getTodo()?.observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo) }
        })

        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER

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
                    todoListViewModel.deleteTodo(rvAdapter.getTodoAt(viewHolder.adapterPosition))
                    dialog.dismiss()
                }
                alert.setNegativeButton("No") { dialog, _ ->
                    todoListViewModel.getTodo()?.observe(viewLifecycleOwner, Observer { todo ->
                        todo?.let { render(todo) }
                    })
                    dialog.dismiss()
                }
                alert.show()
            }

            @RequiresApi(Build.VERSION_CODES.M)
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
                val cardMargin = itemView.resources.getDimension(R.dimen.card_margin).toInt()
                val cardCornerRadius = itemView.resources.getDimension(R.dimen.card_corner_radius)
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
                if (dX > 0) {
                    swipeBackground.setBounds(itemView.left + cardMargin, itemView.top,
                        dX.toInt()  + cardMargin + cardCornerRadius.toInt() * 2, itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin,
                        itemView.left + iconMargin + deleteIcon.intrinsicWidth, itemView.bottom - iconMargin)
                } else {
                    swipeBackground.setBounds(itemView.right + dX.toInt() - cardMargin - cardCornerRadius.toInt() * 2, itemView.top,
                        itemView.right - cardMargin , itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin,
                        itemView.right - iconMargin, itemView.bottom - iconMargin)
                }
                swipeBackground.setCornerRadius(cardCornerRadius)
                swipeBackground.paint.color = colorBackground
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
            val action = TodoListFragmentDirections.actionTodoListToAdd()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        todoListViewModel.getTodo()?.observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo) }
        })
    }

    private fun render(todoList: List<Todo>) {
        rvAdapter.setTodo(todoList)
        if (todoList.isEmpty()) {
            todoRecyclerView.visibility = View.GONE
            empty_list_image.visibility = View.VISIBLE
            empty_list_message.visibility = View.VISIBLE
        } else {
            todoRecyclerView.visibility = View.VISIBLE
            empty_list_image.visibility = View.GONE
            empty_list_message.visibility = View.GONE
        }
    }

    private fun onTodoClicked(todo: Todo) {
        val action = todo.id?.let {
            TodoListFragmentDirections.actionTodoListToEdit(it)}
        action?.let {
            findNavController().navigate(it) }
        }

    private fun setupRecyclerView() {
        todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
        todoRecyclerView.adapter = rvAdapter
        todoRecyclerView.setHasFixedSize(true)
    }
}