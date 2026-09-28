package com.example.emptyapp.feature.gps.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.example.emptyapp.core.common.mvi.BaseMviViewModel
import com.example.emptyapp.core.common.result.onFailure
import com.example.emptyapp.core.common.result.onSuccess
import com.example.emptyapp.core.designsystem.text.ErrorMessageProvider
import com.example.emptyapp.core.designsystem.text.UiText
import com.example.emptyapp.feature.gps.R
import com.example.emptyapp.feature.gps.domain.usecase.GetGpsDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GpsDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGpsDetailUseCase: GetGpsDetailUseCase,
    private val errorMessageProvider: ErrorMessageProvider,
) : BaseMviViewModel<GpsDetailState, GpsDetailEvent, GpsDetailEffect>(GpsDetailState()) {

    private val gpsId: Int = savedStateHandle.get<Int>(GpsDetailArgs.GPS_ID) ?: INVALID_ID

    init {
        load()
    }

    fun onEvent(event: GpsDetailEvent) {
        when (event) {
            GpsDetailEvent.Retry -> load()
        }
    }

    private fun load() {
        if (gpsId == INVALID_ID) {
            setState { copy(errorMessage = UiText.StringResource(R.string.gps_detail_error_missing_id)) }
            return
        }

        setState { copy(isLoading = true, errorMessage = null) }
        launch {
            getGpsDetailUseCase(gpsId)
                .onSuccess { gps -> setState { copy(gps = gps, isLoading = false, errorMessage = null) } }
                .onFailure { error ->
                    setState {
                        copy(isLoading = false, errorMessage = errorMessageProvider.getMessage(error))
                    }
                }
        }
    }

    private companion object {
        const val INVALID_ID = -1
    }
}
