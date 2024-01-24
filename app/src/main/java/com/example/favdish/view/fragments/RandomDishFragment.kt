package com.example.favdish.view.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.appliction.FavDishApplication
import com.example.favdish.databinding.FragmentRandomDishBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.model.entities.RandomDish
import com.example.favdish.utils.Constants
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
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
        binding.dishDetailsParentLayout.visibility = View.GONE
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

    private fun setResponseToUI(recipe: RandomDish.Recipe) {
        var ingredients = ""

        Glide.with(requireActivity())
            .load(recipe.image)
            .into(binding.dishDetailsImageView)

        for (value in recipe.extendedIngredients) {

            ingredients = if (ingredients.isEmpty()) {
                value.original
            } else {
                ingredients + ",\n" + value.original
            }
        }
        binding.dishTitle.text = recipe.title
        binding.dishType.text = recipe.dishTypes[0]
        binding.dishDirectionsToCook.text = Html.fromHtml(recipe.instructions)
        binding.dishIngredients.text = ingredients
        binding.cookingTime.text = recipe.readyInMinutes.toString()
        binding.dishDetailsParentLayout.visibility = View.VISIBLE
        binding.dishDetailsImageView.setOnClickListener {
            val randomFavDish = FavDish(
                recipe.image,
                Constants.DISH_IMAGE_SOURCE_ONLINE,
                recipe.title,
                recipe.dishTypes[0],
                "OTHER",
                ingredients,
                recipe.readyInMinutes.toString(),
                recipe.instructions,
                true
            )
            val mFavDishViewModel: FavDishViewModel by viewModels {
                FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
            }
            mFavDishViewModel.insert(randomFavDish)
            binding.favDish.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favorite_selected
                )
            )
            Toast.makeText(requireActivity(),"Dish Inserted successfully",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}