package com.example.visitor.interfaces

import android.net.Uri
import com.example.visitor.model.Profile

interface Callbacks {

    fun getImageURI(uri: Uri)
    fun getKey(key: String?)
    fun getProfile(profile: Profile, key: String)
}