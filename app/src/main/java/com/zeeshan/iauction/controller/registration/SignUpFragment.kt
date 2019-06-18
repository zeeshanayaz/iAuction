package com.zeeshan.iauction.controller.registration


import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    var auth = FirebaseAuth.getInstance()
    var dbRef = FirebaseFirestore.getInstance()

    private lateinit var accountTypeStatus: String
    private lateinit var dialogBuilder: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTranslationZ(getView()!!, 100f);

        val signUpBtn = view.findViewById<Button>(R.id.createAccSignUpBtn)

        signUpBtn.setOnClickListener {
            if (checkEmptyFields(
                    createAccUserName.text!!.trim().toString(),
                    createAccUserEmail.text!!.trim().toString(),
                    createAccUserPassword.text!!.trim().toString(),
                    createAccUserContactNo.text!!.trim().toString()
                )
            ) {
                when (createAccTypeStatus.checkedRadioButtonId) {
                    createAccBidderRadioBtn.id -> accountTypeStatus = "Bidder"
                    createAccAuctionerRadioBtn.id -> accountTypeStatus = "Auctioner"
                }


                createUserAcc(
                    createAccUserName.text!!.trim().toString().toLowerCase(),
                    createAccUserEmail.text!!.trim().toString(),
                    createAccUserPassword.text!!.trim().toString(),
                    accountTypeStatus,
                    createAccUserContactNo.text!!.trim().toString()
                )
            } else {
                Snackbar.make(it, "all fields are required!", Snackbar.LENGTH_SHORT).show()
            }
        }


    }

    private fun createUserAcc(
        userName: String,
        email: String,
        password: String,
        accountTypeStatus: String,
        contactNumber: String
    ) {
        dbRef.collection("Users")
            .whereEqualTo("userName", userName)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
//                    Toast.makeText(activity, "User Name already occupied!", Toast.LENGTH_LONG).show()


                    val progressDialog = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null)
                    dialogBuilder = AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setView(progressDialog)
                        .show()




                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d(ContentValues.TAG, "createUserWithEmail:success")
                                addUserToFirestore(userName, email, contactNumber, accountTypeStatus)
                            } else {
                                dialogBuilder.dismiss()
                                auth.signOut()
                                // If sign in fails, display a message to the user.
                                Log.w(ContentValues.TAG, "createUserWithEmail:failure", it.exception)
                                Toast.makeText(
                                    activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                } else {
                    Toast.makeText(activity, "User Name already occupied!", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {

            }
    }

    private fun addUserToFirestore(
        userName: String,
        email: String,
        contactNumber: String,
        accountTypeStatus: String
    ) {
        val userData = User(auth.currentUser!!.uid, userName, email, accountTypeStatus)
        dbRef.collection("Users").document(auth.currentUser!!.uid).set(userData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User successfully written!")

                addExtraUserData(userData, contactNumber.toLong())
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error writing document", it)
            }
    }

    private fun addExtraUserData(userData: User, contactNumber: Long) {
        when (userData.userAccType) {
            "Bidder" -> {
                val data = Bidder(
                    userData.userId,
                    userData.userName,
                    userData.userEmail,
                    contactNumber,
                    "",
                    ""
                )

                dbRef.collection(userData.userAccType).document(userData.userId).set(data)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "User successfully written!")
                        navigateToMain()
                    }
                    .addOnFailureListener {
                        Log.w(ContentValues.TAG, "Error writing document", it)
                        dialogBuilder.dismiss()
                        auth.signOut()
                    }
            }
            "Auctioner" -> {
                val data = Auctioner(
                    userData.userId,
                    userData.userName,
                    userData.userEmail,
                    "",
                    contactNumber.toLong(),
                    "",
                    "",
                    ""
                )
                dbRef.collection(userData.userAccType).document(userData.userId).set(data)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "User successfully written!")
                        navigateToMain()
                    }
                    .addOnFailureListener {
                        Log.w(ContentValues.TAG, "Error writing document", it)
                        dialogBuilder.dismiss()
                        auth.signOut()
                    }
            }
        }
    }

    private fun navigateToMain() {
        val registrationActivity = activity!! as RegisterActivity

        val intent = Intent(activity!!, DashboardActivity::class.java).apply {
            //            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        dialogBuilder.dismiss()
        registrationActivity.finish()

        startActivity(intent)
    }

    private fun checkEmptyFields(
        userName: String,
        email: String,
        password: String,
        contactNumber: String
    ): Boolean {
        return (!userName.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty() && !contactNumber.isNullOrEmpty())
    }


}
