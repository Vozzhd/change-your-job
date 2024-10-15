package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.WorkingRegionFragmentBinding

class WorkingRegionFragment : Fragment() {
    private var _binding: WorkingRegionFragmentBinding? = null
    private val binding get() = _binding!!

    private val countryId by lazy { requireArguments().getString(COUNTRY_ID) }
    private val cityId by lazy { requireArguments().getString(CITY_ID) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WorkingRegionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countyActionIcon.setOnClickListener {
            findNavController().navigate(R.id.action_workingRegionFragment_to_countrySelectFragment)
        }

        binding.regionActionIcon.setOnClickListener {
            findNavController().navigate(R.id.action_workingRegionFragment_to_citySelectFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val COUNTRY_ID = "countryId"
        private const val CITY_ID = "cityId"
        fun createArgs(itemId: String): Bundle {
            return bundleOf(
                COUNTRY_ID to itemId,
                CITY_ID to itemId
            )

        }
    }
}
