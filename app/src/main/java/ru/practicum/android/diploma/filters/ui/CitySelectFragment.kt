package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.CitySelectFragmentBinding
import ru.practicum.android.diploma.filters.presentation.CityViewModel
import ru.practicum.android.diploma.filters.presentation.states.FilterItems
import ru.practicum.android.diploma.filters.presentation.states.FiltersChooserScreenState
import ru.practicum.android.diploma.filters.ui.presenter.FilterAdapter
import ru.practicum.android.diploma.search.ui.VacancySearchFragment.Companion.CLICK_DEBOUNCE_TIME


class CitySelectFragment : Fragment() {
    private var _binding: CitySelectFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CityViewModel by viewModel()
    //{ parametersOf(countryId)}

    //private val countryId by lazy { requireArguments().getString(COUNTRY_ID) }

    private var isClickAllowed = true
    private val adapter: FilterAdapter by lazy {
        FilterAdapter { item ->
            if (item is FilterItems.Region) {
                captureId(item.id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CitySelectFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FiltersChooserScreenState.ChooseItem -> {showContent(state.items)
                Log.d("Tag" , "cityes222 ${state.items}")}
                is FiltersChooserScreenState.Empty -> showEmptyScreen()
                is FiltersChooserScreenState.Error -> showErrorScreen()
            }

        }
    }

    private fun captureId(cityId: String) {
        if (isClickAllowed) {
            findNavController().navigate(
                R.id.action_citySelectFragment_to_workingRegionFragment,
                WorkingRegionFragment.createArgs(cityId)
            )
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_TIME)
                isClickAllowed = true
            }
        }
    }

    private fun showContent(state: List<FilterItems>) {
        Log.d("Tag" , "cityes ${adapter.items}")
        binding.recyclerView.isVisible = true
        binding.emptyPlaceholder.isVisible = false
        binding.notFoundPlaceholder.isVisible = false
        adapter.items.clear()
        adapter.items.addAll(state)
        adapter.notifyDataSetChanged()
    }

    fun showEmptyScreen() {
        binding.notFoundPlaceholder.isVisible = true
        binding.recyclerView.isVisible = false
        binding.emptyPlaceholder.isVisible = false
    }
    fun showErrorScreen() {
        binding.notFoundPlaceholder.isVisible = false
        binding.recyclerView.isVisible = false
        binding.emptyPlaceholder.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /*companion object {
        private const val COUNTRY_ID = "countryId"
        fun createArgs(itemId: String): Bundle {
            return bundleOf(
                COUNTRY_ID to itemId
            )
        }
    }

     */
}
