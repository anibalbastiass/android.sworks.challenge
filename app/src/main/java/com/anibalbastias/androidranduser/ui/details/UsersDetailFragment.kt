package com.anibalbastias.androidranduser.ui.details

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.anibalbastias.androidranduser.R
import com.anibalbastias.androidranduser.databinding.FragmentUsersDetailBinding
import com.anibalbastias.androidranduser.ui.UsersNavigator
import com.anibalbastias.library.base.presentation.extensions.isWriteStoragePermissionGranted
import com.anibalbastias.library.uikit.extension.applyFontForToolbarTitle
import com.anibalbastias.library.uikit.extension.setArrowUpToolbar
import com.anibalbastias.library.uikit.extension.toast
import org.koin.android.ext.android.inject

class UsersDetailFragment : Fragment() {

    companion object {
        const val WRITE_EXTERNAL_STORAGE_PERMISSION = 1001
    }

    lateinit var binding: FragmentUsersDetailBinding
    private val args: UsersDetailFragmentArgs by navArgs()
    private val usersNavigator: UsersNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_users_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind<ViewDataBinding>(view) as FragmentUsersDetailBinding
        binding.lifecycleOwner = this
        binding.usersItem = args.itemUsers

        initToolbar()

//        if (activity?.isWriteStoragePermissionGranted(WRITE_EXTERNAL_STORAGE_PERMISSION) == true) {
//            usersNavigator.shareNewsToEmail(act, args.itemNews)
//        } else {
//            activity?.toast(getString(R.string.error_permissions))
//        }
    }

    private fun initToolbar() {
        binding.toolbar.run {
            applyFontForToolbarTitle(activity!!)
            setArrowUpToolbar(activity!!)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_PERMISSION -> {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    usersNavigator.shareNewsToEmail(activity!!, args.itemNews)
                } else {
                    activity?.toast(getString(R.string.error_permissions))
                }
            }
            else -> {

            }
        }
    }

}
