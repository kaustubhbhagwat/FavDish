package com.example.favdish.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.favdish.databinding.FragmentDishDetailsBinding


class DishDetailsFragment : Fragment() {

    lateinit var binding : FragmentDishDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDishDetailsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()
        args.let {
            Glide.with(this@DishDetailsFragment).load(it.StringDishDetails.image).into(binding.dishDetailsImageView)
            binding.dishTitle.text = it.StringDishDetails.title
            binding.dishType.text = it.StringDishDetails.type
            binding.dishIngredients.text = it.StringDishDetails.ingredients
            binding.dishDirectionsToCook.text = it.StringDishDetails.ditectionsToCook
            binding.cookingTime.text = it.StringDishDetails.cookingTime
        }
    }
}