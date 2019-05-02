/*
package com.example.visitor.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

object AuthUtils {

    private lateinit var activity: AppCompatActivity
    private lateinit var code: String
    private lateinit var credential: PhoneAuthCredential


    private fun getOTP(phoneNumber: String, activity: AppCompatActivity) {
        this.activity = activity
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, activity, callbacks)
    }



}
*/
