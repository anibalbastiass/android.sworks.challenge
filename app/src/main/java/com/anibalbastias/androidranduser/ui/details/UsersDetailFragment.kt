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
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.presentation.mapper.UiRandomUsersMapper
import com.anibalbastias.androidranduser.presentation.viewmodel.FavoriteUsersViewModel
import com.anibalbastias.library.base.data.coroutines.Result
import com.anibalbastias.library.base.presentation.extensions.observe
import com.anibalbastias.library.uikit.extension.applyFontForToolbarTitle
import com.anibalbastias.library.uikit.extension.setArrowUpToolbar
import com.anibalbastias.library.uikit.extension.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersDetailFragment : Fragment() {

    companion object {
        const val WRITE_EXTERNAL_STORAGE_PERMISSION = 1001
    }

    private val favoriteUsersViewModel: FavoriteUsersViewModel by viewModel()
    lateinit var binding: FragmentUsersDetailBinding
    private val args: UsersDetailFragmentArgs by navArgs()
    private val uiRandomUsersMapper: UiRandomUsersMapper by inject()

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
        setClickListeners()

//        if (activity?.isWriteStoragePermissionGranted(WRITE_EXTERNAL_STORAGE_PERMISSION) == true) {
//            usersNavigator.shareNewsToEmail(act, args.itemNews)
//        } else {
//            activity?.toast(getString(R.string.error_permissions))
//        }

        with(favoriteUsersViewModel) {
            observe(getFavoriteUserByIdLiveResult, ::getFavoriteUserByIdObserver)
            observe(saveFavoriteUserLiveResult, ::saveFavoriteUserObserver)
            observe(deleteFavoriteUserLiveResult, ::deleteFavoriteUserObserver)
        }

        favoriteUsersViewModel.getFavoriteUserById(args.itemUsers.userId)
    }

    private fun setClickListeners() {
        binding.run {
            ivFavorite.setOnClickListener {
                if (args.itemUsers.isFavorite.get()) {
                    with(uiRandomUsersMapper) {
                        favoriteUsersViewModel.deleteFavoriteUser(args.itemUsers.fromUiToDomain())
                    }
                } else {
                    args.itemUsers.isFavorite.set(true)

                    with(uiRandomUsersMapper) {
                        favoriteUsersViewModel.saveFavoriteUser(args.itemUsers.fromUiToDomain())
                    }
                }
            }
        }
    }

    private fun deleteFavoriteUserObserver(result: Result<Boolean>?) {
        when (result) {
            is Result.OnSuccess -> {
                args.itemUsers.isFavorite.set(false)
                activity?.toast(getString(R.string.deleted_favorite_user))
            }
            is Result.OnError -> {
                activity?.toast(getString(R.string.error_database))
            }
        }
    }

    private fun saveFavoriteUserObserver(result: Result<Boolean>?) {
        when (result) {
            is Result.OnSuccess -> {
                args.itemUsers.isFavorite.set(true)
                activity?.toast(getString(R.string.saved_favorite_user))
            }
            is Result.OnError -> {
                activity?.toast(getString(R.string.error_database))
            }
        }
    }

    private fun getFavoriteUserByIdObserver(result: Result<DomainUserResult>?) {
        when (result) {
            is Result.OnSuccess -> {
                args.itemUsers.isFavorite.set(result.value.isFavorite)
            }
            is Result.OnError -> {
                args.itemUsers.isFavorite.set(false)
            }
        }
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
