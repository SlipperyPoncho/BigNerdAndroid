package com.artem.android.mylibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.util.Date
import java.util.UUID

private const val TAG = "BookFragment"
private const val ARG_BOOK_ID = "book_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_CONTACT = 1
private const val REQUEST_PHOTO = 2
private const val DATE_FORMAT = "EEE, MMM, dd"

class BookFragment: Fragment(), DatePickerFragment.Callbacks {

    private lateinit var book: Book
    private lateinit var titleField: EditText
    private lateinit var authorField: EditText
    private lateinit var dateButton: Button
    private lateinit var readCheckBox: CheckBox
    private lateinit var reviewButton: Button
    private lateinit var ratingButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private val bookDetailViewModel: BookDetailViewModel by lazy {
        ViewModelProvider(this)[BookDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = Book()
        val bookId: UUID = arguments?.getSerializable(ARG_BOOK_ID) as UUID
        bookDetailViewModel.loadBook(bookId)
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
        reviewButton = view.findViewById(R.id.book_review) as Button
        ratingButton = view.findViewById(R.id.book_rating) as Button
        photoButton = view.findViewById(R.id.book_camera) as ImageButton
        photoView = view.findViewById(R.id.book_photo) as ImageView

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookDetailViewModel.bookLiveData.observe(
            viewLifecycleOwner,
            Observer {
                book -> book?.let {
                    this.book = book
                    photoFile = bookDetailViewModel.getPhotoFile(book)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                        "com.artem.android.mylibrary.fileprovider",
                                photoFile)
                    updateUI()
                }
            }
        )
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

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(book.date).apply {
                setTargetFragment(this@BookFragment, REQUEST_DATE)
                show(this@BookFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        reviewButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getBookReview())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.book_review_subject))
            }.also { intent -> startActivity(intent) }
        }

        photoView.setOnClickListener {
            val zoomDialog = PictureZoomFragment.newInstance(book.photoFileName)
            fragmentManager?.let { it1 -> zoomDialog.show(it1, null) }
        }

        ratingButton.apply {
            val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
        }

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            setOnClickListener{
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(
                    captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        bookDetailViewModel.saveBook(book)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    override fun onDateSelected(date: Date) {
        book.date = date
        updateUI()
    }

    private fun updateUI() {
        titleField.setText(book.title)
        authorField.setText(book.author)
        dateButton.text = book.date.toString()
        readCheckBox.apply {
            isChecked = book.isRead
            jumpDrawablesToCurrentState()
        }
        if (book.rating.isNotEmpty()) {
            ratingButton.text = book.rating
        }
        updatePhotoView()
    }

    private fun updatePhotoView() {
        val pictureUtils = PictureUtils()
        if (photoFile.exists()) {
            val bitmap = pictureUtils.getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null -> {
                val contactUri: Uri? = data.data
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = contactUri?.let {
                    requireActivity().contentResolver.query(
                        it,
                        queryFields,
                        null,
                        null,
                        null
                    )
                }
                cursor?.use {
                    if (it.count == 0) { return }
                    it.moveToFirst()
                    val rating = it.getString(0)
                    book.rating = rating
                    bookDetailViewModel.saveBook(book)
                    ratingButton.text = rating
                }
            }
            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
    }

    // Strings are just random maybe improve it later
    @SuppressLint("StringFormatInvalid")
    private fun getBookReview(): String {
        val reviewedString = if (book.isRead) {
            getString(R.string.book_reviewed)
        } else {
            getString(R.string.book_not_reviewed)
        }
        val dateString = DateFormat.format(DATE_FORMAT, book.date).toString()
        var rating = if (book.rating.isBlank()) {
            getString(R.string.book_no_rating)
        } else {
            getString(R.string.book_rating, book.rating)
        }
        return getString(R.string.book_review, book.title, dateString, reviewedString, rating)
    }

    companion object {
        fun newInstance(bookId: UUID): BookFragment {
            val args = Bundle().apply { putSerializable(ARG_BOOK_ID, bookId) }
            return BookFragment().apply { arguments = args }
        }
    }
}