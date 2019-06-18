package com.zeeshan.iauction.controller.registration


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.iauction.R
import com.zeeshan.iauction.controller.dashboard.DashboardActivity
import com.zeeshan.iauction.model.Auctioner
import com.zeeshan.iauction.model.Bidder
import com.zeeshan.iauction.model.User
import com.zeeshan.iauction.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: FirebaseFirestore
    private lateinit var accountTypeStatus: String
    private lateinit var dialogBuilder: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTranslationZ(getView()!!, 100f);

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseFirestore.getInstance()
        val signInBtn = view.findViewById<Button>(R.id.signInSignInBtn)


        forgetPasswordLink.setOnClickListener {
            Snackbar.make(it, "Beta Version. Forget password not implemented...", Snackbar.LENGTH_SHORT).show()
        }


        signInBtn.setOnClickListener {
            if (checkEmptyFields(
                    signInUserEmail.text!!.trim().toString(),
                    signInUserPassword.text!!.trim().toString()
                )
            ) {
                when (signInTypeStatus.checkedRadioButtonId) {
                    signInBidderRadioBtn.id -> accountTypeStatus = "Bidder"
                    signInAuctionerRadioBtn.id -> accountTypeStatus = "Auctioner"
                }
                Snackbar.make(view, "Connecting to Server", Snackbar.LENGTH_SHORT).setAction("Action", null).show()

                val progressDialog = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null)
                dialogBuilder = AlertDialog.Builder(activity)
                    .setCancelable(false)
                    .setView(progressDialog)
                    .show()

                authenticateUser(
                    signInUserEmail.text!!.trim().toString(),
                    signInUserPassword.text!!.trim().toString(),
                    accountTypeStatus
                )
            } else {
                signInUserEmail.setError("Check you inputs...")
                signInUserPassword.setError("Check you inputs...")
                Snackbar.make(view, "All field are required.", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
            }
        }

    }

    private fun authenticateUser(email: String, password: String, accountTypeStatus: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                retrieveUserData(it.user.uid, dbReference, accountTypeStatus)
            }
            .addOnFailureListener {
                dialogBuilder.dismiss()
                Toast.makeText(activity, "Error: ${it.toString()}", Toast.LENGTH_LONG).show()
            }
    }

    private fun retrieveUserData(uid: String, dbReference: FirebaseFirestore, accountTypeStatus: String) {
        dbReference.collection("Users").document(uid).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val userData: User = it.toObject(User::class.java)!!
                    if (userData.userAccType == accountTypeStatus) {
                        AppPref(activity!!).setUser(userData)
                        retrieveData(uid, dbReference, accountTypeStatus)
                    } else {
                        dialogBuilder.dismiss()
                        Toast.makeText(
                            activity,
                            "There might be some error in the input fields. Please verify.....",
                            Toast.LENGTH_LONG
                        ).show()
                        auth.signOut()
                    }

                } else {
                    dialogBuilder.dismiss()
                    Toast.makeText(activity, "User Not Found! please contact admin support.....", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun retrieveData(uid: String, dbReference: FirebaseFirestore, accountTypeStatus: String) {
        dbReference.collection(accountTypeStatus).document(uid).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    when (accountTypeStatus) {
                        "Bidder" -> {
                            val retrieveData: Bidder = it.toObject(Bidder::class.java)!!
                            AppPref(activity!!).setBidder(retrieveData)
                        }
                        "Auctioner" -> {
                            val retrieveData: Auctioner = it.toObject(Auctioner::class.java)!!
                            AppPref(activity!!).setAuctioner(retrieveData)
                        }
                    }
                }
                dialogBuilder.dismiss()



                navigateToMain()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "${it.toString()}", Toast.LENGTH_LONG).show()
            }
    }

    private fun navigateToMain() {
        val registrationActivity = activity!! as RegisterActivity

        val intent = Intent(activity!!, DashboardActivity::class.java).apply {
            //            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        registrationActivity.finish()
        startActivity(intent)
    }

    private fun checkEmptyFields(email: String, password: String): Boolean {
        return (!email.isNullOrEmpty() && !password.isNullOrEmpty())
    }


}
