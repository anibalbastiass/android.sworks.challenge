package com.anibalbastias.androidranduser.ui.details

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract.*
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Intents.Insert
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
import com.anibalbastias.library.base.presentation.extensions.isWriteContactPermissionGranted
import com.anibalbastias.library.base.presentation.extensions.observe
import com.anibalbastias.library.uikit.extension.applyFontForToolbarTitle
import com.anibalbastias.library.uikit.extension.setArrowUpToolbar
import com.anibalbastias.library.uikit.extension.toast
import kotlinx.android.synthetic.main.fragment_users_detail_content.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class UsersDetailFragment : Fragment() {

    companion object {
        const val WRITE_USER_CONTACTS_PERMISSION = 1001
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
                    with(uiRandomUsersMapper) {
                        favoriteUsersViewModel.saveFavoriteUser(args.itemUsers.fromUiToDomain())
                    }
                }
            }

            llCellButton.setOnClickListener {
                addNewContact(
                    args.itemUsers.fullName,
                    args.itemUsers.cell,
                    true,
                    args.itemUsers.email
                )
            }

            llPhoneButton.setOnClickListener {
                addNewContact(
                    args.itemUsers.fullName,
                    args.itemUsers.phone,
                    false,
                    args.itemUsers.email
                )
            }
        }
    }

    private fun addNewContact(
        fullName: String,
        phone: String,
        isMobile: Boolean,
        email: String
    ) {
        if (activity?.isWriteContactPermissionGranted(WRITE_USER_CONTACTS_PERMISSION) == true) {

            val intent = Intent(Intent.ACTION_INSERT)
            intent.type = Contacts.CONTENT_TYPE

            intent.putExtra(Insert.NAME, fullName)
            intent.putExtra(
                Insert.PHONE_TYPE,
                if (isMobile) Phone.TYPE_MOBILE else Phone.TYPE_HOME
            )
            intent.putExtra(Insert.PHONE, phone)
            intent.putExtra(Insert.EMAIL, email)
            activity?.startActivity(intent)

        } else {
            activity?.toast(getString(R.string.error_permissions))
        }
    }

    private fun deleteFavoriteUserObserver(result: Result<DomainUserResult>?) {
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

    private fun saveFavoriteUserObserver(result: Result<DomainUserResult>?) {
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
                args.itemUsers.isFavorite.set(true)
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
            WRITE_USER_CONTACTS_PERMISSION -> {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    activity?.toast(getString(R.string.error_permissions))
                }
            }
            else -> {

            }
        }
    }

}
