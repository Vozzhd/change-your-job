package ru.practicum.android.diploma.filters.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.api.FilterAreaInteractor
import ru.practicum.android.diploma.filters.domain.models.Area
import ru.practicum.android.diploma.filters.presentation.states.FilterItems
import ru.practicum.android.diploma.filters.presentation.states.FiltersChooserScreenState
import ru.practicum.android.diploma.util.network.HttpStatusCode

class CityViewModel(
    //private val countryId : String?,
    private val filterInteractor: FilterAreaInteractor
) : ViewModel() {


    val countryId = "40" //Тестовая переменная

    private val stateLiveData = MutableLiveData<FiltersChooserScreenState>()
    fun observeState(): LiveData<FiltersChooserScreenState> = stateLiveData

    init {
        getCountry()
    }


    private fun renderState(state: FiltersChooserScreenState) {
        stateLiveData.postValue(state)
    }

    private fun getCountry() {
        viewModelScope.launch {
            filterInteractor
                .getCountries()
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }


    private fun processResult(foundCountries: List<Area>?, errorMessage: HttpStatusCode?) {

        when {
            errorMessage == HttpStatusCode.NOT_CONNECTED -> {
                renderState(FiltersChooserScreenState.Error)
            }

            foundCountries.isNullOrEmpty() -> {
                renderState(FiltersChooserScreenState.Empty)
            }


            else -> {

                val specificRegions =
                    getRegionsByParentId(foundCountries, countryId)
                renderState(FiltersChooserScreenState.ChooseItem(specificRegions))
            }
        }
    }

    private fun getRegionsByParentId(areas: List<Area>, parentId: String?): List<FilterItems.Region> {
        val allRegions = mutableListOf<Area>()

        fun collectRegions(currentAreas: List<Area>) {
            for (area in currentAreas) {
                allRegions.add(area)
                if (area.areas.isNotEmpty()) {
                    collectRegions(area.areas)
                }
            }
        }

        collectRegions(areas)
        Log.d("Tag", "${allRegions}")

        return when {
            parentId.isNullOrEmpty() -> {
                allRegions
                    .filter { it.areas.isEmpty() && it.parentId != null }
                    .map { FilterItems.Region(it.id, it.name) }
                    .distinctBy { it.id }
                    .sortedBy { it.name }
            }

            else -> {

                allRegions
                    .filter { it.parentId == parentId }
                    .flatMap { area ->
                        listOf(FilterItems.Region(area.id, area.name)) +
                            area.areas.map { FilterItems.Region(it.id, it.name) }
                    }
                    .distinctBy { it.id }
                    .sortedBy { it.name }
            }
        }
    }
}

