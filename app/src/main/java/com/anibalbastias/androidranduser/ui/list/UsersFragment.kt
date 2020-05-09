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
import com.anibalbastias.library.base.presentation.viewmodel.PaginationViewModel
import com.anibalbastias.androidranduser.presentation.viewmodel.RandomUsersViewModel
import com.anibalbastias.androidranduser.ui.UsersNavigator
import com.anibalbastias.library.base.data.coroutines.Result
import com.anibalbastias.library.base.presentation.extensions.isNetworkAvailable
import com.anibalbastias.library.base.presentation.extensions.observe
import com.anibalbastias.library.base.presentation.adapter.base.BaseBindClickHandler
import com.anibalbastias.library.base.presentation.adapter.base.SingleLayoutBindRecyclerAdapter
import com.anibalbastias.library.base.presentation.databinding.paginationForRecyclerScroll
import com.anibalbastias.library.base.presentation.databinding.runLayoutAnimation
import com.anibalbastias.library.uikit.extension.applyFontForToolbarTitle
import com.anibalbastias.library.uikit.extension.initSwipe
import com.anibalbastias.library.uikit.extension.setNoArrowUpToolbar
import com.anibalbastias.library.uikit.extension.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment : Fragment(), BaseBindClickHandler<UiUserResult> {

    private val randomUsersViewModel: RandomUsersViewModel by viewModel()
    private val paginationViewModel: PaginationViewModel<UiUserResult> by viewModel()

    private val connectionManager: ConnectivityManager by inject()
    private val uiRandomUsersMapper: UiRandomUsersMapper by inject()
    private val usersNavigator: UsersNavigator by inject()
    private val usersAdapter: UsersAdapter by inject()

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
                    items = wrapperResult.items as ArrayList<UiUserResult>,
                    bodyHasItems = { items ->
                        if (items?.isNotEmpty() == true) {
                            usersAdapter.clickHandler = this@UsersFragment
                            usersAdapter.items = (items as? MutableList<UiUserResult?>)!!
                            setAdapterByData()
                        } else {
                            // Show Empty View
                            paginationViewModel.isEmpty.set(true)
                        }
                    }, bodyNoItems = { items ->
                        usersAdapter.clickHandler = this@UsersFragment
                        usersAdapter.items = (items as? MutableList<UiUserResult?>)!!
                        setAdapterByData()
                        // Show Empty View
                        paginationViewModel.isEmpty.set(true)
                    })
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

    override fun onClickView(view: View, item: UiUserResult) {
        usersNavigator.navigateToUsersDetails(view, item)
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
