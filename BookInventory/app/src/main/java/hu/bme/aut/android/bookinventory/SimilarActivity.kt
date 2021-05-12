package hu.bme.aut.android.bookinventory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.android.bookinventory.adapter.SimilarBookAdapter
import hu.bme.aut.android.bookinventory.data.BookItem
import hu.bme.aut.android.bookinventory.data.BookListDatabase
import hu.bme.aut.android.bookinventory.databinding.ActivitySimilarBinding
import kotlinx.coroutines.*

class SimilarActivity : AppCompatActivity(), SimilarBookAdapter.SimilarItemClickListener, CoroutineScope by MainScope() {
    private lateinit var binding: ActivitySimilarBinding

    private lateinit var database: BookListDatabase
    private lateinit var adapter: SimilarBookAdapter

    private lateinit var bookItem: BookItem

    private var numOfBooks: Int = 0
    private var numOfSimilarBooks: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimilarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = BookListDatabase.getDatabase(applicationContext)

        val bundle: Bundle? = intent.extras
        bookItem = bundle?.getSerializable("EXTRA_SIMILAR_BOOK") as BookItem

        supportActionBar!!.title = getString(R.string.similar, bookItem.title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initRecyclerView()
    }


    private fun initRecyclerView() {
        adapter = SimilarBookAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() = launch {
        val items = withContext(Dispatchers.IO) {
            database.bookItemDao().getAll()
        }
        numOfBooks = items.size

        val similarItems = mutableListOf<BookItem>()

        for(item: BookItem in items){
            if (item.title != bookItem.title && (item.author == bookItem.author || item.category == bookItem.category)){
                similarItems.add(item)
            }
        }
        numOfSimilarBooks = similarItems.size
        adapter.update(similarItems)
        loadPieChart()
    }

    override fun onItemSelected(item: BookItem) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("EXTRA_BOOK", item)

        startActivity(intent)
    }

    private fun loadPieChart() {
        var entries: ArrayList<PieEntry> = ArrayList()

        entries.add(PieEntry(numOfSimilarBooks.toFloat(), "Similar"))
        entries.add(PieEntry(numOfBooks.toFloat(), "Rest"))

        val colors = mutableListOf(
                ColorTemplate.rgb("#ffccb3"), ColorTemplate.rgb("#ff9966"), ColorTemplate.rgb("#e74c3c"), ColorTemplate.rgb("#3498db")
        )

        val dataSet = PieDataSet(entries, "Books")
        dataSet.colors = colors

        val data = PieData(dataSet)
        binding.chartBooks.data = data
        binding.chartBooks.invalidate()
    }

}