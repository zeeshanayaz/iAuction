package com.zeeshan.iauction.controller.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.fragment.app.Fragment
import com.zeeshan.iauction.R
import kotlinx.android.synthetic.main.fragment_post_item.*

class PostItemFragment : Fragment() {

    private lateinit var categorySpinner: Spinner
    private lateinit var conditionSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_item, container, false)

//        val floatingAddItemBtn = view.findViewById<Button>(R.id.floating_add_item_btn)
//        floatingAddItemBtn.visibility = View.INVISIBLE


        categorySpinner = view.findViewById(R.id.category_spinner)
        conditionSpinner = view.findViewById(R.id.category_condition)

        categorySpinner.adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.item_category)
        ) as SpinnerAdapter?


        conditionSpinner.adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.item_condition)
        ) as SpinnerAdapter?

        return view
    }


}
