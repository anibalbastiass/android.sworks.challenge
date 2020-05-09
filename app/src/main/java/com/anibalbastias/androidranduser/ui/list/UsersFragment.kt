package com.anibalbastias.androidranduser.ui.list

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.anibalbastias.androidranduser.R
import com.anibalbastias.androidranduser.databinding.FragmentUsersBinding
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.presentation.mapper.UiRandomUsersMapper
import com.anibalbastias.androidranduser.presentation.model.UiUserResult
import com.anibalbastias.androidranduser.presentation.model.UiWrapperUserResult
import com.anibalbastias.androidranduser.presentation.state.SearchState
import com.anibalbastias.androidranduser.presentation.viewmodel.RandomUsersViewModel
import com.anibalbastias.androidranduser.ui.UsersNavigator
import com.anibalbastias.library.base.data.coroutines.Result
import com.anibalbastias.library.base.presentation.extensions.isNetworkAvailable
import com.anibalbastias.library.base.presentation.extensions.observe
import com.anibalbastias.library.uikit.adapter.base.BaseBindClickHandler
import com.anibalbastias.library.uikit.adapter.base.SingleLayoutBindRecyclerAdapter
import com.anibalbastias.library.uikit.adapter.filter.FilterWordListener
import com.anibalbastias.library.uikit.databinding.paginationForRecyclerScroll
import com.anibalbastias.library.uikit.extension.applyFontForToolbarTitle
import com.anibalbastias.library.uikit.extension.initSwipe
import com.anibalbastias.library.uikit.extension.setNoArrowUpToolbar
import com.anibalbastias.library.uikit.extension.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class UsersFragment : Fragment(),
    BaseBindClickHandler<UiUserResult>,
    FilterWordListener<UiUserResult> {

    private val connectionManager: ConnectivityManager by inject()
    private val randomUsersViewModel: RandomUsersViewModel by viewModel()
    private val uiRandomUsersMapper: UiRandomUsersMapper by inject()
    private val usersNavigator: UsersNavigator by inject()

    lateinit var binding: FragmentUsersBinding
    private var isLoading = ObservableBoolean(false)
    private var isError = ObservableBoolean(false)
    private var itemPosition: ObservableInt = ObservableInt(0)
    private var page = ObservableInt(0)

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
        binding.callback = this
        binding.isLoading = isLoading
        binding.isError = isError
        binding.callback = this
        binding.lifecycleOwner = this

        initToolbar()
        usersNavigator.init(view)

        with(randomUsersViewModel) {
            observe(state, ::stateObserver)
            observe(usersLiveResult, ::newsObserver)
        }

        binding.srlNews?.initSwipe {
            randomUsersViewModel.getUsers(page.get())
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
                val adapter = (binding.rvNews.adapter
                        as? SingleLayoutBindRecyclerAdapter<UiUserResult>)
                adapter?.getFilter()?.filter(query)
                return false
            }
        })
    }

    private fun initToolbar() {
        binding.tbNews.run {
            applyFontForToolbarTitle(activity!!)
            setNoArrowUpToolbar(activity!!)
        }
    }

    private fun stateObserver(state: SearchState?) {
        state ?: return

        randomUsersViewModel.run {
            (usersLiveResult.value as? Result.OnSuccess<List<DomainUserResult>>)?.let { result ->
                newsObserver(result)
            } ?: getUsers(page.get())
        }
    }

    private fun newsObserver(result: Result<List<DomainUserResult>>?) {
        when (result) {
            is Result.OnLoading -> {
                binding.isLoading?.set(true)
                binding.isError?.set(false)
            }
            is Result.OnSuccess -> {
                binding.isLoading?.set(false)
                binding.isError?.set(false)
                binding.srlNews.isRefreshing = false

                binding.rvNews.paginationForRecyclerScroll(itemPosition)

                with(uiRandomUsersMapper) {
                    binding.users =
                        UiWrapperUserResult(items = result.value.map { it.fromDomainToUi() })
                }
            }
            is Result.OnError -> {
                binding.isLoading?.set(false)
                binding.isError?.set(true)
                binding.srlNews.isRefreshing = false

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

    override fun onFilterByWord(
        word: String,
        selectedItem: UiUserResult,
        filteredItems: ArrayList<UiUserResult>
    ) {
        if (selectedItem.fullName.contains(word, ignoreCase = true)) {
            filteredItems.add(selectedItem)
        }
    }
}
