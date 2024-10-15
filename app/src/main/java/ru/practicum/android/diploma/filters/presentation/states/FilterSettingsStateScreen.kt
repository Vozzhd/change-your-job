package ru.practicum.android.diploma.filters.presentation.states

sealed class FilterSettingsStateScreen {
    data class FilterSettings(
        val area: String,
        val industry: String,
        val salary: String,
        val showWithSalary: Boolean
    )
}
