package com.zeeshan.iauction.controller


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.iauction.R
import com.zeeshan.iauction.model.User
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    var auth = FirebaseAuth.getInstance()
    var dbRef = FirebaseFirestore.getInstance()

    private lateinit var accountTypeStatus: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
//        val signInBtn = view.findViewById<Button>(R.id.createAccSignInBtn)

//        signInBtn.setOnClickListener {
//            activity!!.supportFragmentManager.beginTransaction().replace(R.id.containerRegister, SignInFragment())
//                .commit()
//        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signUpBtn = view.findViewById<Button>(R.id.createAccSignUpBtn)

        signUpBtn.setOnClickListener {
            if (checkEmptyFields(
                    createAccUserName.text!!.trim().toString(),
                    createAccUserEmail.text!!.trim().toString(),
                    createAccUserPassword.text!!.trim().toString()
                )
            ) {
                when(createAccTypeStatus.checkedRadioButtonId){
                    createAccBidderRadioBtn.id -> accountTypeStatus = "Bidder"
                    createAccAuctionerRadioBtn.id -> accountTypeStatus = "Auctioner"
                }


                createUserAcc(
                    createAccUserName.text!!.trim().toString(),
                    createAccUserEmail.text!!.trim().toString(),
                    createAccUserPassword.text!!.trim().toString(),
                    accountTypeStatus
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
        accountTypeStatus: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    addUserToFirestore(userName,email, accountTypeStatus)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", it.exception)
                    Toast.makeText(
                        activity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun addUserToFirestore(userName: String, email: String, accountTypeStatus: String) {
        val userData = User(auth.currentUser!!.uid, userName, email, accountTypeStatus)
        dbRef.collection("Users").document(auth.currentUser!!.uid).set(userData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User successfully written!")
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error writing document", it)
            }
    }

    private fun checkEmptyFields(userName: String, email: String, password: String): Boolean {
        return (!userName.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty())
    }


}
