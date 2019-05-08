package com.zeeshan.iauction.model

data class Bidder(
    var bidderId : String,
    var bidderUserName: String,
    var bidderEmail: String,
    var bidderFullName: String,
    var bidderProfileImage: String = ""
){}