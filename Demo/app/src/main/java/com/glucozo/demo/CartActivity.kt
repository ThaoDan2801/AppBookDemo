package com.glucozo.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.glucozo.demo.adapter.BookItemClickListener
import com.glucozo.demo.adapter.MyCartAdapter
import com.glucozo.demo.evenbus.UpdateCartEvent
import com.glucozo.demo.listener.ICartLoadListener
import com.glucozo.demo.listener.IDBookLoadListener
import com.glucozo.demo.model.CartModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_item.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CartActivity : AppCompatActivity(), ICartLoadListener{
    var cartLoadListener:ICartLoadListener? = null
//    lateinit var bookLoadListener: BookItemClickListener

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun opUpdateCartEvent(event: UpdateCartEvent) {
        loadCartFromFirbase()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        init()
        loadCartFromFirbase()
    }

    private fun loadCartFromFirbase() {
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children) {
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener!!.onLoadCartSuccess(cartModels)

                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onLoadCartFailed(error.message)
                }

            })
    }

    private fun init() {
        cartLoadListener = this
        val layoutManager = LinearLayoutManager(this)
        rc_cart!!.layoutManager = layoutManager
        rc_cart!!.addItemDecoration(DividerItemDecoration(this,layoutManager.orientation))
        btnBack!!.setOnClickListener{
            finish()
        }
//        cart_item.setOnClickListener{
//
//        }

    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var sum = 0.0
        for (cartModel in cartModelList!!){
            sum += cartModel!!.totalPrice
        }
        txtTotal.text = StringBuilder("Total price :$").append(sum)
        val adapter = MyCartAdapter(this,cartModelList)
        rc_cart!!.adapter = adapter
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout,message!!,Snackbar.LENGTH_LONG).show()
    }


}