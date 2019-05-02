package com.example.visitor.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Profile (
    val name: String = "",
    val number: String = "",
    val photoUrl: String = "",
    var visitCount: Int = 0
)