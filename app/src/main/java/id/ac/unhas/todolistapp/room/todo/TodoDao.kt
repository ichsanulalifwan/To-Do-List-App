package id.ac.unhas.todolistapp.room.todo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Query("Select * from todo")
    fun loadAllTodo(): LiveData<List<Todo>>?

    /*@Query("Select * from todo Where todo =:todo")
    suspend fun loadSingle(todo : Todo)*/
    @Query("SELECT * FROM todo ORDER BY created_date DESC")
    fun sortCreated(): LiveData<List<Todo>>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)
}