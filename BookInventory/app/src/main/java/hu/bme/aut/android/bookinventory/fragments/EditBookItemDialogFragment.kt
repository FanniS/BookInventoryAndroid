package hu.bme.aut.android.bookinventory.fragments

import android.R
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import hu.bme.aut.android.bookinventory.data.BookItem
import hu.bme.aut.android.bookinventory.data.BookItemCategory
import hu.bme.aut.android.bookinventory.databinding.DialogEditBookItemBinding

class EditBookItemDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "EditBookItemDialogFragment"
    }

    private lateinit var listener: EditBookItemDialogFragment.EditBookItemDialogListener

    private var _binding: DialogEditBookItemBinding? = null
    private val binding get() = _binding!!
    lateinit var bookItem: BookItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity: FragmentActivity = getActivity()!!
        listener = if (activity is EditBookItemDialogListener) {
            activity
        } else {
            throw RuntimeException("Activity must implement the NewBookItemDialogListener interface!")
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditBookItemBinding.inflate(LayoutInflater.from(context))

        val bundle: Bundle? = this.arguments
        bookItem = bundle?.getSerializable("BOOK_KEY") as BookItem

        binding.etTitle.setText(bookItem.title)
        binding.etAuthor.setText(bookItem.author)
        binding.etYear.setText(bookItem.year.toString())
        binding.cbAlreadyRead.isChecked = bookItem.isRead

        val adapter = ArrayAdapter(
            context!!,
            R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(hu.bme.aut.android.bookinventory.R.array.category_items)
        )
        binding.spCategory.adapter = adapter
        if (bookItem.category != null){
            var position = adapter.getPosition(bookItem.category.toString())
            binding.spCategory.setSelection(position)
        }
        else{
            binding.spCategory.setSelection(BookItemCategory.toInt(BookItemCategory.CRIME))
        }

        return AlertDialog.Builder(requireActivity())
            .setTitle(hu.bme.aut.android.bookinventory.R.string.edit_book_item)
            .setView(binding.root)
            .setPositiveButton(hu.bme.aut.android.bookinventory.R.string.ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onBookItemUpdated(updateBookItem())
                }
            }
            .setNegativeButton(hu.bme.aut.android.bookinventory.R.string.cancel, null)
            .create()
    }

    interface EditBookItemDialogListener {
        fun onBookItemUpdated(item: BookItem)
    }

    private fun isValid(): Boolean {
        return binding.etTitle.text.isNotEmpty()
    }

    private fun updateBookItem(): BookItem {
        bookItem.title = binding.etTitle.text.toString()
        bookItem.author = binding.etAuthor.text.toString()
        bookItem.year = binding.etYear.text.toString().toIntOrNull() ?: 0
        bookItem.category = BookItemCategory.getByOrdinal(binding.spCategory.selectedItemPosition) ?: BookItemCategory.CRIME
        bookItem.isRead = binding.cbAlreadyRead.isChecked
        return bookItem
    }
}