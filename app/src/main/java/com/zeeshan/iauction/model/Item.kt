package com.zeeshan.iauction.model

class Item(
    var itemId: String = "",
    var itemCategory: String = "",          //Spinner
    var itemCondition: String = "",         //Spinner
    var itemManufacturer: String = "",
    var itemTitle: String = "",
    var itemDescription: String = "",
    var itemMinBid: Int = 0,
    var itemPictureUrl: String? = null,
    var itemPostDateTime: Long = 0,
    var itemFinalDateTime: Long = 0,
    var itemStatus: String = "",
    var itemOwnerId: String = "",
    var itemBidderList: ArrayList<String>? = null
) {
}