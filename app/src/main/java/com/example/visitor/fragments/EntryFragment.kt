package com.example.visitor.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.visitor.CameraActivity
import com.example.visitor.R
import com.example.visitor.interfaces.Callbacks
import com.example.visitor.model.Profile
import com.example.visitor.utils.DatabaseUtils
import com.example.visitor.utils.ImageUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.TimeUnit


class EntryFragment : Fragment(), Callbacks{

    private lateinit var profileImage: CircleImageView
    private lateinit var profileName: TextInputEditText
    private lateinit var profileNumber: TextInputEditText
    private lateinit var otp: TextInputEditText
    private lateinit var getOTP: MaterialButton
    private lateinit var submit: MaterialButton
    private lateinit var imagePath: String
    private lateinit var credential: PhoneAuthCredential
    private lateinit var progressBar: ProgressBar
    private lateinit var downloadUri: Uri
    private lateinit var storedVerificationId: String
    private var numberCheck: Boolean = false
    private var nameCheck: Boolean = false
    private var imageCheck: Boolean = false
    private var otpCheck: Boolean = false
    private val callbacks: Callbacks = this@EntryFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        findViews()
        setListeners()
    }

    private fun findViews() {

        profileImage = view!!.findViewById(R.id.camera_circle)
        profileName = view!!.findViewById(R.id.tiet_name)
        profileNumber = view!!.findViewById(R.id.tiet_number)
        otp = view!!.findViewById(R.id.tiet_otp)
        getOTP = view!!.findViewById(R.id.btn_get_otp)
        submit = view!!.findViewById(R.id.btn_submit)
        progressBar = view!!.findViewById(R.id.profile_progress)

        progressBar.visibility = View.GONE
        getOTP.isEnabled = false
        submit.isEnabled = false
    }

    private fun setListeners() {

        profileName.doAfterTextChanged {
            if (it != null) {
                nameCheck = it.isNotEmpty()
                enableButtons()
            }
        }

        profileNumber.doAfterTextChanged {
            if (it != null) {
                numberCheck = (it.length == 10 && !it.contains(Regex("^[A-Za-z]+$")))
                enableButtons()

            }
        }

        otp.doAfterTextChanged {
            if (it != null) {
                otpCheck = (otp.length() == 6)
                enableButtons()
            }
        }

        profileImage.setOnClickListener {
            startActivityForResult(Intent(this.context, CameraActivity::class.java), CAMERA_REQUEST)
        }

        getOTP.setOnClickListener {

            val authCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d("AuthUtils", "onVerificationCompleted:$credential")
                    signInWithPhoneAuthCredential(this@EntryFragment.activity as AppCompatActivity, credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.w("AuthUtils", "onVerificationFailed", e)

                    val profile = Profile(profileName.text.toString(),profileNumber.text.toString(),downloadUri.toString(),1)
                    DatabaseUtils.updateDB(profile, NON_VERIFIED_USER)
                    Snackbar.make(this@EntryFragment.view!!,"There is some error",Snackbar.LENGTH_SHORT).show()
                    this@EntryFragment.activity?.supportFragmentManager?.popBackStack()

                    if (e is FirebaseAuthInvalidCredentialsException) {

                    } else if (e is FirebaseTooManyRequestsException) {

                    }
                }

                override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken) {
                    Log.d("AuthUtils", "onCodeSent:" + verificationId!!)

                    storedVerificationId = verificationId
                    val resendToken = token

                }

                override fun onCodeAutoRetrievalTimeOut(p0: String?) {
                    val profile = Profile(profileName.text.toString(),profileNumber.text.toString(),downloadUri.toString(),1)
                    DatabaseUtils.updateDB(profile, NON_VERIFIED_USER)
                    Snackbar.make(this@EntryFragment.view!!,"There is some error",Snackbar.LENGTH_SHORT).show()
                    this@EntryFragment.activity?.supportFragmentManager?.popBackStack()
                }
            }

            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + profileNumber.text.toString(), 30, TimeUnit.SECONDS,
                this.activity!!, authCallbacks)
        }

        submit.setOnClickListener {

            DatabaseUtils.searchUser(profileNumber.text.toString(),callbacks)
            credential = PhoneAuthProvider.getCredential(storedVerificationId, otp.text.toString())
            signInWithPhoneAuthCredential(this@EntryFragment.activity as AppCompatActivity, credential)
        }
    }

    private fun signInWithPhoneAuthCredential(activity: AppCompatActivity, credential: PhoneAuthCredential) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AuthUtils", "signInWithCredential:success")
                    Snackbar.make(this@EntryFragment.view!!,"Entry stored",Snackbar.LENGTH_SHORT).show()
                    this@EntryFragment.activity?.supportFragmentManager?.popBackStack()
                } else {
                    // Sign in failed, display a message and update the UI
                    val profile = Profile(profileName.text.toString(),profileNumber.text.toString(),downloadUri.toString(),1)
                    DatabaseUtils.updateDB(profile, NON_VERIFIED_USER)
                    Snackbar.make(this@EntryFragment.view!!,"There is some error",Snackbar.LENGTH_SHORT).show()
                    this@EntryFragment.activity?.supportFragmentManager?.popBackStack()
                    Log.w("AuthUtils", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Snackbar.make(this@EntryFragment.view!!,"Invalid verification code",Snackbar.LENGTH_SHORT).show()
                        this@EntryFragment.activity?.supportFragmentManager?.popBackStack()
                    } else {

                    }
                }
            }
    }

    private fun enableButtons() {
        getOTP.isEnabled = (numberCheck && nameCheck && imageCheck)
        submit.isEnabled = (numberCheck && nameCheck && imageCheck && otpCheck)

    }

    override fun getImageURI(uri: Uri) {
        downloadUri = uri
        imageCheck = true
        progressBar.visibility = View.INVISIBLE
        Glide.with(this)
            .load(downloadUri)
            .into(profileImage)
        profileImage.visibility = View.VISIBLE
        enableButtons()
    }

    override fun getKey(key: String?) {
        if (!key.isNullOrEmpty()) {
            DatabaseUtils.getProfileFromKey(key,this)
        } else {
            val profile = Profile(profileName.text.toString(),profileNumber.text.toString(),downloadUri.toString(),1)
            DatabaseUtils.updateDB(profile, VERIFIED_USER_NOT_FOUND)
        }
    }

    override fun getProfile(profile: Profile, key: String) {
        profile.visitCount++
        DatabaseUtils.updateDB(profile, VERIFIED_USER_FOUND,key)
    }

    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            imagePath = data?.getStringExtra("image") ?: ""
            ImageUtils.uploadImage(this.context!!,imagePath,this)
            profileImage.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

    companion object {

        fun newInstance(): EntryFragment {
            return EntryFragment()
        }

        private const val CAMERA_REQUEST = 1337
        private const val NON_VERIFIED_USER = 0
        private const val VERIFIED_USER_FOUND = 1
        private const val VERIFIED_USER_NOT_FOUND = 2
    }
}