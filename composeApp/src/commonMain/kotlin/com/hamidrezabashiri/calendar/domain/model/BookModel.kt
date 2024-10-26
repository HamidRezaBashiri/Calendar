package com.hamidrezabashiri.calendar.domain.model

data class BookModel(
    val id : Int= 0,
    val title: String= "title",
    val description: String = "description",
    val author: String= "author",
    val pages: String= "pages",
    val publisher: String= "publisher",
    val country: String= "country",
    val imageLink: String= "imageLink",
    val language: String= "language",
    val currentPage: String= "currentPage")