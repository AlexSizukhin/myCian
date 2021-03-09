package com.shokker.mycian.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shokker.mycian.MainContract
import com.shokker.mycian.Model.ResultFlat
import com.shokker.mycian.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FlatListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlatListFragment : Fragment(),MainContract.IFlatResultFragment {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flat_list, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FlatListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FlatListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun addFlatResult(resultList: ResultFlat) {
        TODO("Not yet implemented")
    }

    override fun clearFlatResult() {
        TODO("Not yet implemented")
    }

    override fun flatResultLoaded() {
        TODO("Not yet implemented")
    }

    override fun setOnClickFlat(onClickFlatFunc: (clickedFlat: ResultFlat) -> Unit) {
        this.onClickFlatFunc = onClickFlatFunc
    }

    private var onClickFlatFunc: ((ResultFlat) -> Unit)? = null
}