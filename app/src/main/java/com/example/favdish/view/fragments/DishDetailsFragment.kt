package com.example.favdish.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.databinding.FragmentDishDetailsBinding


class DishDetailsFragment : Fragment() {

    lateinit var binding: FragmentDishDetailsBinding
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
            Glide.with(this@DishDetailsFragment).load(it.StringDishDetails.image)
                .listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("TAG","Error loading image")
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
                            Palette.from(it1).generate{ palette->
                                val intColor = palette?.vibrantSwatch?.rgb ?: 0
                                binding.dishDetailsParentLayout.setBackgroundColor(intColor)
                            }
                        }
                        return false
                    }

                })
                .into(binding.dishDetailsImageView)

            binding.dishTitle.text = it.StringDishDetails.title
            binding.dishType.text = it.StringDishDetails.type
            binding.dishIngredients.text = it.StringDishDetails.ingredients
            binding.dishDirectionsToCook.text = it.StringDishDetails.ditectionsToCook
            binding.cookingTime.text = it.StringDishDetails.cookingTime
        }
    }
}