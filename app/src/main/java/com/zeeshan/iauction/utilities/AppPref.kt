package com.zeeshan.iauction.utilities

import android.content.Context
import com.google.gson.Gson
import com.zeeshan.iauction.model.Auctioner
import com.zeeshan.iauction.model.Bidder
import com.zeeshan.iauction.model.User

class AppPref(var context: Context) {
    //      User Data
    fun getUser(): User? {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val userString = sharedPreferences.getString("user", null)
        if (userString != null) {
            val user = Gson().fromJson<User>(userString, User::class.java)
            return user
        } else {
            return null
        }
    }

    fun setUser(user: User?) {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("user", Gson().toJson(user))
        edit.apply()
    }

    //    Auctioner Data
    fun getAuctioner(): Auctioner? {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val userString = sharedPreferences.getString("company", null)
        if (userString != null) {
            val auctioner = Gson().fromJson<Auctioner>(userString, Auctioner::class.java)
            return auctioner
        } else {
            return null
        }
    }

    fun setAuctioner(auctioner: Auctioner?) {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("auctioner", Gson().toJson(auctioner))
        edit.apply()
    }

    fun deleteAuctioner() {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("auctioner", null)
        edit.apply()
    }


    //    Student Data
    fun getBidder(): Bidder? {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val userString = sharedPreferences.getString("student", null)
        if (userString != null) {
            val bidder = Gson().fromJson<Bidder>(userString, Bidder::class.java)
            return bidder
        } else {
            return null
        }
    }

    fun setBidder(bidder: Bidder) {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("bidder", Gson().toJson(bidder))
        edit.apply()
    }

    fun deleteBidder() {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("bidder", null)
        edit.apply()
    }
}