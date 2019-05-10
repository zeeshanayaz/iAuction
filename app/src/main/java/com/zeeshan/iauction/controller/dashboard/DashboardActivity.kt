package com.zeeshan.iauction.controller.dashboard

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.zeeshan.iauction.R
import com.zeeshan.iauction.controller.registration.RegisterActivity
import com.zeeshan.iauction.model.User
import com.zeeshan.iauction.utilities.AppPref
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar_dashboard)


        startFragment()


        user_profile_image.setOnClickListener {
            PopupMenu(this, it).apply {
                // MainActivity implements OnMenuItemClickListener
                setOnMenuItemClickListener(this@DashboardActivity)
                inflate(R.menu.dashboard)
                show()
            }
//            val popup = PopupMenu(this, it)
//            val inflater: MenuInflater = popup.menuInflater
//            PopupMenu.setOnMenuItemClickListener(this@DashboardActivity)
//            inflater.inflate(R.menu.dashboard, popup.menu)
//            popup.show()
        }

    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Setting", Toast.LENGTH_LONG).show()
                true
            }
            R.id.action_sign_out -> {
                showSignOutPopUp()
                true
            }
            else -> false
        }
    }

    private fun showSignOutPopUp() {
        val dialogBuilder = AlertDialog.Builder(this@DashboardActivity)
        val create: AlertDialog = dialogBuilder.create()

        dialogBuilder.setCancelable(false)

        dialogBuilder.setTitle("Signing Out!")
        dialogBuilder.setMessage("Do you want to Sign out!")

        dialogBuilder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val curUser = FirebaseAuth.getInstance()
                curUser.signOut()
                val user = User("", "", "","")
                AppPref(this@DashboardActivity).setUser(user)
                AppPref(this@DashboardActivity).deleteBidder()
                AppPref(this@DashboardActivity).deleteAuctioner()
                val intent = Intent(this@DashboardActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        dialogBuilder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                create.dismiss()
            }
        })
        dialogBuilder.create()
        dialogBuilder.show()
    }

    private fun startFragment() {
//        supportActionBar!!.setTitle("Explore")
        toolbar_title.setText("Explore Products")
        supportFragmentManager.beginTransaction().add(
            R.id.dashboardContainer,
            ExploreFragment()
        ).commit()
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.dashboard, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
