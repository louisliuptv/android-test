package com.example.emptyapp.feature.gps.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyapp.feature.gps.R
import com.example.emptyapp.feature.gps.domain.model.Gps
import com.example.emptyapp.feature.gps.presentation.asString

@Composable
fun GpsListScreen(
    onGpsClick: (Int) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GpsListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is GpsListEffect.NavigateToDetail -> onGpsClick(effect.id)
            }
        }
    }

    GpsListContent(
        state = state,
        onRetry = { viewModel.onEvent(GpsListEvent.Load) },
        onRefresh = { viewModel.onEvent(GpsListEvent.Refresh) },
        onGpsClick = { id -> viewModel.onEvent(GpsListEvent.GpsClicked(id)) },
        onLogout = onLogout,
        modifier = modifier,
    )
}

@Composable
private fun GpsListContent(
    state: GpsListState,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onGpsClick: (Int) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.gps_list_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onRefresh) {
                Text(stringResource(R.string.gps_list_refresh))
            }
            TextButton(onClick = onLogout) {
                Text(stringResource(R.string.gps_list_logout))
            }
        }

        val errorMessage = state.errorMessage
        when {
            state.isLoading && state.items.isEmpty() -> CenteredProgress()

            errorMessage != null && state.items.isEmpty() -> CenteredMessage(
                message = errorMessage.asString(),
                actionLabel = stringResource(R.string.gps_list_retry),
                onAction = onRetry,
            )

            state.items.isEmpty() -> CenteredMessage(
                message = stringResource(R.string.gps_list_empty),
            )

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = state.items, key = { it.id }) { gps ->
                    GpsListItem(gps = gps, onClick = { onGpsClick(gps.id) })
                }
            }
        }
    }
}

@Composable
private fun GpsListItem(
    gps: Gps,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = gps.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = gps.specialization,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.gps_list_rating, gps.rating.toString()),
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = gps.clinic?.name ?: gps.postcode,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun CenteredProgress() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun CenteredMessage(
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
            if (actionLabel != null && onAction != null) {
                Button(onClick = onAction) { Text(actionLabel) }
            }
        }
    }
}
