package com.zeeshan.iauction.model

data class User(
    var userId: String = "",
    var userName: String = "",
    var userEmail: String = "",
//    var userContactNumber: Long = 0,
    var userAccType: String = ""
) {
}