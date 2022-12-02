package com.tods.rooms.data.model.firebase_model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Binds
import java.io.Serializable
import javax.inject.Singleton

@Singleton
data class Reservation(
    var baseValue: Float = 0f,
    var checkIn: String = "",
    var checkOut: String = "",
    var numDays: Int = 0,
    var paymentMethod: String = "",
    var currency: String = "",
    var numBeds: Int = 0,
    var totalValue: Float = 0f,
    var rate: Float = 0f
): Serializable {
    private lateinit var database: DatabaseReference
    private var auth: FirebaseAuth = Firebase.auth

    fun save() {
        database = FirebaseDatabase.getInstance().getReference("Reservations")
        database.child(auth.currentUser!!.uid)
            .child("$currency$numDays$numBeds$currency")
            .setValue(this)
    }

    fun remove(){
        database = FirebaseDatabase.getInstance().getReference("Reservations")
        database.child(auth.currentUser!!.uid)
            .child("$currency$numDays$numBeds$currency")
            .removeValue()
    }
}
