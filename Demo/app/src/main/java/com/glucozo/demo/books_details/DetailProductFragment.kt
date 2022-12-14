package com.glucozo.book_market.books_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.glucozo.demo.databinding.FragmentWallpaperDetailBinding
import com.glucozo.demo.model.BookModel

class DetailProductFragment : Fragment() {
    private lateinit var binding: FragmentWallpaperDetailBinding
    private lateinit var books: BookModel

    companion object {
        fun newInstance(book: BookModel): DetailProductFragment {
            val fragment = DetailProductFragment()
            fragment.books = book
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWallpaperDetailBinding.inflate(inflater, container, false)
        binding.tvBook.text = books.name.toString()
//        binding.tvTacgia.text = books.price
        Glide.with(binding.imgBook)
            .load(books.image)
            .into(binding.imgBook)
//        binding.btnSetAsWallpaper.setOnClickListener {
//            Thread {
//                WallpaperManager.getInstance(requireContext()).setResource(wallpaper.imageId)
//            }.start()
//        }
        return binding.root
    }

}