package com.example.emptyapp.feature.gps.presentation.list

import app.cash.turbine.test
import com.example.emptyapp.core.common.result.ApiError
import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.core.designsystem.text.ErrorMessageProvider
import com.example.emptyapp.core.designsystem.text.UiText
import com.example.emptyapp.feature.gps.MainDispatcherRule
import com.example.emptyapp.feature.gps.domain.model.Gps
import com.example.emptyapp.feature.gps.domain.repository.GpsRepository
import com.example.emptyapp.feature.gps.domain.usecase.GetGpsListUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class GpsListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeGpsRepository()
    private val errorMessageProvider = ErrorMessageProvider { error ->
        UiText.DynamicString("mapped:${error.javaClass.simpleName}")
    }

    private fun createViewModel() = GpsListViewModel(
        getGpsListUseCase = GetGpsListUseCase(repository),
        errorMessageProvider = errorMessageProvider,
    )

    @Test
    fun `loads the gps list on creation`() = runTest {
        repository.listResult = ApiResult.Success(listOf(drSmith, drJones))

        val viewModel = createViewModel()

        assertEquals(listOf(drSmith, drJones), viewModel.uiState.value.items)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `failure surfaces the mapped error`() = runTest {
        repository.listResult = ApiResult.Failure(ApiError.NoNetwork)

        val viewModel = createViewModel()

        assertEquals(UiText.DynamicString("mapped:NoNetwork"), viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `retry after a failure reloads the list`() = runTest {
        repository.listResult = ApiResult.Failure(ApiError.Timeout)
        val viewModel = createViewModel()

        repository.listResult = ApiResult.Success(listOf(drSmith))
        viewModel.onEvent(GpsListEvent.Load)

        assertEquals(listOf(drSmith), viewModel.uiState.value.items)
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `clicking an item emits a navigation effect`() = runTest {
        repository.listResult = ApiResult.Success(listOf(drSmith))
        val viewModel = createViewModel()

        viewModel.effects.test {
            viewModel.onEvent(GpsListEvent.GpsClicked(drSmith.id))
            assertEquals(GpsListEffect.NavigateToDetail(drSmith.id), awaitItem())
        }
    }

    private class FakeGpsRepository : GpsRepository {
        var listResult: ApiResult<List<Gps>> = ApiResult.Success(emptyList())
        var detailResult: ApiResult<Gps> = ApiResult.Failure(ApiError.NotFound())

        override suspend fun getGpsList(clinicId: Int?, specialization: String?): ApiResult<List<Gps>> =
            listResult

        override suspend fun getGpsDetail(id: Int): ApiResult<Gps> = detailResult
    }

    private companion object {
        val drSmith = Gps(
            id = 1,
            name = "Dr Smith",
            specialization = "General Practitioner",
            clinicId = 10,
            postcode = "2000",
            yearsOfExperience = 12,
            rating = 4.7,
            languages = listOf("English"),
        )
        val drJones = drSmith.copy(id = 2, name = "Dr Jones")
    }
}
