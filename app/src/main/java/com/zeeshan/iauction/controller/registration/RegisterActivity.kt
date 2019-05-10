package com.zeeshan.iauction.controller.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.zeeshan.iauction.R
import com.zeeshan.iauction.adapter.FragmentViewPagerAdapter
import com.zeeshan.iauction.controller.dashboard.DashboardActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val intent = Intent(this@RegisterActivity, DashboardActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)

        }


        val tabLayout = findViewById<TabLayout>(R.id.regist_tab_layout)
        val viewPager = findViewById<ViewPager>(R.id.regist_viewPager)

        val adapter = FragmentViewPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)


//        supportFragmentManager.beginTransaction().add(R.id.containerRegister, SignUpFragment()).commit()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
