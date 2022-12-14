package com.glucozo.demo.adapter

import android.app.AlertDialog
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glucozo.demo.R
import com.glucozo.demo.evenbus.UpdateCartEvent
import com.glucozo.demo.model.CartModel
import com.google.firebase.database.FirebaseDatabase
import org.greenrobot.eventbus.EventBus

class MyCartAdapter(
    private val context: Context,
    private val cartModelList: List<CartModel>,
//    private val callback: BookItemClickListener
):RecyclerView.Adapter<MyCartAdapter.ViewHodel>() {
    class ViewHodel(itemView: View):RecyclerView.ViewHolder(itemView) {
        var btnMinus:ImageView? = null
        var btnPlus: ImageView? = null
        var imageView: ImageView? = null
        var btnDelete:ImageView? = null
        var txtName: TextView? = null
        var txtPrice: TextView? = null
        var txtQuantity: TextView? = null

        init {
            btnMinus = itemView.findViewById(R.id.btnMinus) as ImageView
            btnPlus = itemView.findViewById(R.id.btnPlus) as ImageView
            imageView = itemView.findViewById(R.id.imageView) as ImageView
            btnDelete = itemView.findViewById(R.id.btn_Delete) as ImageView
            txtName = itemView.findViewById(R.id.txtName) as TextView
            txtPrice = itemView.findViewById(R.id.txtPrice) as TextView
            txtQuantity = itemView.findViewById(R.id.txtQuantity) as TextView
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        return ViewHodel(LayoutInflater.from(context)
            .inflate(R.layout.cart_item,parent,false)

        )
    }

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        Glide.with(context)
            .load(cartModelList[position].image)
            .into(holder.imageView!!)
        holder.txtName!!.text = java.lang.StringBuilder().append(cartModelList[position].name)

        holder.txtPrice!!.text = java.lang.StringBuilder("$").append(cartModelList[position].price)
        holder.txtQuantity!!.text = java.lang.StringBuilder("").append(cartModelList[position].quantity)
    //event
        holder.btnMinus!!.setOnClickListener{e-> minusCartItem(holder,cartModelList[position])}
        holder.btnPlus!!.setOnClickListener{e-> plusCartItem(holder,cartModelList[position])}
        holder.btnDelete!!.setOnClickListener{e->
            val dialog = AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Do you really want to delete item")
                .setNegativeButton("CANCEL"){dialog,_ -> dialog.dismiss()}
                .setNegativeButton("DELETE"){dialog,_ ->

                   notifyItemRemoved(position)
                    FirebaseDatabase.getInstance()
                        .getReference("Cart")
                        .child("UNIQUE_USER_ID")
                        .child(cartModelList[position].key!!)
                        .removeValue()
                        .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }

                }
                .create()
            dialog.show()


        }
//        holder.itemView.setOnClickListener {
//            //báo cho wallpaper fragment vị trí(position) vừa được click
//            callback.onWallpaperItemClick(position)
//        }


    }

    private fun plusCartItem(holder: MyCartAdapter.ViewHodel, cartModel: CartModel) {
        cartModel.quantity += 1
        cartModel.totalPrice = cartModel.quantity *cartModel.price!!.toFloat()
        holder.txtQuantity!!.text = java.lang.StringBuilder("").append(cartModel.quantity)
        updateFirebase(cartModel)

    }

    private fun minusCartItem(holder: MyCartAdapter.ViewHodel, cartModel: CartModel) {
        if (cartModel.quantity > 1){
            cartModel.quantity -= 1
            cartModel.totalPrice = cartModel.quantity *cartModel.price!!.toFloat()
            holder.txtQuantity!!.text = java.lang.StringBuilder("").append(cartModel.quantity)
            updateFirebase(cartModel)

        }
    }


    private fun updateFirebase(cartModel: CartModel) {
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .child(cartModel.key!!)
            .setValue(cartModel)
            .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }
    }

    override fun getItemCount(): Int {
     return  cartModelList.size
    }
}