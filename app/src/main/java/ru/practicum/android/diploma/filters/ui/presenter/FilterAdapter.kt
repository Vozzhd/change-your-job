package ru.practicum.android.diploma.filters.ui.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaCardBinding
import ru.practicum.android.diploma.filters.presentation.states.FilterItems

class FilterAdapter(private val clickListener: FilterItemClickListern) : RecyclerView.Adapter<FilterViewHolder>() {

    val items = ArrayList<FilterItems>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AreaCardBinding.inflate(layoutInflater, parent, false)
        return FilterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val itemView = items[position]
        holder.bind(itemView)
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(itemView)
        }
    }
}

fun interface FilterItemClickListern {
    fun onItemClick(item: FilterItems)
}
