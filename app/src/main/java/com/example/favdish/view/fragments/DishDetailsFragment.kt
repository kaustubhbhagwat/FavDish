package com.example.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.appliction.FavDishApplication
import com.example.favdish.databinding.FragmentDishDetailsBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DishDetailsFragment : Fragment() {
    private var mFavDishDetails: FavDish? = null

    private lateinit var binding: FragmentDishDetailsBinding
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share_dish -> {

                val type = "text/plain"
                val subject = "Checkout this recipe"
                var extraText = ""
                val shareWith = "Share with"
                mFavDishDetails?.let {
                    var image = ""
                    if (it.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE) {
                        image = it.image
                    }
                    var cookingInstructions = ""
                    cookingInstructions = Html.fromHtml(it.ditectionsToCook).toString()
                    extraText = "$image \n" + "Title: ${it.title} \n \n" + "Type :${it.type} \n \n " +
                                "Category:${it.category}\n"+ "Instructions: ${it.ditectionsToCook}\n"
                }
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT,subject)
                intent.putExtra(Intent.EXTRA_TEXT,extraText)
                startActivity(Intent.createChooser(intent,shareWith))
            }
        }
        return super.onOptionsItemSelected(item)
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
        mFavDishDetails = args.dishDetails
        args.let {
            if (args.dishDetails.favouriteDish) {
                binding.favDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
            } else {
                binding.favDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )

            }
            Glide.with(this@DishDetailsFragment).load(it.dishDetails.image)
                .into(binding.dishDetailsImageView)
//                .listener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Drawable>,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        Log.e("TAG", "Error loading image")
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable,
//                        model: Any,
//                        target: Target<Drawable>?,
//                        dataSource: DataSource,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        resource.toBitmap().let { it1 ->
//                            Palette.from(it1).generate { palette ->
//                                val intColor = palette?.vibrantSwatch?.rgb ?: 0
//                                binding.dishDetailsParentLayout.setBackgroundColor(intColor)
//                            }
//                        }
//                        return false
//                    }
//
//                }
//        )

            binding.dishTitle.text = it.dishDetails.title
            binding.dishType.text = it.dishDetails.type
            binding.dishIngredients.text = Html.fromHtml(it.dishDetails.ingredients)
            binding.dishDirectionsToCook.text = it.dishDetails.ditectionsToCook
            binding.cookingTime.text = it.dishDetails.cookingTime

        }

        binding.favDish.setOnClickListener {
            args.dishDetails.favouriteDish = !args.dishDetails.favouriteDish
            lifecycleScope.launch(Dispatchers.IO) {
                mFavDishViewModel.update(args.dishDetails)
            }
            if (args.dishDetails.favouriteDish) {
                binding.favDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
                Toast.makeText(requireActivity(), "Added to favourite dish", Toast.LENGTH_SHORT)
                    .show()
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
        }
    }
}