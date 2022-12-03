package com.tods.rooms.ui.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.tods.rooms.R
import com.tods.rooms.data.model.firebase_model.Reservation
import com.tods.rooms.databinding.FragmentRoomBinding
import com.tods.rooms.state.ResourceState
import com.tods.rooms.ui.base.BaseFragment
import com.tods.rooms.util.hide
import com.tods.rooms.util.show
import com.tods.rooms.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class RoomFragment: BaseFragment<FragmentRoomBinding, RoomViewModel>() {
    private lateinit var hiddenViewDate: ConstraintLayout
    private lateinit var hiddenViewPayment: ConstraintLayout
    private lateinit var hiddenViewTotal: ConstraintLayout
    private lateinit var calendarViewIn: MaterialCalendarView
    private lateinit var calendarViewOut: MaterialCalendarView
    private var bedValue: Float = 0f
    private var rate: Float = 0f
    private var usdValue: Float = 0f
    private var checkInDay: CalendarDay? = null
    private var checkOutDay: CalendarDay? = null
    private var paymentMethod: String = ""
    private var bedNumbers: Int = 0
    private var chosenCurrency: String = ""
    private val args: RoomFragmentArgs by navArgs()
    override val viewModel: RoomViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configInitialSettings()
        configHiddenCards()
        configDateValues()
        configPaymentsValues()
    }

    private fun configCollectObserver() = lifecycleScope.launch {
        viewModel.search.collect { result ->
            when(result) {
                is ResourceState.Success -> {
                    binding.progressCircular.hide()
                    result.data?.let { values ->
                        val decimal = DecimalFormat()
                        decimal.maximumFractionDigits = 2
                        val convertedTotalValue = values.result
                        val convertedValueToDecimal = decimal.format(convertedTotalValue)
                        binding.tvTotalValue.text =
                            "${convertedValueToDecimal.toString()} ${values.query.to.uppercase(
                                Locale.getDefault())}"
                        rate = values.info.rate
                    }
                }
                is ResourceState.Loading -> {
                    binding.progressCircular.show()
                }
                is ResourceState.Error -> {
                    binding.progressCircular.hide()
                    toast(getString(R.string.failed))
                }
                else -> { }
            }
        }
    }

    private fun configTotalValues() = with(binding) {
        val dateOne = checkInDay!!.date.time.milliseconds
        val dateTwo = checkOutDay!!.date.time.milliseconds
        val millisecondsBetween: Long = dateTwo.inWholeMilliseconds - dateOne.inWholeMilliseconds
        val daysBetween = TimeUnit.MILLISECONDS.toDays(millisecondsBetween).toString()
        val totalValue = (bedValue * daysBetween.toInt() * bedNumbers)
        viewModel.fetch(chosenCurrency, totalValue)
        configCollectObserver()
        tvDays.text = daysBetween
        tvCurrency.text = chosenCurrency
        tvNumberRooms.text = bedNumbers.toString()
        tvPayment.text = paymentMethod
        tvCheckIn.text = "${checkInDay!!.year.toString()}/${(((checkInDay!!.month).toInt()) + 1).toString()}/${checkInDay!!.day.toString()}"
        tvCheckOut.text = "${checkOutDay!!.year.toString()}/${(((checkOutDay!!.month).toInt()) + 1).toString()}/${checkOutDay!!.day.toString()}"
        buttonAccept.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.confirm))
                .setMessage(getString(R.string.sure_confirm))
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }.setPositiveButton(getString(R.string.accept)) { _, _ ->
                    val reservation = Reservation()
                    reservation.paymentMethod = paymentMethod
                    reservation.totalValue = totalValue
                    reservation.checkIn = "${checkInDay!!.year.toString()}/${(((checkInDay!!.month).toInt()) + 1).toString()}/${checkInDay!!.day.toString()}"
                    reservation.checkOut = "${checkOutDay!!.year.toString()}/${(((checkOutDay!!.month).toInt()) + 1).toString()}/${checkOutDay!!.day.toString()}"
                    reservation.numDays = daysBetween.toInt()
                    reservation.baseValue = bedValue
                    reservation.currency = chosenCurrency
                    reservation.numBeds = bedNumbers
                    reservation.rate = rate
                    reservation.save()
                    toast(getString(R.string.registered_successfully_reservation))
                    val action = RoomFragmentDirections.actionRoomFragmentToMyReservationsFragment()
                    findNavController().navigate(action)
                }.show()
        }
    }

    private fun configPaymentsValues() = with(binding) {
        configBedNumbers()
        configPaymentMethod()
        configChosenCurrency()
        buttonNextPayment.setOnClickListener {
            if(chosenCurrency != getString(R.string.dash_dash) && bedNumbers != 0 && paymentMethod != getString(R.string.dash_dash)) {
                arrowDownTotal.setOnClickListener {
                    if(hiddenViewTotal.visibility == View.VISIBLE) {
                        hiddenViewTotal.hide()
                        arrowDownTotal.setImageResource(R.drawable.ic_arrow_down_24)
                    } else {
                        TransitionManager.beginDelayedTransition(cardViewRoom, AutoTransition())
                        hiddenViewTotal.show()
                        arrowDownTotal.setImageResource(R.drawable.ic_arrow_up_24)
                    }
                }
                toast(getString(R.string.payment_saved))
                hiddenViewPayment.hide()
                hiddenViewTotal.show()
                configTotalValues()
            } else {
                toast(getString(R.string.please_complete))
            }
        }
    }

    private fun FragmentRoomBinding.configChosenCurrency() {
        val viewSpinner: View = layoutInflater.inflate(R.layout.custom_spinner, null)
        val spinnerCurrency: Spinner = viewSpinner.findViewById(R.id.custom_spinner_)
        val currency: Array<out String> = resources.getStringArray(R.array.currency)
        val adapterCurrency: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currency)
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = adapterCurrency
        buttonCurrency.setOnClickListener {
            val dialogCurrency: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            dialogCurrency
                .setTitle(getString(R.string.currency_))
                .setView(viewSpinner)
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }.setPositiveButton(getString(R.string.choose)) { _, _ ->
                    if (spinnerCurrency.selectedItem.toString() != getString(R.string.dash_dash)) {
                        chosenCurrency = spinnerCurrency.selectedItem.toString()
                        textCurrency.text = chosenCurrency
                    }
                }
            val dialog: AlertDialog = dialogCurrency.create()
            dialog.show()
        }
    }

    private fun FragmentRoomBinding.configBedNumbers() {
        val viewSpinner: View = layoutInflater.inflate(R.layout.custom_spinner, null)
        val spinnerRoomNumber: Spinner = viewSpinner.findViewById(R.id.custom_spinner_)
        val beds: Array<out String> = when (bedValue) {
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
        spinnerRoomNumber.adapter = bedAdapter
        buttonRoomNumber.setOnClickListener {
            val dialogBed: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            dialogBed
                .setTitle(getString(R.string.bed_numbers))
                .setView(viewSpinner)
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }.setPositiveButton(getString(R.string.choose)) { _, _ ->
                    if (spinnerRoomNumber.selectedItem.toString() != getString(R.string.dash_dash)) {
                        val stringNumbers = spinnerRoomNumber.selectedItem.toString()
                        textRoomNumber.text = stringNumbers
                        bedNumbers = stringNumbers.toInt()
                    }
                }
            val dialog: AlertDialog = dialogBed.create()
            dialog.show()
        }
    }

    private fun FragmentRoomBinding.configPaymentMethod() {
        val viewSpinner: View = layoutInflater.inflate(R.layout.custom_spinner, null)
        val spinnerPayment: Spinner = viewSpinner.findViewById(R.id.custom_spinner_)
        val paymentMethods: Array<out String> = resources.getStringArray(R.array.payment)
        val adapterPayment: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        adapterPayment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPayment.adapter = adapterPayment
        buttonPayment.setOnClickListener {
            val dialogPayment: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            dialogPayment
                .setTitle(getString(R.string.payment_method))
                .setView(viewSpinner)
                .setPositiveButton(getString(R.string.choose)) { _, _ ->
                    if(spinnerPayment.selectedItem.toString() != getString(R.string.dash_dash)) {
                        paymentMethod = spinnerPayment.selectedItem.toString()
                        textPayment.text = paymentMethod
                    }
                }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = dialogPayment.create()
            dialog.show()
        }
    }

    private fun configDateValues() = with(binding) {
        configDateView()
        buttonSaveCheckIn.setOnClickListener {
            if(calendarViewIn.selectedDate != null && calendarViewIn.selectedDate.isAfter(calendarViewIn.currentDate)) {
                buttonSaveCheckIn.hide()
                buttonSaveCheckOut.show()
                calendarViewIn.hide()
                calendarViewOut.show()
                val selectedCheckInDate: CalendarDay = calendarViewIn.selectedDate
                checkInDay = selectedCheckInDate
                checkInDate.text = "${checkInDay!!.year.toString()}/${(((checkInDay!!.month).toInt()) + 1).toString()}/${checkInDay!!.day.toString()}"
            } else {
                toast(getString(R.string.please_date_check_in))
            }
        }
        buttonSaveCheckOut.setOnClickListener {
                if(calendarViewOut.selectedDate != null && calendarViewOut.selectedDate.isAfter(calendarViewIn.selectedDate)) {
                    buttonSaveCheckOut.hide()
                    calendarViewOut.hide()
                    buttonNext.show()
                    val selectedCheckOutDate: CalendarDay = calendarViewOut.selectedDate
                    checkOutDay = selectedCheckOutDate
                    checkOutDate.text = "${checkOutDay!!.year.toString()}/${(((checkOutDay!!.month).toInt()) + 1).toString()}/${checkOutDay!!.day.toString()}"
                } else {
                    toast(getString(R.string.please_date_check_out))
                }
        }
        buttonNext.setOnClickListener {
            if(checkInDay != null && checkOutDay != null) {
                if(checkInDay!!.year <= checkOutDay!!.year) {
                    if(checkInDay!!.month <= checkOutDay!!.month) {
                        if(checkInDay!!.day <= checkOutDay!!.day) {
                            toast(getString(R.string.date_registered))
                            hiddenViewDate.hide()
                            hiddenViewPayment.show()
                            calendarViewIn.clearSelection()
                            calendarViewOut.clearSelection()
                            buttonNext.setOnClickListener(null)
                            arrowDownPayment.setOnClickListener {
                                if(hiddenViewPayment.visibility == View.VISIBLE) {
                                    hiddenViewPayment.hide()
                                    arrowDownPayment.setImageResource(R.drawable.ic_arrow_down_24)
                                } else {
                                    TransitionManager.beginDelayedTransition(cardViewPayment, AutoTransition())
                                    hiddenViewPayment.show()
                                    arrowDownPayment.setImageResource(R.drawable.ic_arrow_up_24)
                                }
                            }
                        }
                    } else {
                        toast(getString(R.string.invalid_date))
                    }
                } else {
                    toast(getString(R.string.invalid_date))
                }
            } else {
                toast(getString(R.string.please_date))
            }
        }
    }

    private fun FragmentRoomBinding.configDateView() {
        buttonSaveCheckIn.hide()
        buttonSaveCheckOut.hide()
        calendarViewOut.hide()
        calendarIn.setOnClickListener {
            if (calendarViewIn.visibility == View.GONE) {
                calendarViewIn.show()
                buttonNext.hide()
                buttonSaveCheckIn.show()
                calendarIn.setOnClickListener(null)
                calendarOut.setOnClickListener(null)
            } else {
                calendarViewIn.hide()
                buttonSaveCheckIn.hide()
                buttonNext.show()
            }
        }
    }

    private fun configInitialSettings() {
        bedValue = args.value
        when(bedValue) {
            17.56f -> {
                binding.tvRoomType.text = getString(R.string.six_beds_dorm)
                binding.tvRoomValue.text = getString(R.string._17_56)
            }
            14.50f -> {
                binding.tvRoomType.text = getString(R.string.eight_beds_dorm)
                binding.tvRoomValue.text = getString(R.string._14_50)
            }
            else -> {
                binding.tvRoomType.text = getString(R.string.twelve_beds_dorm)
                binding.tvRoomValue.text = getString(R.string._12_01)
            }
        }
        calendarViewIn = binding.calendarViewIn
        calendarViewOut = binding.calendarViewOut
    }

    private fun configHiddenCards() {
        hiddenViewDate = binding.constraintDate
        hiddenViewDate.show()
        hiddenViewPayment = binding.constraintPayment
        hiddenViewPayment.hide()
        hiddenViewTotal = binding.constraintTotal
        hiddenViewTotal.hide()
        configCardView()
    }

    private fun configCardView() = with(binding) {
        arrowDownDate.setOnClickListener {
            if(hiddenViewDate.visibility == View.VISIBLE) {
                hiddenViewDate.hide()
                arrowDownDate.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardViewDays, AutoTransition())
                hiddenViewDate.show()
                arrowDownDate.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
    }

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentRoomBinding = FragmentRoomBinding.inflate(inflater, container, false)
}