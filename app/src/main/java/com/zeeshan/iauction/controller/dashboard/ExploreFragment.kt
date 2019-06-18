package com.zeeshan.iauction.controller.dashboard


import android.content.ContentValues
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.zeeshan.iauction.R
import com.zeeshan.iauction.adapter.ItemListAdapter
import com.zeeshan.iauction.model.Item
import com.zeeshan.iauction.model.User
import com.zeeshan.iauction.utilities.AppPref
import kotlinx.android.synthetic.main.activity_dashboard.*


class ExploreFragment : Fragment() {

    private lateinit var appPrefUser: User      //User from App Preference
    private lateinit var dbReference: FirebaseFirestore
    private lateinit var tabLayout: TabLayout
    private lateinit var itemListAdapter: ItemListAdapter
    private var itemList = ArrayList<Item>()
    private lateinit var addItemBtn : FloatingActionButton
    var spanCount = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.zeeshan.iauction.R.layout.fragment_explore, container, false)

        appPrefUser = AppPref(activity!!).getUser()!!
        dbReference = FirebaseFirestore.getInstance()
        tabLayout = view.findViewById<TabLayout>(com.zeeshan.iauction.R.id.product_tablayout)
        addItemBtn = view.findViewById<FloatingActionButton>(R.id.floating_add_item_btn)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val orientation = resources.configuration.orientation
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            spanCount = 3
//        }
//        val screenSize = resources.configuration.screenWidthDp
//        if (screenSize > 720) {
//            spanCount = 3
//        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.product_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(activity!!, spanCount)
        itemListAdapter = ItemListAdapter(activity!!, itemList, dbReference,
            {
                //            OnClick
                Toast.makeText(activity!!, "Clicked ${it.itemTitle}", Toast.LENGTH_SHORT).show()
            },
            {
                //            OnClick
                Toast.makeText(activity!!, "Long Clicked ${it.itemTitle}", Toast.LENGTH_SHORT).show()
            })
        recyclerView.adapter = itemListAdapter


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.getPosition()
                when (position) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                    4 -> {

                    }
                }
            }

        })

        retrieveAllItemList()

        addItemBtn.setOnClickListener {
//            toolbar_title.setText("Post Your Products")
            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.dashboardContainer,
                PostItemFragment()
            ).addToBackStack("explore").commit()
        }

    }

    private fun retrieveAllItemList() {
        dbReference.collection("Item")
            .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@EventListener
                }

                for (dc in querySnapshot!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            try {
                                itemList.add(dc.document.toObject(Item::class.java))
                                println(itemList)
                                Log.d("ItemExploreFragment", itemList.toString())
                                itemListAdapter.notifyDataSetChanged()
                            } catch (e: Exception) {
                                Toast.makeText(activity, "${e.printStackTrace()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {

//                            if (dc.document != null) {
//                                val updatedJob = dc.document.toObject(Job::class.java)
//
//                                jobList.forEachIndexed { position, jobObj ->
//                                    if (jobObj.equals(updatedJob)) {
//                                        println("MATCHED")
//                                        jobList[position] = updatedJob
//                                        jobListAdapter.notifyDataSetChanged()
//                                    } else {
//                                        println("NOT MATCHED")
//                                    }
//                                }
//                            }

                        }
                        DocumentChange.Type.REMOVED -> {

//                            if (dc.document != null) {
//                                val removedJob = dc.document.toObject(Job::class.java)
//                                var index: Int = -1
//
//                                jobList.forEachIndexed { position, jobObj ->
//                                    if (jobObj.equals(removedJob)) {
//                                        println("MATCHED")
//                                        index = position
//                                    } else {
//                                        println("NOT MATCHED")
//                                    }
//                                }
//                                jobList.removeAt(index)
//                                jobListAdapter.notifyDataSetChanged()
//                                checkEmptyList()
//                            }

                        }
                    }
                }
            })
    }
}
