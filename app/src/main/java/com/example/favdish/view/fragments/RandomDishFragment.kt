package com.example.favdish.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.favdish.databinding.FragmentRandomDishBinding
import com.example.favdish.model.entities.RandomDish
import com.example.favdish.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {
    private var _binding: FragmentRandomDishBinding? = null

    private lateinit var mRandomDishViewModel: RandomDishViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this)[RandomDishViewModel::class.java]
        mRandomDishViewModel.getRandomDishFromApi()
        randomDishViewModelObserver()
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(
            viewLifecycleOwner
        ) { randomDishResponse ->
            randomDishResponse?.let {
                setResponseToUI(randomDishResponse.recipes[0])
            }
        }
        mRandomDishViewModel.randomDishLoadingError.observe(
            viewLifecycleOwner
        ) { dataError ->
            dataError.let {
                Log.i("Data Error", "$dataError")
            }
        }

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner) { loadRandomDish ->
            loadRandomDish.let {
                Log.i("Load Random DIsh", "$loadRandomDish")
            }
        }
    }

    private fun setResponseToUI(recipe: RandomDish.Recipe){
        var ingredients = ""

        Glide.with(requireActivity())
            .load(recipe.image)
            .into(binding.dishDetailsImageView)

        for(value in recipe.extendedIngredients){

            if(ingredients.isEmpty()){
                ingredients = value.original
            }else{
                ingredients = ingredients +",\n" + value.original
            }
        }


        binding.dishTitle.text = recipe.title
        binding.dishType.text = recipe.dishTypes[0]
        binding.dishDirectionsToCook.text = recipe.instructions
        binding.dishIngredients.text = ingredients

        binding.cookingTime.text = recipe.readyInMinutes.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}