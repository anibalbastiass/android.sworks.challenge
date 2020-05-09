package com.anibalbastias.androidranduser.di

import android.content.Context
import android.net.ConnectivityManager
import com.anibalbastias.androidranduser.BuildConfig
import com.anibalbastias.androidranduser.R
import com.anibalbastias.androidranduser.data.datasource.remote.RemoteDataStore
import com.anibalbastias.androidranduser.data.datasource.remote.api.RandUserApi
import com.anibalbastias.androidranduser.domain.mapper.RandomUsersMapper
import com.anibalbastias.androidranduser.domain.repository.RemoteRepository
import com.anibalbastias.androidranduser.domain.usecase.GetRandomUsersUseCase
import com.anibalbastias.androidranduser.presentation.mapper.UiRandomUsersMapper
import com.anibalbastias.androidranduser.presentation.viewmodel.RandomUsersViewModel
import com.anibalbastias.androidranduser.ui.UsersNavigator
import com.anibalbastias.library.base.data.interceptor.FakeInterceptor
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
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

    // ViewModels
    viewModel<RandomUsersViewModel>()

    // Factories
    factoryBy<RemoteRepository, RemoteDataStore>()

    // Use Cases
    factory<GetRandomUsersUseCase>()

    // Mapper
    factory<RandomUsersMapper>()
    factory<UiRandomUsersMapper>()

    // Navigator
    factory<UsersNavigator>()
}
