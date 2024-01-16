package com.example.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.R
import com.example.favdish.appliction.FavDishApplication
import com.example.favdish.databinding.FragmentAllDishesBinding
import com.example.favdish.view.activites.AddUpdateFavDishActivity
import com.example.favdish.view.adapters.FavDishAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import com.example.favdish.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

    private lateinit var binding: FragmentAllDishesBinding
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAllDishesBinding.inflate(layoutInflater)

        binding.rvList.layoutManager = GridLayoutManager(activity, 2)
        val favDishAdapater = FavDishAdapter(this@AllDishesFragment)
        binding.rvList.adapter = favDishAdapater


        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes ->
            dishes.let {
               if(it.isNotEmpty()){
                   binding.rvList.visibility = View.VISIBLE
                   binding.textNoDish.visibility = View.GONE
                   favDishAdapater.dishList(it)
               }else{
                   binding.textNoDish.visibility = View.GONE
                   binding.rvList.visibility = View.VISIBLE
               }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes,menu)
        super.onCreateOptionsMenu(menu, inflater)
        invalidateOptionsMenu(requireActivity())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.action_add_dish ->
               startActivity(Intent(requireActivity(),AddUpdateFavDishActivity::class.java))
       }
        return super.onOptionsItemSelected(item)
    }
}