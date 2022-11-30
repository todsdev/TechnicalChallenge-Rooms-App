package com.tods.rooms.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

data class User(
    var name: String = "",
    var email: String = "",
    @get:Exclude
    var password: String = ""
) {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private fun recoverCurrentUser(): FirebaseUser? {
        auth = Firebase.auth
        return auth.currentUser
    }

    fun save() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(recoverCurrentUser()!!.uid).setValue(this)
    }
}


