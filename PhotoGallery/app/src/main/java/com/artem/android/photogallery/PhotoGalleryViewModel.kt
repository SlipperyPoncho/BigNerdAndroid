package com.artem.android.photogallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {

    val galleryItemLiveData: LiveData<List<GalleryItem>>

    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchItem = MutableLiveData<String>()

    val searchTerm: String get() = mutableSearchItem.value ?: ""

    init {
        mutableSearchItem.value = QueryPreferences.getStoredQuery(app)
        galleryItemLiveData = mutableSearchItem.switchMap {
            searchItem -> if (searchItem.isBlank()) {
                flickrFetchr.fetchPhotos()
            } else {
                flickrFetchr.searchPhotos(searchItem)
            }
        }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchItem.value = query
    }
}