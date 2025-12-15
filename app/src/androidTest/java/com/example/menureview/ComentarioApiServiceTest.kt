package com.example.menureview

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.menureview.data.Api.ApiClient
import com.example.menureview.data.Api.ComentarioApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComentarioApiServiceTest {

    private lateinit var api: ComentarioApiService

    @Before
    fun setup() {
        api = ApiClient.create(ComentarioApiService::class.java)
    }

    @Test
    fun getComentariosByRestaurante_devuelve_200() = runBlocking {
        val response = api.getComentariosByRestaurante(1)
        assertTrue(response.isSuccessful)
    }
}