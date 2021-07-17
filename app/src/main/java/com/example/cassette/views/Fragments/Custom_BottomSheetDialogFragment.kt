package com.example.cassette.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cassette.R
import com.example.cassette.utlis.SortUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.modal_bottom_sheet.*

class Custom_BottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): Custom_BottomSheetDialogFragment? {
            return Custom_BottomSheetDialogFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_bottom_sheet, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()

        first_item_iv.visibility = View.INVISIBLE
        second_item_iv.visibility = View.VISIBLE

        ascendingOrder.setOnClickListener {
            context?.let { it1 ->
                SortUtils.changeSortingOrder(
                    it1,
                    getResources().getString(R.string.first_item_modal_bottomsheet)
                )
            }
        }

        sortByName.setOnClickListener {
            context?.let { it1 ->
                SortUtils.changeSortingOrder(
                    it1,
                    getResources().getString(R.string.second_item_modal_bottomsheet)
                )
            }
        }
        sortByArtist.setOnClickListener {
            context?.let { it1 ->
                SortUtils.changeSortingOrder(
                    it1,
                    getResources().getString(R.string.third_item_modal_bottomsheet)
                )
            }
        }
        sortByYear.setOnClickListener {
            context?.let { it1 ->
                SortUtils.changeSortingOrder(
                    it1,
                    getResources().getString(R.string.forth_item_modal_bottomsheet)
                )
            }
        }

    }
}