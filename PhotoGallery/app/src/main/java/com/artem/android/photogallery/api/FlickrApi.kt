package com.artem.android.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {
    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=40e62d5cf5af6f71c192c944b84db1d5" + "&format=json" + "&nojsoncallback=1" + "&extras=url_s")
    fun fetchPhotos(): Call<String>
}