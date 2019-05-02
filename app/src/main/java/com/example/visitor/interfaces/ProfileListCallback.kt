package com.example.visitor.interfaces

import com.example.visitor.model.Profile

interface ProfileListCallback {
    fun getProfileList(profileList: ArrayList<Profile>)

}
