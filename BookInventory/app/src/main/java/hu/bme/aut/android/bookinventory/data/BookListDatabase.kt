package hu.bme.aut.android.bookinventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BookItem::class], version = 1)
@TypeConverters(value = [BookItemCategory::class])
abstract class BookListDatabase : RoomDatabase() {
    companion object {
        fun getDatabase(applicationContext: Context): BookListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                BookListDatabase::class.java,
                "book-list"
            ).build();
        }
    }

    abstract fun bookItemDao(): BookItemDao
}