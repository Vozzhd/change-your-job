package ru.practicum.android.diploma.filters.ui.presenter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaCardBinding
import ru.practicum.android.diploma.filters.presentation.states.FilterItems

class FilterViewHolder(private val binding: AreaCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FilterItems) {
        when (item) {
            is FilterItems.Region -> binding.name.text = item.name
            else -> Unit
        }
    }
}
