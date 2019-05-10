package com.zeeshan.iauction.model

data class Auctioner(
    var auctionerId: String = "",
    var auctionerUserName: String = "",
    var auctionerEmail: String = "",
    var auctionerFullName: String = "",
    var auctionerContactNumber: Long = 0,
    var auctionerProfileImage: String = "",
    var auctionerCompany: String = "",
    var auctionerCompanyInfo: String = ""
) {}