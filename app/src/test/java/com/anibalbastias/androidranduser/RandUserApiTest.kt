package com.anibalbastias.androidranduser

import com.anibalbastias.androidranduser.data.datasource.remote.api.RandUserApi
import com.anibalbastias.library.base.presentation.extensions.await

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest


import org.koin.test.inject

class RandUserApiTest : KoinTest {

    private val query = hashMapOf<String, String>()
    private val queryError = hashMapOf<String, String>()

    val api by inject<RandUserApi>()

    @Before
    fun start_koin() {
        startKoin {
            modules(
                apiTestModule
            )
        }
    }

    @After
    fun stop_koin() {
        stopKoin()
    }

    @Test
    fun `should get news from api`() {
        val result = runBlocking {
            api.getUsersByPage(params = query).await()
        }

        Assert.assertNotNull(result); result ?: return

        Assert.assertTrue(result.results?.isNotEmpty() == true)
    }
}

