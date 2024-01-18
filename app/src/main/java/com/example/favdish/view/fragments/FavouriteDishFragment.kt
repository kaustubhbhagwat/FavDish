package com.example.favdish.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.R
import com.example.favdish.appliction.FavDishApplication
import com.example.favdish.databinding.FragmentFavouriteDishBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.view.activites.MainActivity
import com.example.favdish.view.adapters.FavDishAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory


class FavouriteDishFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteDishBinding
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavList.layoutManager = GridLayoutManager(activity, 2)
        val favDishAdapter = FavDishAdapter(this@FavouriteDishFragment)
        binding.rvFavList.adapter = favDishAdapter

        mFavDishViewModel.favDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()) {
                    binding.rvFavList.visibility = View.VISIBLE
                    favDishAdapter.dishList(it)
                }
            }
        }
    }

    fun goToDishDetails(favDish: FavDish) {
        findNavController().navigate(
            FavouriteDishFragmentDirections.actionNavigationFavouriteDishToDishDetailsFragment(favDish)
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes,menu)
        super.onCreateOptionsMenu(menu, inflater)
        ActivityCompat.invalidateOptionsMenu(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)?.showBottomNavView()
    }
}