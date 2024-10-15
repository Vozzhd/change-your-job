package ru.practicum.android.diploma.filters.presentation.states

sealed class FilterItems {
    data class Industy(val id: String, val name: String) : FilterItems()
    data class Region(val id: String, val name: String) : FilterItems()
}
