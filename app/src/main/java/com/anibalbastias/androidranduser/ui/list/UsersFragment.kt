package com.anibalbastias.androidranduser.ui.list

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.anibalbastias.androidranduser.R
import com.anibalbastias.androidranduser.databinding.FragmentUsersBinding
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.presentation.mapper.UiRandomUsersMapper
import com.anibalbastias.androidranduser.presentation.model.UiUserResult
import com.anibalbastias.androidranduser.presentation.model.UiWrapperUserResult
import com.anibalbastias.androidranduser.presentation.state.SearchState
import com.anibalbastias.androidranduser.presentation.viewmodel.FavoriteUsersViewModel
import com.anibalbastias.androidranduser.presentation.viewmodel.RandomUsersViewModel
import com.anibalbastias.androidranduser.ui.UsersNavigator
import com.anibalbastias.library.base.data.coroutines.Result
import com.anibalbastias.library.base.presentation.adapter.base.BaseBindClickHandler
import com.anibalbastias.library.base.presentation.adapter.base.SingleLayoutBindRecyclerAdapter
import com.anibalbastias.library.base.presentation.databinding.paginationForRecyclerScroll
import com.anibalbastias.library.base.presentation.databinding.runLayoutAnimation
import com.anibalbastias.library.base.presentation.extensions.isNetworkAvailable
import com.anibalbastias.library.base.presentation.extensions.observe
import com.anibalbastias.library.base.presentation.viewmodel.PaginationViewModel
import com.anibalbastias.library.uikit.extension.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment : Fragment(), BaseBindClickHandler<UiUserResult>, UserItemListener,
    FavoriteUserItemListener {

    private val randomUsersViewModel: RandomUsersViewModel by viewModel()
    private val paginationViewModel: PaginationViewModel<UiUserResult> by viewModel()
    private val favoriteUsersViewModel: FavoriteUsersViewModel by viewModel()

    private val connectionManager: ConnectivityManager by inject()
    private val uiRandomUsersMapper: UiRandomUsersMapper by inject()
    private val usersNavigator: UsersNavigator by inject()
    private val usersAdapter: UsersAdapter by inject()
    private val favoriteUsersAdapter: FavoriteUsersAdapter by inject()

    lateinit var binding: FragmentUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind<ViewDataBinding>(view) as FragmentUsersBinding
        binding.lifecycleOwner = this

        paginationViewModel.run {
            binding.isLoading = isLoading
            binding.isError = isError
        }

        initToolbar()
        usersNavigator.init(view)

        with(randomUsersViewModel) {
            observe(state, ::stateObserver)
            observe(usersLiveResult, ::newsObserver)
        }

        with(favoriteUsersViewModel) {
            observe(getFavoriteUsersLiveResult, ::getFavoriteUsersObserver)
            observe(saveFavoriteUserLiveResult, ::saveFavoriteUserObserver)
            observe(deleteFavoriteUserLiveResult, ::deleteFavoriteUserObserver)
        }

        binding.srlUsers.initSwipe {
            paginationViewModel.run {
                offset.set(0)
                randomUsersViewModel.getUsers(offset.get())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.search_menu, menu)
        val item: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val adapter = (binding.rvUsers.adapter
                        as? SingleLayoutBindRecyclerAdapter<UiUserResult>)
                adapter?.getFilter()?.filter(query)
                return false
            }
        })
    }

    private fun initToolbar() {
        binding.tbUsers.run {
            applyFontForToolbarTitle(activity!!)
            setNoArrowUpToolbar(activity!!)
        }
    }

    private fun stateObserver(state: SearchState?) {
        state ?: return

        randomUsersViewModel.run {
            (usersLiveResult.value as? Result.OnSuccess<List<DomainUserResult>>)?.let { result ->
                newsObserver(result)
            } ?: getUsers(paginationViewModel.offset.get())
        }
    }

    private fun newsObserver(result: Result<List<DomainUserResult>>?) {
        when (result) {
            is Result.OnLoading -> {
                if (!paginationViewModel.isLoadingMorePages.get()) {
                    binding.isLoading?.set(true)
                    binding.isError?.set(false)
                }
            }
            is Result.OnSuccess -> {

                binding.isLoading?.set(false)
                binding.isError?.set(false)
                binding.srlUsers.isRefreshing = false

                val wrapperResult = with(uiRandomUsersMapper) {
                    UiWrapperUserResult(items = result.value.map {
                        paginationViewModel.pageSize = it.pageSize
                        it.fromDomainToUi()
                    })
                }

                paginationViewModel.setPagination(
                    adapter = usersAdapter,
                    items = wrapperResult?.items as ArrayList<UiUserResult>,
                    bodyHasItems = { items ->
                        if (items?.isNotEmpty() == true) {
                            usersAdapter.clickHandler = this@UsersFragment
                            usersAdapter.favoriteClickHandler = this@UsersFragment
                            usersAdapter.items = (items as? MutableList<UiUserResult?>)!!
                            setAdapterByData()
                        } else {
                            // Show Empty View
                            paginationViewModel.isEmpty.set(true)
                        }
                    }, bodyNoItems = { items ->
                        usersAdapter.clickHandler = this@UsersFragment
                        usersAdapter.favoriteClickHandler = this@UsersFragment
                        usersAdapter.items = (items as? MutableList<UiUserResult?>)!!
                        setAdapterByData()
                        // Show Empty View
                        paginationViewModel.isEmpty.set(true)
                    })

                favoriteUsersViewModel.getFavoriteUsers()
            }
            is Result.OnError -> {
                binding.isLoading?.set(false)
                binding.isError?.set(true)
                binding.srlUsers.isRefreshing = false

                if (connectionManager.isNetworkAvailable()) {
                    activity?.toast(getString(R.string.error_connection))
                } else {
                    activity?.toast(getString(R.string.error_connection_internet))
                }
            }
        }
    }

    private fun deleteFavoriteUserObserver(result: Result<DomainUserResult>?) {
        when (result) {
            is Result.OnSuccess -> {
                usersAdapter.items.map { uiItem ->
                    if (uiItem?.userId == result.value.userId) {
                        uiItem.isFavorite.set(false)
                    }
                }
                favoriteUsersViewModel.getFavoriteUsers()
            }
            is Result.OnError -> {
                activity?.toast(getString(R.string.error_database))
            }
        }
    }

    private fun saveFavoriteUserObserver(result: Result<DomainUserResult>?) {
        when (result) {
            is Result.OnSuccess -> {
                usersAdapter.items.map { uiItem ->
                    if (uiItem?.userId == result.value.userId) {
                        uiItem.isFavorite.set(true)
                    }
                }
                favoriteUsersViewModel.getFavoriteUsers()
            }
            is Result.OnError -> {
                activity?.toast(getString(R.string.error_database))
            }
        }
    }

    private fun getFavoriteUsersObserver(result: Result<List<DomainUserResult>>?) {
        when (result) {
            is Result.OnSuccess -> {
                // First refresh vertical view
                val users = usersAdapter.items

                if (result.value.isEmpty()) {
                    users.map { uiItem ->
                        uiItem?.isFavorite?.set(false)
                    }
                } else {
                    var favIds = ""
                    result.value.map {
                        favIds += "$it&"
                    }

                    users.map { uiItem ->
                        if (favIds.contains(uiItem?.userId!!)) {
                            uiItem.isFavorite.set(true)
                        } else {
                            uiItem.isFavorite.set(false)
                        }
                    }
                }

                // After refresh horizontal view
                if (result.value.isEmpty()) {
                    binding.cvFavoriteUserContainer.gone()
                } else {
                    binding.cvFavoriteUserContainer.visible()

                    binding.rvFavoriteUsers.let { rv ->
                        if (rv.adapter == null) {
                            rv.setHasFixedSize(true)
                            favoriteUsersAdapter.favoriteClickHandler = this@UsersFragment
                            rv.adapter = favoriteUsersAdapter
                        }

                        val items =
                            with(uiRandomUsersMapper) { result.value.map { it.fromDomainToUi() } }.toMutableList()
                        favoriteUsersAdapter.items = items.toMutableList()
                        favoriteUsersAdapter.notifyDataSetChanged()
                    }
                }
            }
            is Result.OnError -> {
                activity?.toast(getString(R.string.error_database))
            }
        }
    }

    override fun onClickView(view: View, item: UiUserResult) {
        usersNavigator.navigateToUsersDetails(view, item)
    }


    override fun onUserFavoriteClick(view: View, item: UiUserResult) {
        usersNavigator.navigateToUsersDetails(view, item)
    }

    override fun onFavoriteClick(item: UiUserResult) {
        if (item.isFavorite.get()) {
            with(uiRandomUsersMapper) {
                item.isFavorite.set(false)
                favoriteUsersViewModel.deleteFavoriteUser(item.fromUiToDomain())
            }
        } else {
            with(uiRandomUsersMapper) {
                item.isFavorite.set(true)
                favoriteUsersViewModel.saveFavoriteUser(item.fromUiToDomain())
            }
        }
    }

    private fun setAdapterByData() {
        context?.let {
            val tdLayoutManager = GridLayoutManager(it, 3)

            binding.rvUsers.let { rv ->
                rv.setHasFixedSize(true)
                rv.layoutManager = tdLayoutManager
                rv.adapter = usersAdapter

                paginationViewModel.run {
                    rv.paginationForRecyclerScroll(
                        itemPosition = itemPosition,
                        lastPosition = lastPosition,
                        offset = offset,
                        listSize = usersAdapter.items.size,
                        pageSize = pageSize,
                        isLoadingMorePages = isLoadingMorePages
                    ) {
                        binding.isLoading?.set(false)
                        randomUsersViewModel.getUsers(offset.get())
                    }
                    rv.scrollToPosition(itemPosition.get())
                }
                rv.runLayoutAnimation()
            }
        }
    }
}
