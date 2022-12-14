package com.glucozo.demo.listener

import com.glucozo.demo.model.BookModel


interface IDBookLoadListener {
    fun onBookLoadSuccess(bookModelList:List<BookModel>?)
    fun onBookLoadFailed(message:String?)
}