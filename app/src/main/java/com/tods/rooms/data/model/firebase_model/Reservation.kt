package com.tods.rooms.data.model.firebase_model

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Binds
import java.io.Serializable
import javax.annotation.Nonnull
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
        val replaced1 = checkIn.replace("/", "", false)
        val replaced2 = checkOut.replace("/", "", false)
        val replaced3 = baseValue.toString().replace(".", "", false)
        database = FirebaseDatabase.getInstance().getReference("Reservations")
        database.child(auth.currentUser!!.uid)
            .child("$replaced1$replaced2${replaced3}_$numDays")
            .setValue(this)
    }

    fun remove() {
        val replaced1 = checkIn.replace("/", "", false)
        val replaced2 = checkOut.replace("/", "", false)
        val replaced3 = baseValue.toString().replace(".", "", false)
        database = FirebaseDatabase.getInstance().getReference("Reservations")
        database.child(auth.currentUser!!.uid)
            .child("$replaced1$replaced2${replaced3}_$numDays")
            .removeValue()
    }

    fun update() {
        val map = toMap()
        val replaced1 = checkIn.replace("/", "", false)
        val replaced2 = checkOut.replace("/", "", false)
        val replaced3 = baseValue.toString().replace(".", "", false)
        val update = mapOf<String, Reservation>()
        database = FirebaseDatabase.getInstance().getReference("Reservations")
        database.child(auth.currentUser!!.uid)
            .child("$replaced1$replaced2${replaced3}_$numDays")
            .updateChildren(map)
    }

    private fun toMap(): Map<String, Any?> {
        return mapOf(
            "baseValue" to baseValue,
            "checkIn" to checkIn,
            "checkOut" to checkOut,
            "numDays" to numDays,
            "paymentMethod" to paymentMethod,
            "currency" to currency,
            "numBeds" to numBeds,
            "totalValue" to totalValue,
            "rate" to rate
        )
    }
}
