package com.shokker.mycian.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shokker.mycian.MainContract
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.R
import com.shokker.mycian.databinding.FragmentFilterBinding

class FilterFragment : Fragment() /*, MainContract.IFlatFilterFragment */{

    private var _binding: FragmentFilterBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getCurrentFilterState():FilterState
    {
       var fType = FilterState.FilterType.Rent
        when (binding.filterType.isChecked) {
            false -> fType = FilterState.FilterType.Rent
            true -> fType = FilterState.FilterType.Sale
        }
        return FilterState(fromPrice =  binding.priceFrom.currentValue.toInt(),
            toPrice =  binding.priceTo.currentValue.toInt(),
            fromSq = binding.squareFrom.currentValue.toInt(),
            toSq = binding.squareTo.currentValue.toInt(),
            fType = fType
        )
    }
    fun setFilterState(filterState: FilterState)
    {
        binding.priceFrom.currentValue = filterState.fromPrice.toDouble()
        binding.priceTo.currentValue = filterState.toPrice.toDouble()
        binding.squareFrom.currentValue = filterState.fromSq.toDouble()
        binding.squareTo.currentValue = filterState.toSq.toDouble()
        when (filterState.fType) {
            FilterState.FilterType.Rent -> binding.filterType.isChecked = false
            FilterState.FilterType.Sale -> binding.filterType.isChecked = true
        }
        binding.priceFrom.invalidate()
        binding.priceTo.invalidate()
        binding.squareFrom.invalidate()
        binding.squareTo.invalidate()
    }

 /*   override fun setOnChangeFilter(onChangeFunc: (newState: FilterState) -> Unit) {
        onChangeFuntion = onChangeFunc
    }
    private var onChangeFuntion: (( FilterState) -> Unit)? = null*/
}