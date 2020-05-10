package com.anibalbastias.androidranduser.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.anibalbastias.androidranduser.BuildConfig
import com.anibalbastias.androidranduser.R
import com.anibalbastias.androidranduser.data.datasource.remote.RemoteDataStore
import com.anibalbastias.androidranduser.data.datasource.remote.api.RandUserApi
import com.anibalbastias.androidranduser.domain.Constants.DATABASE_NAME
import com.anibalbastias.androidranduser.domain.database.DatabaseDataStore
import com.anibalbastias.androidranduser.domain.database.UsersDatabase
import com.anibalbastias.androidranduser.domain.mapper.BdRandomUsersMapper
import com.anibalbastias.androidranduser.domain.mapper.RandomUsersMapper
import com.anibalbastias.androidranduser.domain.repository.DatabaseRepository
import com.anibalbastias.androidranduser.domain.repository.RemoteRepository
import com.anibalbastias.androidranduser.domain.usecase.*
import com.anibalbastias.androidranduser.presentation.mapper.UiRandomUsersMapper
import com.anibalbastias.androidranduser.presentation.model.UiUserResult
import com.anibalbastias.androidranduser.presentation.viewmodel.FavoriteUsersViewModel
import com.anibalbastias.library.base.presentation.viewmodel.PaginationViewModel
import com.anibalbastias.androidranduser.presentation.viewmodel.RandomUsersViewModel
import com.anibalbastias.androidranduser.ui.UsersNavigator
import com.anibalbastias.androidranduser.ui.list.adapter.FavoriteUsersAdapter
import com.anibalbastias.androidranduser.ui.list.adapter.UsersAdapter
import com.anibalbastias.library.base.data.interceptor.FakeInterceptor
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.factory
import org.koin.experimental.builder.factoryBy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    // Android Services
    single {
        androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    // Retrofit
    single {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val clientBuilder = OkHttpClient.Builder()

        if (BuildConfig.FLAVOR.equals("dummy")) {
            clientBuilder.addInterceptor(FakeInterceptor(androidApplication()))
        } else {
            if (BuildConfig.DEBUG) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(httpLoggingInterceptor)
            }
        }

        clientBuilder.callTimeout(1, TimeUnit.MINUTES)
        clientBuilder.connectTimeout(1, TimeUnit.MINUTES)
        clientBuilder.writeTimeout(1, TimeUnit.MINUTES)
        clientBuilder.readTimeout(1, TimeUnit.MINUTES)
        clientBuilder.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(androidApplication().getString(R.string.randuser_challenge_endpoint))
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>()
            .create(RandUserApi::class.java) as RandUserApi
    }

    // Picasso
    single {
        Picasso.get()
    }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            UsersDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    // ViewModels
    viewModel<RandomUsersViewModel>()
    viewModel<FavoriteUsersViewModel>()
    viewModel<PaginationViewModel<UiUserResult>>()

    // Factories
    factoryBy<RemoteRepository, RemoteDataStore>()
    factoryBy<DatabaseRepository, DatabaseDataStore>()

    // Use Cases
    factory<GetRandomUsersUseCase>()
    factory<GetFavoriteUserByIdUseCase>()
    factory<GetFavoriteUsersUseCase>()
    factory<SaveFavoriteUserUseCase>()
    factory<DeleteFavoriteUserUseCase>()

    // Mapper
    factory<RandomUsersMapper>()
    factory<UiRandomUsersMapper>()
    factory<BdRandomUsersMapper>()

    // Navigator
    factory<UsersNavigator>()

    // Adapter
    factory<UsersAdapter>()
    factory<FavoriteUsersAdapter>()
}
