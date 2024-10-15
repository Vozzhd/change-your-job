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
    //private val countryId : String,
    private val filterInteractor: FilterAreaInteractor
) : ViewModel() {


    val countryId = "113"
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
        val specificRegions = mutableListOf<Area>()
        val finalRegions = mutableListOf<Area>()
        val allRegions = specificRegions + finalRegions
        fun collectRegions(currentAreas: List<Area>) {
            for (area in currentAreas) {
                // Если parentId равен null, собираем все регионы, кроме стран (страны не имеют parentId)
                if (parentId != null) {
                    if (area.parentId != null) {
                        specificRegions.add(area)
                    }
                }
                // Если parentId передан, ищем регионы с этим parentId
                else if (area.parentId == parentId) {
                    specificRegions.add(area)
                }
                // Рекурсивно продолжаем собирать вложенные регионы
                collectRegions(area.areas)
            }
        }
        fun collectFinish(currentAreas: List<Area>) {
            for (area in currentAreas) {
                    if (area.areas.isEmpty()) {
                        finalRegions.add(area)
                    }
                collectRegions(area.areas)
            }
        }


        collectRegions(areas)
        collectFinish(specificRegions)
        Log.d("Tag","$specificRegions")
        Log.d("Tag","$finalRegions")
        Log.d("Tag","$allRegions")
        return allRegions
            .sortedBy { it.name }
            .map { FilterItems.Region(it.id, it.name) }
    }
    }

