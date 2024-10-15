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

class CountryViewModel(
    private val filterInteractor: FilterAreaInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FiltersChooserScreenState>()
    fun observeState(): LiveData<FiltersChooserScreenState> = stateLiveData

    init {
        getCountry()
        Log.d("Tag","${getCountry()}")
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
                renderState(FiltersChooserScreenState.ChooseItem(convertToCountries(foundCountries)))
            }
        }
    }

    private fun convertToCountries(foundCountries: List<Area>): List<FilterItems.Region> {
        val countries = foundCountries
            .filter { it.parentId == null }
            .sortedBy { if (it.id == "1001") 1 else 0 }
            .map {
                FilterItems.Region(
                    it.id,
                    it.name
                )
            }
        return countries
    }
}

