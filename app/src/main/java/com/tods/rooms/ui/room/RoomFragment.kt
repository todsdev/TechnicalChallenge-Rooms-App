package com.tods.rooms.ui.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.tods.rooms.R
import com.tods.rooms.databinding.FragmentRoomBinding
import com.tods.rooms.ui.base.BaseFragment
import com.tods.rooms.util.hide
import com.tods.rooms.util.show
import com.tods.rooms.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomFragment: BaseFragment<FragmentRoomBinding, RoomViewModel>() {
    private lateinit var hiddenViewDate: ConstraintLayout
    private lateinit var hiddenViewPayment: ConstraintLayout
    private lateinit var hiddenViewTotal: ConstraintLayout
    private lateinit var calendarViewIn: MaterialCalendarView
    private lateinit var calendarViewOut: MaterialCalendarView
    private var bedValue: Float = 0f
    private var checkInDay: CalendarDay? = null
    private var checkOutDay: CalendarDay? = null
    private val args: RoomFragmentArgs by navArgs()
    override val viewModel: RoomViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configInitialSettings()
        configHiddenCards()
        configDateValues()
        configPaymentsValues()
    }

    private fun configPaymentsValues() = with(binding) {
        buttonCurrency.setOnClickListener {

        }
        buttonPayment.setOnClickListener {

        }
        buttonRoomNumber.setOnClickListener {

        }
    }

    private fun configInitialSettings() {
        bedValue = args.value
        calendarViewIn = binding.calendarViewIn
        calendarViewOut = binding.calendarViewOut
    }

    private fun configDateValues() = with(binding) {
        configDateView()
        buttonSaveCheckIn.setOnClickListener {
            if(calendarViewIn.selectedDate != null) {
                val selectedCheckInDate: CalendarDay = calendarViewIn.selectedDate
                checkInDay = selectedCheckInDate
                calendarViewIn.hide()
                buttonSaveCheckIn.hide()
                buttonSaveCheckOut.show()
                calendarViewOut.show()
                checkInDate.text = "${checkInDay!!.year.toString()}/${checkInDay!!.month.toString()}/${checkInDay!!.day.toString()}"
                calendarIn.setOnClickListener(null)
            } else {
                toast(getString(R.string.please_date_check_in))
            }
        }
        buttonSaveCheckOut.setOnClickListener {
                if(calendarViewOut.selectedDate != null && calendarViewOut.selectedDate.isAfter(checkInDay!!)) {
                    val selectedCheckOutDate: CalendarDay = calendarViewOut.selectedDate
                    checkOutDay = selectedCheckOutDate
                    calendarViewOut.hide()
                    buttonSaveCheckOut.hide()
                    buttonNext.show()
                    checkOutDate.text = "${checkOutDay!!.year.toString()}/${checkOutDay!!.month.toString()}/${checkOutDay!!.day.toString()}"
                    calendarOut.setOnClickListener(null)
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
        calendarViewIn.hide()
        calendarViewOut.hide()
        calendarIn.setOnClickListener {
            if (calendarViewIn.visibility == View.GONE) {
                calendarViewIn.show()
                buttonNext.hide()
                buttonSaveCheckIn.show()
            } else {
                calendarViewIn.hide()
                buttonSaveCheckIn.hide()
                buttonNext.show()
            }
        }
        calendarOut.setOnClickListener {
            if (calendarViewOut.visibility == View.GONE) {
                calendarViewOut.show()
                buttonNext.hide()
                buttonSaveCheckOut.show()
            } else {
                calendarViewOut.hide()
                buttonSaveCheckOut.hide()
                buttonNext.show()
            }
        }
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
        arrowDownTotal.setOnClickListener {

        }
    }

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentRoomBinding = FragmentRoomBinding.inflate(inflater, container, false)

}