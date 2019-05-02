package com.example.visitor.utils

import android.util.Log
import com.example.visitor.interfaces.Callbacks
import com.example.visitor.interfaces.ProfileListCallback
import com.example.visitor.model.Profile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object DatabaseUtils {

    private const val NON_VERIFIED_USER = 0
    private const val VERIFIED_USER_FOUND = 1
    private const val VERIFIED_USER_NOT_FOUND = 2

    private val database = FirebaseDatabase.getInstance().reference
    private val visitorsRef = database.child("visitors")
    private val suspiciousRef = database.child("suspicious_visitors")
    lateinit var userProfile: Profile

    fun searchUser(phone: String, callbacks: Callbacks) {

        var key: String?
        val searchQuery = visitorsRef.orderByChild("number").equalTo(phone)
        searchQuery.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    userProfile = singleSnapshot.getValue<Profile>(Profile::class.java)!!
                    key = singleSnapshot.key
                    Log.d("inonData",key)
                    callbacks.getKey(key)
                    return
                }
                callbacks.getKey("")
            }
        })
    }

    fun getProfileFromKey(key: String, callbacks: Callbacks): Profile? {

        var profile: Profile? = null
        visitorsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot){
                for (singleSnapshot in dataSnapshot.children) {
                    if (singleSnapshot.key == key) {
                        profile =  singleSnapshot.getValue<Profile>(Profile::class.java)
                        callbacks.getProfile(profile!!,key)
                    }
                }
            }
        })
        return profile
    }

    fun updateDB(profile: Profile, userType: Int, key: String = "") {

        when (userType) {
            VERIFIED_USER_FOUND -> {
                visitorsRef.child(key).setValue(profile)
            }
            VERIFIED_USER_NOT_FOUND -> {
                val userKey = visitorsRef.push().key
                if (userKey != null) {
                    visitorsRef.child(userKey).setValue(profile)
                }
            }
            NON_VERIFIED_USER -> {
                val userKey = visitorsRef.push().key
                if (userKey != null) {
                    suspiciousRef.child(userKey).setValue(profile)
                }
            }
        }

    }

    fun getAllEntries(profileListCallback: ProfileListCallback) {
        var key: String?
        val searchQuery = visitorsRef.orderByKey()
        val profileList = ArrayList<Profile>()
        /*searchQuery.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    userProfile = singleSnapshot.getValue<Profile>(Profile::class.java)!!
                    profileList.add(userProfile)
                    profileListCallback.getProfileList(profileList)
                    return
                }
        }
        })*/
        visitorsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    userProfile = singleSnapshot.getValue<Profile>(Profile::class.java)!!
                    profileList.add(userProfile)
                    profileListCallback.getProfileList(profileList)
                    return
                }
            }
        })
        profileListCallback.getProfileList(profileList)
    }

}