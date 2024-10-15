package ru.practicum.android.diploma.filters.domain.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filters.domain.api.FilterAreaInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterAreaRepository
import ru.practicum.android.diploma.filters.domain.models.Area
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.network.HttpStatusCode

class FilterAreaInteractorImpl(
    private val repository: FilterAreaRepository
) : FilterAreaInteractor {

    override fun getCountries(): Flow<Pair<List<Area>?, HttpStatusCode?>> {
        return repository.getAreas().map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, HttpStatusCode.OK)
                }
                is Resource.Error -> {
                    Pair(null, result.httpStatusCode)
                }
            }
        }
    }
}
