package com.tods.rooms.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

data class User(
    var name: String,
    var email: String,
    @Exclude
    var password: String = "",
    var id: String = ""
) {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    fun recoverCurrentUser(): FirebaseUser? {
        auth = Firebase.auth
        return auth.currentUser
    }

    fun save() {
        val user = User(name, email, id)
        val generatedId = recoverCurrentUser()!!.uid
        user.id = generatedId
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(generatedId).setValue(this)
    }
}


