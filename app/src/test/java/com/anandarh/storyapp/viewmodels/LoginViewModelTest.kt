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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = DummyData.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `Login Success`(): Unit = mainCoroutineRule.testScope.runTest {

        val response: Flow<DataState<ResponseModel>> = flow {
            emit(DataState.Success(dummyLoginResponse))
        }

        `when`(
            authRepository.login(
                email = "test@gmail.com",
                password = "test1234"
            )
        ).thenReturn(response)

        loginViewModel.login(
            email = "test@gmail.com",
            password = "test1234"
        )

        advanceUntilIdle()

        val actualResponse = loginViewModel.loginState.getOrAwaitValue()

        assertNotNull(actualResponse)
        assertEquals(
            actualResponse,
            DataState.Success(dummyLoginResponse)
        )
        assertTrue(actualResponse is DataState.Success)
    }

    @Test
    fun `Login Failed`(): Unit = mainCoroutineRule.testScope.runTest {

        val response: Flow<DataState<ResponseModel>> = flow {
            emit(DataState.Error(Exception("Unauthorized")))
        }

        `when`(
            authRepository.login(
                email = "test@gmail.com",
                password = "test1234"
            )
        ).thenReturn(response)

        loginViewModel.login(
            email = "test@gmail.com",
            password = "test1234"
        )

        advanceUntilIdle()

        val actualResponse = loginViewModel.loginState.getOrAwaitValue()

        assertNotNull(actualResponse)
        assertTrue(actualResponse is DataState.Error)
    }
}