package com.example.emptyapp.feature.gps.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyapp.core.designsystem.text.asString
import com.example.emptyapp.feature.gps.R
import com.example.emptyapp.feature.gps.domain.model.Clinic
import com.example.emptyapp.feature.gps.domain.model.Gps

@Composable
fun GpsDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GpsDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.gps_detail_back))
            }
            Text(
                text = stringResource(R.string.gps_detail_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        val errorMessage = state.errorMessage
        when {
            state.isLoading -> CenteredProgress()

            errorMessage != null -> CenteredError(
                message = errorMessage.asString(),
                onRetry = { viewModel.onEvent(GpsDetailEvent.Retry) },
            )

            state.gps != null -> GpsDetailContent(gps = state.gps!!)
        }
    }
}

@Composable
private fun GpsDetailContent(gps: Gps) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = gps.name, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = gps.specialization,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.gps_detail_rating, gps.rating.toString()))
        Text(stringResource(R.string.gps_detail_experience, gps.yearsOfExperience))
        Text(stringResource(R.string.gps_detail_postcode, gps.postcode))
        if (gps.languages.isNotEmpty()) {
            Text(stringResource(R.string.gps_detail_languages, gps.languages.joinToString()))
        }

        gps.clinic?.let { clinic ->
            Spacer(Modifier.height(8.dp))
            ClinicSection(clinic)
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.gps_detail_slots_title),
            style = MaterialTheme.typography.titleMedium,
        )
        val slots = gps.availableSlots.filter { it.available }
        if (slots.isEmpty()) {
            Text(stringResource(R.string.gps_detail_no_slots))
        } else {
            slots.forEach { slot ->
                Text(stringResource(R.string.gps_detail_slot, slot.startTime, slot.endTime))
            }
        }
    }
}

@Composable
private fun ClinicSection(clinic: Clinic) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.gps_detail_clinic_name, clinic.name),
            style = MaterialTheme.typography.titleMedium,
        )
        if (clinic.address.isNotBlank()) {
            Text(stringResource(R.string.gps_detail_clinic_address, clinic.address))
        }
        if (clinic.phoneNumber.isNotBlank()) {
            Text(stringResource(R.string.gps_detail_clinic_phone, clinic.phoneNumber))
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
private fun CenteredError(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onRetry) { Text(stringResource(R.string.gps_list_retry)) }
        }
    }
}
