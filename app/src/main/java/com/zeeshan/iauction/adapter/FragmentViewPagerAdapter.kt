package com.zeeshan.iauction.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zeeshan.iauction.controller.registration.SignInFragment
import com.zeeshan.iauction.controller.registration.SignUpFragment

class FragmentViewPagerAdapter(var ctx: Context, var fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return SignInFragment()
            else -> return SignUpFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Sign In"
            else -> return "Sign Up"
        }
    }

    override fun getCount(): Int {
        return 2
    }

}