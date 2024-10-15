package ru.practicum.android.diploma.filters.presentation.states

sealed class FiltersChooserScreenState {
    data class ChooseItem(val items: List<FilterItems>)  : FiltersChooserScreenState()
    data object Error : FiltersChooserScreenState()
    data object Empty : FiltersChooserScreenState()
}
