package com.anibalbastias.androidranduser.ui

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.anibalbastias.androidranduser.presentation.model.UiUserResult
import com.anibalbastias.androidranduser.ui.list.UsersFragmentDirections


class UsersNavigator {

    companion object {
        const val KEY_ITEMS = "itemUsers"
        const val SECOND_TRANSITION = "secondTransitionName"
    }

    private var navController: NavController? = null

    fun init(view: View) {
        navController = Navigation.findNavController(view)
    }

    private fun getImageViewFromChild(view: View): ImageView {
        val cardView = (view as? CardView)
        val cl1 = (cardView?.getChildAt(0) as? ConstraintLayout)
        return (cl1?.getChildAt(0) as? ImageView)!!
    }

    fun navigateToUsersDetails(
        view: View,
        item: UiUserResult
    ) {
        val extras = FragmentNavigatorExtras(
            getImageViewFromChild(view) to SECOND_TRANSITION
        )
        ViewCompat.setTransitionName(getImageViewFromChild(view), SECOND_TRANSITION)

        navController?.navigate(
            UsersFragmentDirections.actionUsersFragmentToUsersDetailFragment(item).actionId,
            bundleOf(Pair(KEY_ITEMS, item)), null, extras
        )
    }

}