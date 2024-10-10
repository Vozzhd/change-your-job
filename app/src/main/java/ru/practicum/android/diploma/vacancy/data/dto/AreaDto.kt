package ru.practicum.android.diploma.vacancy.data.dto

data class AreaDto(
    val id: String,
    val name: String,
    val parentId: String?,
    val areas: List<AreaDto>?
)
