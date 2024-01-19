package com.example.favdish.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.databinding.DishItemLayoutBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.view.fragments.AllDishesFragment
import com.example.favdish.view.fragments.FavouriteDishFragment

class FavDishAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: DishItemLayoutBinding =
            DishItemLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment).load(dish.image).into(holder.ivDishImage)
        holder.ivDishTitle.text = dish.title
        holder.ivDishIngredients.text = dish.ingredients

        holder.itemView.setOnClickListener {
            when (fragment) {
                is AllDishesFragment -> {
                    fragment.goToDishDetails(dish)
                }

                is FavouriteDishFragment -> {
                    fragment.goToDishDetails(dish)
                }
            }
        }
//        holder.ibMore.setOnClickListener {
//            val popUp = PopupMenu(fragment.context, holder.ibMore)
//            val inflater: MenuInflater = popUp.menuInflater
//            inflater.inflate(R.menu.menu_adapter, popUp.menu)
//
//            popUp.setOnMenuItemClickListener {
//                if (it.itemId == R.id.editDish) {
//                    Log.i("POPUP", "Edit was clicked")
//                } else if (it.itemId == R.id.deleteDish) {
//                    Log.d("POPUP", "Delete was clicked")
//                }
//                true
//            }
//        }

    }

    fun dishList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: DishItemLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.dishImage
        val ivDishTitle = view.dishTitle
        val ivDishIngredients = view.dishIngredients
        val ibMore = view.more
    }

}