package hu.bme.aut.android.bookinventory

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.bookinventory.adapter.BookItemAdapter
import hu.bme.aut.android.bookinventory.data.BookItem
import hu.bme.aut.android.bookinventory.data.BookListDatabase
import hu.bme.aut.android.bookinventory.databinding.ActivityMainBinding
import hu.bme.aut.android.bookinventory.fragments.EditBookItemDialogFragment
import hu.bme.aut.android.bookinventory.fragments.NewBookItemDialogFragment
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), BookItemAdapter.BookItemClickListener, NewBookItemDialogFragment.NewBookItemDialogListener, EditBookItemDialogFragment.EditBookItemDialogListener, CoroutineScope by MainScope() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: BookListDatabase
    private lateinit var adapter: BookItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        setTheme(hu.bme.aut.android.bookinventory.R.style.Theme_BookInventory)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = BookListDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener {
            NewBookItemDialogFragment().show(supportFragmentManager, NewBookItemDialogFragment.TAG)
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = BookItemAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() = launch {
        val items = withContext(Dispatchers.IO) {
            database.bookItemDao().getAll()
        }
        adapter.update(items)
    }

    override fun onItemChanged(item: BookItem) {
        updateItemInBackground(item)
    }

    private fun updateItemInBackground(item: BookItem) = launch {
        withContext(Dispatchers.IO) {
            database.bookItemDao().update(item)
        }
    }

    override fun onBookItemCreated(item: BookItem) {
        addItemInBackground(item)
    }

    private fun addItemInBackground(item: BookItem) = launch {
        withContext(Dispatchers.IO) {
            database.bookItemDao().insert(item)
        }
        adapter.addItem(item)
    }

    override fun onItemRemoved(item: BookItem) {
        removeItemInBackground(item)
    }

    private fun removeItemInBackground(item: BookItem) = launch {
        withContext(Dispatchers.IO) {
            database.bookItemDao().deleteItem(item)
        }
        adapter.deleteItem(item)
    }

    override fun onItemSelected(item: BookItem) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("EXTRA_BOOK", item)

        startActivity(intent)
    }

    override fun onItemEdited(item: BookItem) {
        sendData(item)
    }

    private fun sendData(item: BookItem) {
        val bundle = Bundle()
        bundle.putSerializable("BOOK_KEY", item)

        val editFragment = EditBookItemDialogFragment()
        editFragment.arguments = bundle

        editFragment.show(supportFragmentManager, EditBookItemDialogFragment.TAG)
    }

    override fun onBookItemUpdated(item: BookItem) {
        updateItemInBackground(item)
        initRecyclerView()
    }

}