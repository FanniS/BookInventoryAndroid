package hu.bme.aut.android.bookinventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable

enum class BookItemCategory {
    CRIME, LITERATURE, ROMANTIC, SCIFI, HISTORICAL, DRAMA, POLITICAL, KIDS, YOUNG;

    companion object {
        @TypeConverter
        @JvmStatic
        fun getByOrdinal(ordinal: Int): BookItemCategory? {
            var ret: BookItemCategory? = null
            for (cat in values()) {
                if (cat.ordinal == ordinal) {
                    ret = cat
                    break
                }
            }
            return ret
        }

        @TypeConverter
        @JvmStatic
        fun toInt(category: BookItemCategory): Int {
            return category.ordinal
        }

    }
}

@Entity(tableName = "bookItem")
data class BookItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,

    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "author") var author: String = "",
    @ColumnInfo(name = "year") var year: Int = 0,
    @ColumnInfo(name = "category") var category: BookItemCategory,
    @ColumnInfo(name = "is_read") var isRead: Boolean = false

) : Serializable