package com.anandarh.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.repositories.AuthRepository
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.utils.DummyData
import com.anandarh.storyapp.utils.MainCoroutineRule
import com.anandarh.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var registerViewModel: RegisterViewModel

    private val dummyResponse = DummyData.generateDummyDefaultResponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(authRepository)
    }

    @Test
    fun `Register Success`(): Unit = mainCoroutineRule.runBlockingTest {

        val response: Flow<DataState<ResponseModel>> = flow {
            emit(DataState.Success(dummyResponse))
        }

        Mockito.`when`(
            authRepository.register(
                email = "test@gmail.com",
                password = "test1234",
                name = "test"
            )
        ).thenReturn(response)

        registerViewModel.register(
            email = "test@gmail.com",
            password = "test1234",
            name = "test"
        )

        val actualResponse = registerViewModel.registerState.getOrAwaitValue()

        assertNotNull(actualResponse)
        assertEquals(
            actualResponse,
            DataState.Success(dummyResponse)
        )
        assertTrue(actualResponse is DataState.Success)
    }

    @Test
    fun `Register Failed`(): Unit = mainCoroutineRule.runBlockingTest {

        val response: Flow<DataState<ResponseModel>> = flow {
            emit(DataState.Error(Exception()))
        }

        Mockito.`when`(
            authRepository.register(
                email = "test@gmail.com",
                password = "test1234",
                name = "test"
            )
        ).thenReturn(response)

        registerViewModel.register(
            email = "test@gmail.com",
            password = "test1234",
            name = "test"
        )

        val actualResponse = registerViewModel.registerState.getOrAwaitValue()

        assertNotNull(actualResponse)
        assertTrue(actualResponse is DataState.Error)
    }

}