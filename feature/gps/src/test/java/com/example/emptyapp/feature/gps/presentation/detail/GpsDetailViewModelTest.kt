package com.example.emptyapp.feature.gps.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.example.emptyapp.core.common.result.ApiError
import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.core.designsystem.text.ErrorMessageProvider
import com.example.emptyapp.core.designsystem.text.UiText
import com.example.emptyapp.feature.gps.MainDispatcherRule
import com.example.emptyapp.feature.gps.R
import com.example.emptyapp.feature.gps.domain.model.Gps
import com.example.emptyapp.feature.gps.domain.repository.GpsRepository
import com.example.emptyapp.feature.gps.domain.usecase.GetGpsDetailUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class GpsDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeGpsRepository()
    private val errorMessageProvider = ErrorMessageProvider { error ->
        UiText.DynamicString("mapped:${error.javaClass.simpleName}")
    }

    private fun createViewModel(id: Int = 1) = GpsDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf(GpsDetailArgs.GPS_ID to id)),
        getGpsDetailUseCase = GetGpsDetailUseCase(repository),
        errorMessageProvider = errorMessageProvider,
    )

    @Test
    fun `loads the detail for the id argument`() = runTest {
        repository.detailResult = ApiResult.Success(drSmith)

        val viewModel = createViewModel(id = drSmith.id)

        assertEquals(drSmith, viewModel.uiState.value.gps)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `failure surfacing uses the mapped error`() = runTest {
        repository.detailResult = ApiResult.Failure(ApiError.NotFound())

        val viewModel = createViewModel()

        assertEquals(
            UiText.DynamicString("mapped:NotFound"),
            viewModel.uiState.value.errorMessage,
        )
    }

    @Test
    fun `missing id shows the missing id error without calling the repository`() = runTest {
        val viewModel = GpsDetailViewModel(
            savedStateHandle = SavedStateHandle(),
            getGpsDetailUseCase = GetGpsDetailUseCase(repository),
            errorMessageProvider = errorMessageProvider,
        )

        assertEquals(
            UiText.StringResource(R.string.gps_detail_error_missing_id),
            viewModel.uiState.value.errorMessage,
        )
    }

    @Test
    fun `retry reloads the detail`() = runTest {
        repository.detailResult = ApiResult.Failure(ApiError.Timeout)
        val viewModel = createViewModel()

        repository.detailResult = ApiResult.Success(drSmith)
        viewModel.onEvent(GpsDetailEvent.Retry)

        assertEquals(drSmith, viewModel.uiState.value.gps)
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }

    private class FakeGpsRepository : GpsRepository {
        var detailResult: ApiResult<Gps> = ApiResult.Success(drSmith)

        override suspend fun getGpsList(clinicId: Int?, specialization: String?): ApiResult<List<Gps>> =
            ApiResult.Success(emptyList())

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
    }
}
