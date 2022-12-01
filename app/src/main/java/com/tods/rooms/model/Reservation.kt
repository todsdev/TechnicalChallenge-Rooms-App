package com.tods.rooms.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class Reservation(
    var baseValue: Float = 0f,
    var checkIn: String = "",
    var checkOut: String = "",
    var numDays: Int = 0,
    var paymentMethod: String = "",
    var currency: String = "",
    var numBeds: Int = 0,
    var totalValue: Float = 0f
) {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    fun save() {
        database = FirebaseDatabase.getInstance().getReference("Reservations")
        database.child(auth.currentUser!!.uid).setValue(this)
    }
}
