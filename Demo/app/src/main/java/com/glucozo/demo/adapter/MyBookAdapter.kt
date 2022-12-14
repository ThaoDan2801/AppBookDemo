package com.glucozo.demo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glucozo.demo.R
import com.glucozo.demo.evenbus.UpdateCartEvent
import com.glucozo.demo.listener.ICartLoadListener
import com.glucozo.demo.listener.IRecylerClickListener
import com.glucozo.demo.model.BookModel
import com.glucozo.demo.model.CartModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus

class MyBookAdapter(
    private val context: Context,
    private val list: List<BookModel>,
    private val cartListener: ICartLoadListener
//    private val callback:BookItemClickListener

):RecyclerView.Adapter<MyBookAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
     var imageView: ImageView? = null
     var txtName: TextView? = null
     var txtPrice: TextView? = null
        

        private var clickListener:IRecylerClickListener? = null

        fun setClickListener(clickListener: IRecylerClickListener){
            this.clickListener = clickListener
        }
    init {
        imageView = itemView.findViewById(R.id.imageView) as ImageView
        txtName = itemView.findViewById(R.id.txtName) as TextView
        txtPrice = itemView.findViewById(R.id.txtPrice) as TextView

        itemView.setOnClickListener(this)
    }

        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v,adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context)
           .inflate(R.layout.book_item,parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position].image)
            .into(holder.imageView!!)
        holder.txtName!!.text = java.lang.StringBuilder().append(list[position].name)
        holder.txtPrice!!.text = java.lang.StringBuilder("$").append(list[position].price)

        holder.setClickListener(object : IRecylerClickListener{
            override fun onItemClickListener(view: View?, position: Int) {
                addToCart(list[position])
            }
        })
//        holder.itemView.setOnClickListener {
            //báo cho wallpaper fragment vị trí(position) vừa được click
//            callback.onWallpaperItemClick(position)
//        }
    }

    private fun addToCart(bookModel: BookModel) {
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
        userCart.child(bookModel.key!!)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val carModel = snapshot.getValue(CartModel::class.java)
                        val updateData:MutableMap<String,Any> = HashMap()
                        carModel!!.quantity = carModel!!.quantity+1
                        updateData["quantity"] = carModel!!.quantity
                        updateData["totalPrice"] = carModel!!.quantity*carModel.price!!.toFloat()

                         userCart.child(bookModel.key!!)
                             .updateChildren(updateData)
                             .addOnSuccessListener {
                                 EventBus.getDefault().postSticky(UpdateCartEvent())
                                 cartListener.onLoadCartFailed("Success add to cart")
                             }
                             .addOnFailureListener{e -> cartListener.onLoadCartFailed(e.message)}

                    }
                    else{
                        val cartModel = CartModel()
                        cartModel.key = bookModel.key
                        cartModel.name = bookModel.name
                        cartModel.image = bookModel.image
                        cartModel.price = bookModel.price
                        cartModel.quantity=1
                        cartModel.totalPrice = bookModel.price!!.toFloat()

                        userCart.child(bookModel.key!!)
                            .setValue(cartModel)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                            cartListener.onLoadCartFailed("Success add to cart")
                        }
                            .addOnFailureListener{e -> cartListener.onLoadCartFailed(e.message)}

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    cartListener.onLoadCartFailed(error.message)
                }

            })
    }

    override fun getItemCount(): Int {
      return  list.size
    }
}



//
//class MyBookAdapter(
//    private val list: List<BookModel>
//)
//    : RecyclerView.Adapter<MyBookAdapter.ViewHolder>(){
//    class ViewHolder(private val binding: BookItemBinding ):
//    RecyclerView.ViewHolder(binding.root) {
//        fun bind(bookModel: BookModel){
//            binding.txtName.text = bookModel.name
//            binding.txtPrice.text = bookModel.price
//            binding.txtPrice!!.text =java.lang.StringBuilder("$").append(1)
//            Glide.with(binding.imageView)
//                .load(bookModel.image)
//                .into(binding.imageView)
//
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            BookItemBinding.inflate(
//                LayoutInflater.from(parent.context), parent, false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        list[position].let {
//            holder.bind(it)
//        }
//    }
//
//    override fun getItemCount() = list.size
//}
