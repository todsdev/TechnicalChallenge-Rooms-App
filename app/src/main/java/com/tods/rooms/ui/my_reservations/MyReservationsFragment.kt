package com.tods.rooms.ui.my_reservations

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.tods.rooms.R
import com.tods.rooms.data.model.firebase_model.Reservation
import com.tods.rooms.databinding.FragmentMyReservationsBinding
import com.tods.rooms.ui.adapter.MyReservationsAdapter
import com.tods.rooms.ui.base.BaseFragment
import com.tods.rooms.util.hide
import com.tods.rooms.util.show
import com.tods.rooms.util.toast
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class MyReservationsFragment: BaseFragment<FragmentMyReservationsBinding, MyReservationsViewModel>() {
    private var adUserRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var reservationList: MutableList<Reservation> = ArrayList<Reservation>()
    private val auth: FirebaseAuth = Firebase.auth
    override val viewModel: MyReservationsViewModel by viewModels()
    private val adapterReservations by lazy { MyReservationsAdapter(reservationList) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecyclerView()
        configClickAdapter()
        configRecoverReservations()
    }

    private fun configRecoverReservations(){
        val dialog: AlertDialog? = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage(getString(R.string.recovering_reservations))
            .setCancelable(false)
            .build()
        dialog?.show()
        adUserRef = FirebaseDatabase.getInstance()
            .getReference("Reservations")
            .child(auth.currentUser!!.uid)
        adUserRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reservationList.clear()
                for (ds: DataSnapshot in snapshot.children){
                    reservationList.add(ds.getValue(Reservation::class.java)!!)
                }
                if(reservationList.isEmpty()) {
                    binding.recyclerMyReservations.hide()
                    binding.textEmptyList.show()
                }
                reservationList.reverse()
                dialog?.dismiss()
                adapterReservations.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                null
            }
        })
    }

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentMyReservationsBinding = FragmentMyReservationsBinding.inflate(inflater, container, false)

    private fun itemTouchHelperCallback(): ItemTouchHelper.SimpleCallback {
        return object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder):
                    Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.delete_reservation))
                    .setMessage(getString(R.string.sure_delete))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        val reservation = adapterReservations.getReservationPosition(viewHolder.adapterPosition)
                        reservation.remove()
                        toast(getString(R.string.deleted_reservation))
                    }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss().also {
                            adapterReservations.notifyDataSetChanged()
                        }
                    }.show()
            }
        }
    }

    private fun configRecyclerView() = with(binding) {
        recyclerMyReservations.apply {
            layoutManager = LinearLayoutManager(context)
            recyclerMyReservations.setHasFixedSize(true)
            adapter = adapterReservations
        }
        ItemTouchHelper(itemTouchHelperCallback()).attachToRecyclerView(recyclerMyReservations)
    }

    private fun configClickAdapter() {
        adapterReservations.setOnClickListener { reservation ->
            val action = MyReservationsFragmentDirections.actionMyReservationsFragmentToMyReservationDetailsFragment(reservation)
            findNavController().navigate(action)
        }
    }
}