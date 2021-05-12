package hu.bme.aut.android.bookinventory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import hu.bme.aut.android.bookinventory.data.BookItem
import hu.bme.aut.android.bookinventory.data.BookItemCategory
import hu.bme.aut.android.bookinventory.databinding.ActivityDetailsBinding
import hu.bme.aut.android.bookinventory.databinding.ActivityMainBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var bookItem: BookItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val bundle: Bundle? = intent.extras
        bookItem = bundle?.getSerializable("EXTRA_BOOK") as BookItem

        supportActionBar!!.title = getString(R.string.book, bookItem.title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.etTitle.text = bookItem.title
        binding.etAuthor.text = bookItem.author
        binding.tvCategory.text = bookItem.category.toString()
        binding.etYear.text = bookItem.year.toString()
        binding.cbAlreadyRead.isChecked= bookItem.isRead

        binding.button.setOnClickListener {
            val intent = Intent(this, SimilarActivity::class.java)
            intent.putExtra("EXTRA_SIMILAR_BOOK", bookItem)

            startActivity(intent)
        }

        binding.ivIcon.setImageResource(getImageResource(BookItemCategory.valueOf(binding.tvCategory.text.toString())))

    }

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
}