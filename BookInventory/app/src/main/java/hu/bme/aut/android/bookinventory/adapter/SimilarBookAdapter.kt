package hu.bme.aut.android.bookinventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.bookinventory.R
import hu.bme.aut.android.bookinventory.data.BookItem
import hu.bme.aut.android.bookinventory.data.BookItemCategory
import hu.bme.aut.android.bookinventory.databinding.SimilarItemBookListBinding

class SimilarBookAdapter(private val listener: SimilarBookAdapter.SimilarItemClickListener) : RecyclerView.Adapter<SimilarBookAdapter.SimilarItemViewHolder>() {

    private val items = mutableListOf<BookItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SimilarItemViewHolder(
        SimilarItemBookListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SimilarItemViewHolder, position: Int) {
        val bookItem = items[position]

        holder.binding.ivIcon.setImageResource(getImageResource(bookItem.category))
        holder.binding.cbIsRead.isChecked = bookItem.isRead
        holder.binding.tvTitle.text = bookItem.title
        holder.binding.tvAuthor.text = bookItem.author
        holder.binding.tvCategory.text = bookItem.category.name
        holder.binding.tvYear.text = bookItem.year.toString()


        holder.itemView.setOnClickListener{
            listener.onItemSelected(bookItem)
        }
    }

    override fun getItemCount(): Int = items.size

    interface SimilarItemClickListener {
        fun onItemSelected(item: BookItem)
    }

    inner class SimilarItemViewHolder(val binding: SimilarItemBookListBinding) :
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
    fun update(bookItems: List<BookItem>) {
        items.clear()
        items.addAll(bookItems)
        notifyDataSetChanged()
    }
}