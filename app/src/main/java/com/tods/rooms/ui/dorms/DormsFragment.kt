package com.tods.rooms.ui.dorms

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tods.rooms.R
import com.tods.rooms.databinding.FragmentDormsBinding
import com.tods.rooms.ui.base.BaseFragment
import com.tods.rooms.util.hide
import com.tods.rooms.util.show
import com.tods.rooms.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DormsFragment: BaseFragment<FragmentDormsBinding, DormsViewModel>() {
    private lateinit var hiddenView6: ConstraintLayout
    private lateinit var hiddenView8: ConstraintLayout
    private lateinit var hiddenView12: ConstraintLayout
    private var auth: FirebaseAuth = Firebase.auth
    override val viewModel: DormsViewModel by viewModels()

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentDormsBinding = FragmentDormsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configHiddenCards()
        configInitialSettings()
        configRoomPicker()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun configRoomPicker() = with(binding) {
            choose6.setOnClickListener {
                if(auth.currentUser != null) {
                    val value = 17.56f
                    val action = DormsFragmentDirections.actionDormsFragmentToRoomFragment(value)
                    findNavController().navigate(action)
                } else {
                    configDialog()
                }
            }
            choose8.setOnClickListener {
                if(auth.currentUser != null) {
                    val value = 14.50f
                    val action = DormsFragmentDirections.actionDormsFragmentToRoomFragment(value)
                    findNavController().navigate(action)
                } else {
                    configDialog()
                }
            }
            choose12.setOnClickListener {
                if(auth.currentUser != null) {
                    val value = 12.01f
                    val action = DormsFragmentDirections.actionDormsFragmentToRoomFragment(value)
                    findNavController().navigate(action)
                } else {
                    configDialog()
                }
            }
    }

    private fun configDialog() = MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.dear_user))
        .setMessage(getString(R.string.dear_user_message))
        .setPositiveButton(getString(R.string.register)) { _, _ ->
            val action = DormsFragmentDirections.actionDormsFragmentToRegisterFragment()
            findNavController().navigate(action)
        }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }.setNeutralButton(getString(R.string.login)) { _, _ ->
            val action = DormsFragmentDirections.actionDormsFragmentToLoginFragment()
            findNavController().navigate(action)
        }.show()

    private fun configInitialSettings() {
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.app_name)
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
                hiddenView6.hide()
                arrowDown6.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView6, AutoTransition())
                hiddenView6.show()
                arrowDown6.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
        arrowDown8.setOnClickListener {
            if (hiddenView8.visibility == View.VISIBLE) {
                hiddenView8.hide()
                arrowDown8.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView8, AutoTransition())
                hiddenView8.show()
                arrowDown8.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
        arrowDown12.setOnClickListener {
            if (hiddenView12.visibility == View.VISIBLE) {
                hiddenView12.hide()
                arrowDown12.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView12, AutoTransition())
                hiddenView12.show()
                arrowDown12.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dorms, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_login -> {
                val action = DormsFragmentDirections.actionDormsFragmentToLoginFragment()
                findNavController().navigate(action)
                true
            }
            R.id.menu_register -> {
                val action = DormsFragmentDirections.actionDormsFragmentToRegisterFragment()
                findNavController().navigate(action)
                true
            }
            R.id.menu_logout -> {
                auth.signOut()
                (activity as AppCompatActivity).invalidateOptionsMenu()
                toast(getString(R.string.logged_out_successfully))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if(auth.currentUser == null) {
            menu.setGroupVisible(R.id.group_logged_out, true)
        } else {
            menu.setGroupVisible(R.id.group_logged_out, false)
            menu.setGroupVisible(R.id.group_logged_in, true)
        }
        super.onPrepareOptionsMenu(menu)
    }
}