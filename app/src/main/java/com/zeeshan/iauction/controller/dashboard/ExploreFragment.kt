package com.zeeshan.iauction.controller.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout


class ExploreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.zeeshan.iauction.R.layout.fragment_explore, container, false)


        val tabLayout = view.findViewById<TabLayout>(com.zeeshan.iauction.R.id.product_tablayout)

//        Toast.makeText(activity, "${tabLayout.selectedTabPosition.toString()}", Toast.LENGTH_SHORT).show()
//        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                val position = tab.position
//            }
//        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.getPosition()
                if (position != null) {
                    Toast.makeText(activity, "${position.toString()}",Toast.LENGTH_SHORT ).show()
                }
            }

        })
        return view
    }


}
