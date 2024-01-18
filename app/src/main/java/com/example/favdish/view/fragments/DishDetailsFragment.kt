package com.example.favdish.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.R
import com.example.favdish.appliction.FavDishApplication
import com.example.favdish.databinding.FragmentDishDetailsBinding
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DishDetailsFragment : Fragment() {

    lateinit var binding: FragmentDishDetailsBinding
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDishDetailsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()
        args.let {
            Glide.with(this@DishDetailsFragment).load(it.dishDetails.image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("TAG", "Error loading image")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource.toBitmap().let { it1 ->
                            Palette.from(it1).generate { palette ->
                                val intColor = palette?.vibrantSwatch?.rgb ?: 0
                                binding.dishDetailsParentLayout.setBackgroundColor(intColor)
                            }
                        }
                        return false
                    }

                })
                .into(binding.dishDetailsImageView)

            binding.dishTitle.text = it.dishDetails.title
            binding.dishType.text = it.dishDetails.type
            binding.dishIngredients.text = it.dishDetails.ingredients
            binding.dishDirectionsToCook.text = it.dishDetails.ditectionsToCook
            binding.cookingTime.text = it.dishDetails.cookingTime
        }

        if (args.dishDetails.favouriteDish) {
            binding.favDish.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favorite_selected
                )
            )
            Toast.makeText(requireActivity(), "Added to favourite dish", Toast.LENGTH_SHORT).show()
        } else {
            binding.favDish.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favorite_unselected
                )
            )
            Toast.makeText(requireActivity(), "Removed from favourite dish", Toast.LENGTH_SHORT)
                .show()

        }

        binding.favDish.setOnClickListener {
            args.dishDetails.favouriteDish = !args.dishDetails.favouriteDish

            lifecycleScope.launch(Dispatchers.IO) {
                mFavDishViewModel.update(args.dishDetails)
            }
        }
    }
}