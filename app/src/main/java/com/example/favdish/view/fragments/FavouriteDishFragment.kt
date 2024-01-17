package com.example.favdish.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.favdish.R
import com.example.favdish.databinding.FragmentFavouriteDishBinding


class FavouriteDishFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteDishBinding.inflate(layoutInflater)
        return binding.root
    }
}