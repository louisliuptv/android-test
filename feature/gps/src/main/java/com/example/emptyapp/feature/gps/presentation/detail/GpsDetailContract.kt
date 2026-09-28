package com.example.emptyapp.feature.gps.presentation.detail

import com.example.emptyapp.core.common.mvi.UiEffect
import com.example.emptyapp.core.common.mvi.UiEvent
import com.example.emptyapp.core.common.mvi.UiState
import com.example.emptyapp.core.designsystem.text.UiText
import com.example.emptyapp.feature.gps.domain.model.Gps

object GpsDetailArgs {
    const val GPS_ID = "gpsId"
}

data class GpsDetailState(
    val gps: Gps? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
) : UiState

sealed interface GpsDetailEvent : UiEvent {
    data object Retry : GpsDetailEvent
}

sealed interface GpsDetailEffect : UiEffect
