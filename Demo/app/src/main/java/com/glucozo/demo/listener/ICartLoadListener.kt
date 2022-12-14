package com.glucozo.demo.listener

import com.glucozo.demo.model.CartModel

interface ICartLoadListener {


    fun onLoadCartSuccess(cartModelList: List<CartModel>)
    fun onLoadCartFailed(message:String?)
}