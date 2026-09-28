package com.example.emptyapp.core.common.mvi

/** Marker for the immutable UI state a screen renders. */
interface UiState

/** Marker for user intents delivered to the ViewModel. */
interface UiEvent

/** Marker for one-off side effects (navigation, snackbar, ...). */
interface UiEffect
