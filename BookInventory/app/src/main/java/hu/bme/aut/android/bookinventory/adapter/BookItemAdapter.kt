package hu.bme.aut.android.bookinventory.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.bookinventory.DetailsActivity
import hu.bme.aut.android.bookinventory.R
import hu.bme.aut.android.bookinventory.data.BookItem
import hu.bme.aut.android.bookinventory.data.BookItemCategory
import hu.bme.aut.android.bookinventory.databinding.ItemBookListBinding

class BookItemAdapter(private val listener: BookItemClickListener) : RecyclerView.Adapter<BookItemAdapter.BookItemViewHolder>() {
    private val items = mutableListOf<BookItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BookItemViewHolder(
        ItemBookListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        val bookItem = items[position]

        holder.binding.ivIcon.setImageResource(getImageResource(bookItem.category))
        holder.binding.cbIsRead.isChecked = bookItem.isRead
        holder.binding.tvTitle.text = bookItem.title
        holder.binding.tvAuthor.text = bookItem.author
        holder.binding.tvCategory.text = bookItem.category.name
        holder.binding.tvYear.text = bookItem.year.toString()

        holder.binding.cbIsRead.setOnCheckedChangeListener { buttonView, isChecked ->
            bookItem.isRead= isChecked
            listener.onItemChanged(bookItem)
        }

        holder.binding.ibRemove.setOnClickListener{
            listener.onItemRemoved(bookItem)
        }

        holder.itemView.setOnClickListener{
            listener.onItemSelected(bookItem)
        }

        holder.binding.ibEdit.setOnClickListener {
            listener.onItemEdited(bookItem)
        }
    }

    override fun getItemCount(): Int = items.size

    interface BookItemClickListener {
        fun onItemChanged(item: BookItem)
        fun onItemRemoved(item: BookItem)
        fun onItemSelected(item: BookItem)
        fun onItemEdited(item: BookItem)
    }

    inner class BookItemViewHolder(val binding: ItemBookListBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    @DrawableRes()
    private fun getImageResource(category: BookItemCategory): Int {
        return when (category) {
            BookItemCategory.CRIME-> R.drawable.ic_crime
            BookItemCategory.LITERATURE-> R.drawable.ic_literature
            BookItemCategory.ROMANTIC-> R.drawable.ic_romantic
            BookItemCategory.SCIFI-> R.drawable.ic_scifi
            BookItemCategory.HISTORICAL-> R.drawable.ic_historical
            BookItemCategory.DRAMA-> R.drawable.ic_drama
            BookItemCategory.POLITICAL-> R.drawable.ic_political
            BookItemCategory.KIDS-> R.drawable.ic_kids
            BookItemCategory.YOUNG-> R.drawable.ic_young
        }
    }

    fun addItem(item: BookItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(bookItems: List<BookItem>) {
        items.clear()
        items.addAll(bookItems)
        notifyDataSetChanged()
    }

    fun deleteItem(item: BookItem) {
        val position = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

}