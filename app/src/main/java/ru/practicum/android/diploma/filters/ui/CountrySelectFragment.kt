package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.databinding.CountrySelectFragmentBinding
import ru.practicum.android.diploma.filters.presentation.CountryViewModel
import ru.practicum.android.diploma.filters.ui.presenter.FilterAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filters.presentation.states.FilterItems
import ru.practicum.android.diploma.filters.presentation.states.FiltersChooserScreenState
import ru.practicum.android.diploma.search.ui.VacancySearchFragment.Companion.CLICK_DEBOUNCE_TIME

class CountrySelectFragment : Fragment() {
    private val viewModel: CountryViewModel by viewModel()
    private var _binding: CountrySelectFragmentBinding? = null
    private val binding get() = _binding!!
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
        _binding = CountrySelectFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.toolBar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FiltersChooserScreenState.ChooseItem -> showContent(state.items)
                is FiltersChooserScreenState.Empty -> showEmptyScreen()
                is FiltersChooserScreenState.Error -> showErrorScreen()
            }

        }
    }

    private fun captureId(countryId: String) {
        if (isClickAllowed) {
            findNavController().navigate(
                R.id.action_countrySelectFragment_to_workingRegionFragment,
                WorkingRegionFragment.createArgs(countryId)
            )
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_TIME)
                isClickAllowed = true
            }
        }
    }

    private fun showContent(state: List<FilterItems>) {
        binding.recyclerView.isVisible = true
        binding.placeholder.isVisible = false
        adapter.items.clear()
        adapter.items.addAll(state)
        adapter.notifyDataSetChanged()
    }

    fun showEmptyScreen() {
        binding.placeholder.isVisible = true
        binding.recyclerView.isVisible = false
        binding.imagePlaceholder.setImageResource(R.drawable.empty_area_placeholder)
        binding.textPlaceholder.setText(R.string.area_not_found)
    }

    fun showErrorScreen() {
        binding.placeholder.isVisible = true
        binding.recyclerView.isVisible = false
        binding.imagePlaceholder.setImageResource(R.drawable.search_not_connected_placeholder)
        binding.textPlaceholder.setText(R.string.no_internet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
