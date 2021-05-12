package hu.bme.aut.android.bookinventory.data

import androidx.room.*

@Dao
interface BookItemDao {
    @Query("SELECT * FROM bookItem")
    suspend fun getAll(): List<BookItem>

    @Insert
    suspend fun insert(items: BookItem): Long

    @Update
    suspend fun update(item: BookItem)

    @Delete
    suspend fun deleteItem(item: BookItem)
}