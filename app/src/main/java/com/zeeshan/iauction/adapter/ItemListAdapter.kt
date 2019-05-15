package com.zeeshan.iauction.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.iauction.R
import com.zeeshan.iauction.model.Item
import com.zeeshan.iauction.utilities.AppPref
import com.zeeshan.iauction.utilities.dummyImage

class ItemListAdapter(
    var context: Context,
    var dataList: ArrayList<Item>,
    var dbReference: FirebaseFirestore,
    var itemClick: (item: Item) -> Unit,
    var itemLongClick: (item: Item) -> Unit
) : RecyclerView.Adapter<ItemListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.card_item_layout, null, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bindItem(dataList[position])
    }


    inner class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val user = AppPref(context).getUser()

        val title = view.findViewById<TextView>(R.id.cardItemTitle)
        val manufacture = view.findViewById<TextView>(R.id.cardItemManufacture)
        val status = view.findViewById<TextView>(R.id.cardItemStatus)
        val category = view.findViewById<TextView>(R.id.cardItemCategory)
        val condition = view.findViewById<TextView>(R.id.cardItemCondition)
        val bidAmount = view.findViewById<TextView>(R.id.cardItemBidAmount)
        val bidEndDate = view.findViewById<TextView>(R.id.cardItemBidEndDate)
        val itemImage = view.findViewById<ImageView>(R.id.cardItemImage)
        private lateinit var imageUrl: String

        fun bindItem(item: Item) {
            title.text = item.itemTitle
            manufacture.text = item.itemManufacturer
            status.text = item.itemStatus
            category.text = item.itemCategory
            condition.text = item.itemCondition
            bidAmount.text = item.itemMinBid.toString()

            if (!item.itemPictureUrl.isNullOrEmpty()) {
                imageUrl = item.itemPictureUrl!!
            } else {
                imageUrl = dummyImage
            }

            Glide.with(context).applyDefaultRequestOptions(RequestOptions().apply() {
                placeholder(CircularProgressDrawable(context).apply {
                    strokeWidth = 2f
                    centerRadius = 50f
                    start()
                })
            }).load(imageUrl).into(itemImage)


            view.setOnClickListener {
                itemClick(item)
            }

            view.setOnLongClickListener {
                itemLongClick(item)
                true
            }
        }

    }
}