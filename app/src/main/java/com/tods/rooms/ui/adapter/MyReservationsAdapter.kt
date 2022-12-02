package com.tods.rooms.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tods.rooms.data.model.firebase_model.Reservation
import com.tods.rooms.databinding.ItemReservationBinding
import java.text.DecimalFormat

class MyReservationsAdapter(private val list: List<Reservation>):
    RecyclerView.Adapter<MyReservationsAdapter.MyReservationsViewHolder>() {

    inner class MyReservationsViewHolder(val binding: ItemReservationBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<Reservation>() {
        override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem.baseValue == newItem.baseValue

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReservationsViewHolder {
        return MyReservationsViewHolder(
            ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyReservationsViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                val reservation: Reservation = list[position]
                binding.dateCheckIn.text = reservation.checkIn
                binding.dateCheckOut.text = reservation.checkOut
                binding.numberBeds.text = reservation.numBeds.toString()
                val finalValue = (reservation.rate * reservation.totalValue)
                val decimal = DecimalFormat()
                decimal.maximumFractionDigits = 2
                val convertedValue = decimal.format(finalValue)
                val convertedValueToDecimal = convertedValue.toString()
                binding.totalConvertedValue.text = convertedValueToDecimal
                binding.textCurrency.text = reservation.currency
                when (reservation.baseValue) {
                    17.56f -> {
                        Picasso.get().load("https://i.pinimg.com/564x/a1/71/9c/a1719ca9d3e8b1f41942ac3a1a6def97.jpg")
                            .into(binding.imageReservation)
                    }
                    14.50f -> {
                        Picasso.get().load("https://i.pinimg.com/564x/8a/4c/c0/8a4cc0e44b60c8e3946ac39363dc50e0.jpg")
                            .into(binding.imageReservation)
                    }
                    else -> {
                        Picasso.get().load("https://i.pinimg.com/564x/28/d0/db/28d0dbf080d9f05e74f411af069f8f2b.jpg")
                            .into(binding.imageReservation)
                    }
                }
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(list[position])
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private var onItemClickListener: ((Reservation) -> Unit)? = null

    fun setOnClickListener(listener: (Reservation) -> Unit) {
        onItemClickListener = listener
    }

    fun getReservationPosition(position: Int): Reservation {
        return list[position]
    }
}