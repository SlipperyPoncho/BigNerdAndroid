package com.artem.android.mylibrary

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class BookFragment: Fragment() {

    private lateinit var book: Book
    private lateinit var titleField: EditText
    private lateinit var authorField: EditText
    private lateinit var dateButton: Button
    private lateinit var readCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = Book()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book, container, false)
        titleField = view.findViewById(R.id.book_title) as EditText
        authorField = view.findViewById(R.id.book_author) as EditText
        dateButton = view.findViewById(R.id.publication_date) as Button
        readCheckBox = view.findViewById(R.id.book_read) as CheckBox
        dateButton.apply {
            text = book.date.toString()
            isEnabled = false
        }
        return view
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher
        {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                book.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {

            }
        }

        val authorWatcher = object : TextWatcher
        {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                book.author = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)
        authorField.addTextChangedListener(authorWatcher)

        readCheckBox.apply {
            setOnCheckedChangeListener {
                    _,
                    isRead -> book.isRead = isRead
            }
        }
    }
}