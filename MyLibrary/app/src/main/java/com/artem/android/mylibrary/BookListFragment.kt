package com.artem.android.mylibrary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.UUID

private const val TAG = "BookListFragment"

class BookListFragment : Fragment() {
    interface Callbacks { fun onBookSelected(bookId: UUID) }

    private var callbacks: Callbacks? = null
    private lateinit var bookRecyclerView: RecyclerView
    private var adapter: BookAdapter? = BookAdapter(emptyList())

    private val bookListViewModel: BookListViewModel by lazy {
        ViewModelProvider(this)[BookListViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)
        bookRecyclerView = view.findViewById(R.id.book_recycler_view) as RecyclerView
        bookRecyclerView.layoutManager = LinearLayoutManager(context)
        bookRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookListViewModel.booksListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                books -> books?.let {
                    Log.i(TAG, "Got books ${books.size}")
                    updateUI(books)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_book_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_book -> {
                val book = Book()
                bookListViewModel.addBook(book)
                callbacks?.onBookSelected(book.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(books: List<Book>) {
        adapter = BookAdapter(books)
        bookRecyclerView.adapter = adapter
    }

    private inner class BookHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var book: Book
        private val titleTextView: TextView = itemView.findViewById(R.id.book_title)
        private val authorTextView: TextView = itemView.findViewById(R.id.book_author)
        private val readCheckBox: CheckBox = itemView.findViewById(R.id.readCheckBox)

        init { itemView.setOnClickListener(this) }

        fun bind(book: Book) {
            this.book = book
            titleTextView.text = this.book.title
            authorTextView.text = this.book.author
            readCheckBox.isChecked = book.isRead
        }

        override fun onClick(v: View) {
            callbacks?.onBookSelected(book.id)
        }
    }

    private inner class BookAdapter(var books: List<Book>): RecyclerView.Adapter<BookHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
            val view = layoutInflater.inflate(R.layout.list_item_book, parent, false)
            return BookHolder(view)
        }

        override fun getItemCount() = books.size

        override fun onBindViewHolder(holder: BookHolder, position: Int) {
            val book = books[position]
            holder.bind(book)
        }
    }

    companion object {
        fun newInstance(): BookListFragment {
            return BookListFragment()
        }
    }
}
