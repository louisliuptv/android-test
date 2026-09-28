package com.example.emptyapp.feature.gps.presentation.list

import com.example.emptyapp.core.common.mvi.UiEffect
import com.example.emptyapp.core.common.mvi.UiEvent
import com.example.emptyapp.core.common.mvi.UiState
import com.example.emptyapp.core.common.ui.UiText
import com.example.emptyapp.feature.gps.domain.model.Gps

data class GpsListState(
    val items: List<Gps> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: UiText? = null,
) : UiState {
    val isEmpty: Boolean
        get() = items.isEmpty() && !isLoading && errorMessage == null
}

sealed interface GpsListEvent : UiEvent {
    data object Load : GpsListEvent
    data object Refresh : GpsListEvent
    data class GpsClicked(val id: Int) : GpsListEvent
}

sealed interface GpsListEffect : UiEffect {
    data class NavigateToDetail(val id: Int) : GpsListEffect
}
