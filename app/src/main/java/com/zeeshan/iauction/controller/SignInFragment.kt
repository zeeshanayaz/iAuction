package com.zeeshan.iauction.controller


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.zeeshan.iauction.R

class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
//        val signUpBtn = view.findViewById<Button>(R.id.signInSignUpBtn)

//        signUpBtn.setOnClickListener {
//            activity!!.supportFragmentManager.beginTransaction().replace(R.id.containerRegister, SignUpFragment())
//                .commit()
//        }


        return view
    }


}
