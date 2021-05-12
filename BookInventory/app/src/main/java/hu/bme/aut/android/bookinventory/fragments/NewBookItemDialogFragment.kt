package hu.bme.aut.android.bookinventory.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import hu.bme.aut.android.bookinventory.R
import hu.bme.aut.android.bookinventory.data.BookItem
import hu.bme.aut.android.bookinventory.data.BookItemCategory
import hu.bme.aut.android.bookinventory.databinding.DialogNewBookItemBinding

class NewBookItemDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "NewBookItemDialogFragment"
    }

    private lateinit var listener: NewBookItemDialogListener

    private var _binding: DialogNewBookItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity: FragmentActivity = getActivity()!!
        listener = if (activity is NewBookItemDialogListener) {
            activity
        } else {
            throw RuntimeException("Activity must implement the NewBookItemDialogListener interface!")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogNewBookItemBinding.inflate(LayoutInflater.from(context))

        binding.spCategory.adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

        return AlertDialog.Builder(requireActivity())
            .setTitle(R.string.new_book_item)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onBookItemCreated(getBookItem())
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    interface NewBookItemDialogListener {
        fun onBookItemCreated(item: BookItem)
    }

    private fun isValid(): Boolean {
        return binding.etTitle.text.isNotEmpty()
    }

    private fun getBookItem(): BookItem {
        return BookItem(
            title = binding.etTitle.text.toString(),
            author = binding.etAuthor.text.toString(),
            year = binding.etYear.text.toString().toIntOrNull() ?: 0,
            category = BookItemCategory.getByOrdinal(binding.spCategory.selectedItemPosition)
                ?: BookItemCategory.CRIME,
            isRead = binding.cbAlreadyRead.isChecked
        )
    }
}