package ru.practicum.android.diploma.filters.industries.ui.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryCardBinding
import ru.practicum.android.diploma.filters.industries.domain.models.Industry

class IndustrySelectRecyclerViewAdapter(
    private val clickListener: IndustryClickListener
) : RecyclerView.Adapter<IndustriesViewHolder>() {

    var list = mutableListOf<Industry>()
    var lastSelect = 0
    private var previousList = mutableListOf<Industry>()
    private var previousRequest: String = ""
    private var selectedIndustry: Industry? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesViewHolder {
        val view = LayoutInflater.from(parent.context)
        return IndustriesViewHolder(IndustryCardBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: IndustriesViewHolder, position: Int) {
        val itemView = list[position]
        holder.bind(itemView)
        if (itemView.isChecked) selectedIndustry = itemView

        holder.itemView.setOnClickListener {
            val i = list.indexOf(selectedIndustry)
            if (i >= 0) {
                list[i] = list[i].copy(isChecked = false)
                selectedIndustry = itemView
            }

            clickListener.onClick(itemView)
            val j = list.indexOf(itemView)
            if (j >= 0) {
                list[j] = list[j].copy(isChecked = true)
            }
        }
    }

    fun filterResults(request: String) {
        if (request.isNotEmpty()
            && previousRequest.isNotEmpty()
            && previousRequest.contains(request)
        ) {
            list.clear()
            list.addAll(previousList)
        }

        val filteredList = list.filter { industry ->
            industry.name
                .lowercase()
                .contains(request)
        }

        if (filteredList.isNotEmpty()) {
            previousList.clear()
            previousList.addAll(list)
        }
        previousRequest = request

        list.clear()
        list.addAll(filteredList)
        notifyDataSetChanged()
    }

    fun interface IndustryClickListener {
        fun onClick(industry: Industry)
    }
}
