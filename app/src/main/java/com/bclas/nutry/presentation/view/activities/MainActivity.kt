package com.bclas.nutry.presentation.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bclas.nutry.presentation.view.ui.screens.AnthropometricAssessmentScreen
import com.bclas.nutry.presentation.view.ui.theme.NutryTheme
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentUiState
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: AnthropometricAssessmentViewModel = viewModel()
                    AnthropometricAssessmentScreen(
                        state = viewModel.uiState,
                        onAction = viewModel::onAction,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun AnthropometricAssessmentPreviewContainer(
    modifier: Modifier = Modifier
) {
    AnthropometricAssessmentScreen(
        state = AnthropometricAssessmentUiState(
            weight = "80",
            height = "1.75",
            bmi = "26.12",
            waistCircumference = "85",
            hipCircumference = "102",
            waistHipRatio = "0.83",
            bodyFatPercentage = "18"
        ),
        onAction = {},
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AnthropometricAssessmentPreview() {
    NutryTheme {
        AnthropometricAssessmentPreviewContainer()
    }
}
