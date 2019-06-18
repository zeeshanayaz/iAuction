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
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.iauction.R
import com.zeeshan.iauction.controller.registration.RegisterActivity
import com.zeeshan.iauction.model.Auctioner
import com.zeeshan.iauction.model.Bidder
import com.zeeshan.iauction.model.User
import com.zeeshan.iauction.utilities.AppPref
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private lateinit var appPrefUser: User      //User from App Preference
    private var appPrefAuction: Auctioner? = null      //Company from App Preference
    private var appPrefBidder: Bidder? = null      //Company from App Preference
    private lateinit var dbReference: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar_dashboard)

        appPrefUser = AppPref(this).getUser()!!
        dbReference = FirebaseFirestore.getInstance()

//        if (appPrefUser.userAccType.equals("Bidder")) {
//            floating_add_item_btn.visibility = View.GONE
//        } else {
//            floating_add_item_btn.visibility = View.VISIBLE
//        }

//        checkVisibileView()


        startFragment()



        user_profile_image.setOnClickListener {
            PopupMenu(this, it).apply {
                // MainActivity implements OnMenuItemClickListener
                setOnMenuItemClickListener(this@DashboardActivity)
                inflate(R.menu.dashboard)
                show()

//                if (appPrefUser.userAccType.equals("Bidder")){
//                    menu.findItem(R.id.action_add_item).setVisible(false)
//                }
            }
        }




    }

//    private fun checkVisibileView() {
//        supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
//            // the fragment exists
//            if (it is ExploreFragment) {
//                // The presented fragment is FooFragment type
//                if (appPrefUser.userAccType.equals("Bidder")) {
//                    floating_add_item_btn.visibility = View.GONE
//                } else {
//                    floating_add_item_btn.visibility = View.VISIBLE
//                }
//            } else {
//                floating_add_item_btn.visibility = View.INVISIBLE
//            }
//        }
//    }

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
                val user = User("", "", "", "")
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
