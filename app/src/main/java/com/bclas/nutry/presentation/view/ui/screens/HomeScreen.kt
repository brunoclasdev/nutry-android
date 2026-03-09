package com.bclas.nutry.presentation.view.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(
    onRegisterPatientClick: () -> Unit,
    onPatientListClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        val isTablet = maxWidth >= 840.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = if (isTablet) 900.dp else 520.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Nutry",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Selecione a funcionalidade desejada.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (isTablet) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onRegisterPatientClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cadastro de pacientes")
                    }
                    Button(
                        onClick = onPatientListClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Pacientes cadastrados")
                    }
                }

            } else {
                Button(
                    onClick = onRegisterPatientClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cadastro de pacientes")
                }
                Button(
                    onClick = onPatientListClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pacientes cadastrados")
                }
            }
        }
    }
}
