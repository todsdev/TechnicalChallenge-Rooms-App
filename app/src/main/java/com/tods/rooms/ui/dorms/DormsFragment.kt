package com.tods.rooms.ui.dorms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.tods.rooms.R
import com.tods.rooms.databinding.FragmentDormsBinding
import com.tods.rooms.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DormsFragment: BaseFragment<FragmentDormsBinding, DormsViewModel>() {
    private lateinit var hiddenView6: ConstraintLayout
    private lateinit var hiddenView8: ConstraintLayout
    private lateinit var hiddenView12: ConstraintLayout
    override val viewModel: DormsViewModel by viewModels()

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentDormsBinding = FragmentDormsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configHiddenCards()
    }

    private fun configHiddenCards() {
        hiddenView6 = binding.constraint6
        hiddenView6.visibility = View.GONE
        hiddenView8 = binding.constraint8
        hiddenView8.visibility = View.GONE
        hiddenView12 = binding.constraint12
        hiddenView12.visibility = View.GONE
        configCardView()
    }

    private fun configCardView() = with(binding) {
        arrowDown6.setOnClickListener {
            if (hiddenView6.visibility == View.VISIBLE) {
                hiddenView6.visibility = View.GONE
                arrowDown6.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView6, AutoTransition())
                hiddenView6.visibility = View.VISIBLE
                arrowDown6.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
        arrowDown8.setOnClickListener {
            if (hiddenView8.visibility == View.VISIBLE) {
                hiddenView8.visibility = View.GONE
                arrowDown8.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView8, AutoTransition())
                hiddenView8.visibility = View.VISIBLE
                arrowDown8.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
        arrowDown12.setOnClickListener {
            if (hiddenView12.visibility == View.VISIBLE) {
                hiddenView12.visibility = View.GONE
                arrowDown12.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView12, AutoTransition())
                hiddenView12.visibility = View.VISIBLE
                arrowDown12.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
    }
}