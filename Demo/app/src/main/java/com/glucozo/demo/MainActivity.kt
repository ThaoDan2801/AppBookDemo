package com.glucozo.demo

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.glucozo.demo.adapter.MyBookAdapter
import com.glucozo.demo.databinding.ActivityMainBinding
import com.glucozo.demo.evenbus.UpdateCartEvent
import com.glucozo.demo.listener.ICartLoadListener
import com.glucozo.demo.listener.IDBookLoadListener
import com.glucozo.demo.model.BookModel
import com.glucozo.demo.model.CartModel
import com.glucozo.mang.util.SpaceItemDEcoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), IDBookLoadListener, ICartLoadListener {
    lateinit var binding: ActivityMainBinding
    lateinit var bookLoadListener: IDBookLoadListener
    lateinit var cartLoadListener: ICartLoadListener
    private val books = arrayListOf<BookModel>()
    private lateinit var adapter: MyBookAdapter

//    private val currentFragment
//        get() = supportFragmentManager.findFragmentById(R.id.content)

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
        countCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        evenAdd()
        loadBookFromFirebase()
        countCartFromFirebase()

//        if (currentFragment == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.content, WallpaperFragment())
//                .commit()
//        }
    }

    private fun countCartFromFirebase() {
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
                    cartLoadListener.onLoadCartSuccess(cartModels)

                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }

            })
    }

    private fun loadBookFromFirebase() {
        val bookModels: MutableList<BookModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Books")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (bookSnapshot in snapshot.children) {
                            val bookModel = bookSnapshot.getValue(BookModel::class.java)
                            bookModel!!.key = bookSnapshot.key
                            bookModels.add(bookModel)
                        }
                        bookLoadListener.onBookLoadSuccess(bookModels)

                    } else {
                        bookLoadListener.onBookLoadFailed("Book items not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    bookLoadListener.onBookLoadFailed(error.message)
                }

            })
    }

    private fun evenAdd() {
        bookLoadListener = this
        cartLoadListener = this
        val gridLayoutManager = GridLayoutManager(this, 2)
        rv_book.layoutManager = gridLayoutManager
        rv_book.addItemDecoration(SpaceItemDEcoration())

        btn_cart.setOnClickListener{
            startActivity(Intent(this,CartActivity::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBookLoadSuccess(bookModelList: List<BookModel>?) {
//        val adapter = MyBookAdapter
//        val adapter = bookModelList?.let { MyBookAdapter(this, it, cartLoadListener) }
        val adapter = MyBookAdapter(this,bookModelList!!,cartLoadListener)
        rv_book.adapter = adapter
    }


    override fun onBookLoadFailed(message: String?) {
        Snackbar.make(mainLayout, message!!, Snackbar.LENGTH_LONG).show()

    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for (cartModel in cartModelList!!) {
            cartSum += cartModel!!.quantity
            badge!!.setNumber(cartSum)
        }

    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout, message!!, Snackbar.LENGTH_LONG).show()

    }


//
}