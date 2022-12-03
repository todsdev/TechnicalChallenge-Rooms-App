package com.tods.rooms.ui.my_reservation_details

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tods.rooms.R
import com.tods.rooms.data.model.firebase_model.Reservation
import com.tods.rooms.databinding.FragmentMyReservationDetailsBinding
import com.tods.rooms.state.ResourceState
import com.tods.rooms.ui.base.BaseFragment
import com.tods.rooms.util.Constants
import com.tods.rooms.util.hide
import com.tods.rooms.util.show
import com.tods.rooms.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MyReservationDetailsFragment: BaseFragment<FragmentMyReservationDetailsBinding, MyReservationDetailsViewModel>() {
    override val viewModel: MyReservationDetailsViewModel by viewModels()
    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentMyReservationDetailsBinding = FragmentMyReservationDetailsBinding.inflate(inflater, container, false)
    private val args: MyReservationDetailsFragmentArgs by navArgs()
    private lateinit var reservation: Reservation
    private var newReservation: Reservation = Reservation()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configInitialSettings()
        configInitialView()
        configButtonRecalculateClickListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configSharedPreferences()
    }

    private fun configSharedPreferences() {
        sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE) ?: return
        val value =
            sharedPreferences.getInt(getString(R.string.how_to_use), Constants.DEFAULT_VALUE)
        if (value == Constants.DEFAULT_VALUE) {
            sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPreferences.edit()) {
                putInt(getString(R.string.how_to_use), Constants.UPDATED_VALUE)
                apply()
            }
            configOneTimeDialog()
        }
    }

    private fun configOneTimeDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.how_to_use_))
            .setMessage(getString(R.string.dialog_message))
            .setPositiveButton(getString(R.string.accept)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun configButtonRecalculateClickListener() = with(binding) {
        buttonRecalculate.setOnClickListener {
            if(spinnerCurrency.selectedItem.toString() != getString(R.string.dash_dash)
                && spinnerCurrency.selectedItem.toString() != reservation.currency
                && spinnerBeds.selectedItem != reservation.numBeds
                && spinnerBeds.selectedItem != getString(R.string.dash_dash)) {
                val calculatingNewBedValue: Float = (reservation.totalValue) / (reservation.numBeds)
                val recoveringBedsString = spinnerBeds.selectedItem.toString()
                val recoveringBedsFloat = recoveringBedsString.toFloat()
                val newValue = (calculatingNewBedValue * recoveringBedsFloat)
                if(spinnerPaymentMethod.selectedItem.toString() != getString(R.string.dash_dash)
                    && spinnerPaymentMethod.selectedItem.toString() != reservation.paymentMethod) {
                    editPayment.text = spinnerPaymentMethod.selectedItem.toString()
                }
                editBeds.text = spinnerBeds.selectedItem.toString()
                viewModel.fetch(spinnerCurrency.selectedItem.toString(), newValue)
                configCollectObserver()
            } else if(spinnerCurrency.selectedItem.toString() != getString(R.string.dash_dash)
                && spinnerCurrency.selectedItem.toString() == reservation.currency
                && spinnerBeds.selectedItem != reservation.numBeds
                && spinnerBeds.selectedItem != getString(R.string.dash_dash)) {
                val calculatingNewBedValue: Float = (reservation.totalValue) / (reservation.numBeds)
                val recoveringBedsString = spinnerBeds.selectedItem.toString()
                val recoveringBedsFloat = recoveringBedsString.toFloat()
                val newValue = (calculatingNewBedValue * recoveringBedsFloat)
                if(spinnerPaymentMethod.selectedItem.toString() != getString(R.string.dash_dash)
                    && spinnerPaymentMethod.selectedItem.toString() != reservation.paymentMethod) {
                    editPayment.text = spinnerPaymentMethod.selectedItem.toString()
                }
                editBeds.text = spinnerBeds.selectedItem.toString()
                viewModel.fetch(reservation.currency, newValue)
                configCollectObserver()
            } else if(spinnerCurrency.selectedItem.toString() != getString(R.string.dash_dash)
                && spinnerCurrency.selectedItem.toString() != reservation.currency
                && spinnerBeds.selectedItem == reservation.numBeds
                && spinnerBeds.selectedItem != getString(R.string.dash_dash)) {
                if(spinnerPaymentMethod.selectedItem.toString() != getString(R.string.dash_dash)
                    && spinnerPaymentMethod.selectedItem.toString() != reservation.paymentMethod) {
                    editPayment.text = spinnerPaymentMethod.selectedItem.toString()
                }
                viewModel.fetch(spinnerCurrency.selectedItem.toString(), reservation.totalValue)
                configCollectObserver()
            } else {
                toast(getString(R.string.impossible_recalculate))
            }
            buttonSaveChanges.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.update_reservation))
                    .setMessage(getString(R.string.sure_delete))
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }.setPositiveButton(getString(R.string.confirm)) { _, _ ->
                        newReservation.numBeds = spinnerBeds.selectedItem.toString().toInt()
                        newReservation.currency = spinnerCurrency.selectedItem.toString()
                        if(spinnerPaymentMethod.selectedItem.toString() == getString(R.string.dash_dash)) {
                            newReservation.paymentMethod = reservation.paymentMethod
                        } else {
                            newReservation.paymentMethod = spinnerPaymentMethod.selectedItem.toString()
                        }
                        newReservation.numDays = reservation.numDays
                        newReservation.checkIn = reservation.checkIn
                        newReservation.checkOut = reservation.checkOut
                        newReservation.baseValue = reservation.baseValue
                        newReservation.update()
                        val action = MyReservationDetailsFragmentDirections.actionMyReservationDetailsFragmentToMyReservationsFragment()
                        findNavController().navigate(action)
                    }.show()
            }
        }
    }

    private fun configInitialSettings() {
        reservation = args.reservation
    }

    private fun configInitialView() = with(binding) {
        when (reservation.baseValue) {
            17.56f -> {
                textTitle.text = getString(R.string.six_beds_dorm)
                textBaseValue.text = getString(R.string._17_56)
                Picasso.get().load(Constants.URL_6)
                    .into(imageDetails)
            }
            14.50f -> {
                textTitle.text = getString(R.string.eight_beds_dorm)
                textBaseValue.text = getString(R.string._14_50)
                Picasso.get().load(Constants.URL_8)
                    .into(imageDetails)
            }
            else -> {
                textTitle.text = getString(R.string.twelve_beds_dorm)
                textBaseValue.text = getString(R.string._12_01)
                Picasso.get().load(Constants.URL_12)
                    .into(imageDetails)
            }
        }
        etCheckIn.text = reservation.checkIn
        etCheckOut.text = reservation.checkOut
        val calculatedValue = ((reservation.baseValue) * (reservation.rate) * (reservation.numBeds) * (reservation.numDays))
        val decimal = DecimalFormat()
        decimal.maximumFractionDigits = 2
        val convertedValueToDecimal = decimal.format(calculatedValue)
        textTotalValue.text = convertedValueToDecimal.toString()
        textRate.text = reservation.rate.toString()
        textSelectedCurrency.text = reservation.currency
        editCurrency.text = reservation.currency
        editBeds.text = reservation.numBeds.toString()
        editPayment.text = reservation.paymentMethod
        configSpinnerBeds()
        configSpinnerCurrency()
        configSpinnerPaymentMethod()
    }

    private fun configSpinnerPaymentMethod() {
        val spinnerPaymentMethod: Spinner = binding.spinnerPaymentMethod
        val paymentMethod: Array<out String> = resources.getStringArray(R.array.payment)
        val paymentMethodAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethod)
        spinnerPaymentMethod.adapter = paymentMethodAdapter
    }

    private fun configSpinnerCurrency() {
        val spinnerCurrency: Spinner = binding.spinnerCurrency
        val currency: Array<out String> = resources.getStringArray(R.array.currency)
        val currencyAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currency)
        spinnerCurrency.adapter = currencyAdapter
    }

    private fun configSpinnerBeds() {
        val spinnerBeds: Spinner = binding.spinnerBeds
        val beds: Array<out String> = when (reservation.baseValue) {
            17.56f -> {
                resources.getStringArray(R.array.room6)
            }
            14.50f -> {
                resources.getStringArray(R.array.room8)
            }
            else -> {
                resources.getStringArray(R.array.room12)
            }
        }
        val bedAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, beds)
        spinnerBeds.adapter = bedAdapter
    }

    private fun configCollectObserver() = lifecycleScope.launch {
        viewModel.search.collect { result ->
            when(result) {
                is ResourceState.Success -> {
                    binding.progressDetails.hide()
                    result.data?.let { values ->
                        val newValue = values.result
                        val decimal = DecimalFormat()
                        decimal.maximumFractionDigits = 2
                        val convertedValue = decimal.format(newValue)
                        binding.textTotalValue.text = convertedValue.toString()
                        binding.textRate.text = values.info.rate.toString()
                        binding.textSelectedCurrency.text = values.query.to
                        binding.editCurrency.text = values.query.to
                        newReservation.totalValue = values.result
                        newReservation.rate = values.info.rate
                    }
                }
                is ResourceState.Loading -> {
                    binding.progressDetails.show()
                }
                is ResourceState.Error -> {
                    binding.progressDetails.hide()
                    toast(getString(R.string.failed))
                }
                else -> { }
            }
        }
    }
}
