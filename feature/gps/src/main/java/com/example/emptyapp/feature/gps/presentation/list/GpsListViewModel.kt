package com.example.emptyapp.feature.gps.presentation.list

import com.example.emptyapp.core.common.mvi.BaseMviViewModel
import com.example.emptyapp.core.common.result.onFailure
import com.example.emptyapp.core.common.result.onSuccess
import com.example.emptyapp.core.designsystem.text.ErrorMessageProvider
import com.example.emptyapp.feature.gps.domain.usecase.GetGpsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GpsListViewModel @Inject constructor(
    private val getGpsListUseCase: GetGpsListUseCase,
    private val errorMessageProvider: ErrorMessageProvider,
) : BaseMviViewModel<GpsListState, GpsListEvent, GpsListEffect>(GpsListState()) {

    init {
        load(isRefresh = false)
    }

    fun onEvent(event: GpsListEvent) {
        when (event) {
            GpsListEvent.Load -> load(isRefresh = false)
            GpsListEvent.Refresh -> load(isRefresh = true)
            is GpsListEvent.GpsClicked ->
                launch { sendEffect(GpsListEffect.NavigateToDetail(event.id)) }
        }
    }

    private fun load(isRefresh: Boolean) {
        if (currentState.isLoading || currentState.isRefreshing) return

        setState {
            copy(
                isLoading = !isRefresh && items.isEmpty(),
                isRefreshing = isRefresh,
                errorMessage = null,
            )
        }

        launch {
            getGpsListUseCase()
                .onSuccess { gpsList ->
                    setState {
                        copy(items = gpsList, isLoading = false, isRefreshing = false, errorMessage = null)
                    }
                }
                .onFailure { error ->
                    setState {
                        copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = errorMessageProvider.getMessage(error),
                        )
                    }
                }
        }
    }
}
