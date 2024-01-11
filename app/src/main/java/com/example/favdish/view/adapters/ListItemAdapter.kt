package com.example.favdish.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favdish.databinding.ItemCustomListBinding
import com.example.favdish.view.activites.AddUpdateFavDishActivity

class ListItemAdapter(
    private val activity: Activity,
    private val list: List<String>, private val selection: String
) : RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding =
            ItemCustomListBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvText.text = item
        holder.tvText.setOnClickListener{
            if(activity is AddUpdateFavDishActivity){
                activity.selectedListItem(item,selection)
            }
        }
    }

    class ViewHolder(view: ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
            val tvText = view.tvText
    }
}