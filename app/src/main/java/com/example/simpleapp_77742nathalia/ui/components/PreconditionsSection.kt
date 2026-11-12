package com.example.simpleapp_77742nathalia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.simpleapp_77742nathalia.ui.viewmodel.ExceptionLog

/**
 * Section 1: Precondition Functions Component (Exceptions 1).
 * Demonstrates:
 * 1. Precondition function `check(isLightOn)` throwing `IllegalStateException`.
 * 2. Precondition function `require(number >= 0)` throwing `IllegalArgumentException`.
 * 3. Exception handling (try-catch) to catch and display exception info.
 */
@Composable
fun PreconditionsSection(
    isLightOn: Boolean,
    lightLog: ExceptionLog?,
    onToggleLight: (Boolean) -> Unit,
    onExecuteLightAction: () -> Unit,
    numberInputText: String,
    numberLog: ExceptionLog?,
    onUpdateNumberInput: (String) -> Unit,
    onValidateNumberInput: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Exceptions 1 — Preconditions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Demonstrates Kotlin Precondition Functions (check & require) and Exception Handling.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            // --- Scenario A: Light Switch (check precondition) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = if (isLightOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Scenario A: Light State",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = isLightOn,
                            onCheckedChange = onToggleLight
                        )
                    }

                    Text(
                        text = "Precondition: check(isLightOn) { ... } -> Throws IllegalStateException if light is OFF.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = onExecuteLightAction,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Execute Action Requiring Light")
                    }

                    if (lightLog != null) {
                        LogCard(log = lightLog)
                    }
                }
            }

            // --- Scenario B: Non-Negative Number Check (require precondition) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Numbers,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scenario B: Non-Negative Number",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Precondition: require(number >= 0) { ... } -> Throws IllegalArgumentException if number < 0.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = numberInputText,
                        onValueChange = onUpdateNumberInput,
                        label = { Text("Input Number (e.g., 10 or -5)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = onValidateNumberInput,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Validate Number Input")
                    }

                    if (numberLog != null) {
                        LogCard(log = numberLog)
                    }
                }
            }
        }
    }
}

@Composable
fun LogCard(log: ExceptionLog, modifier: Modifier = Modifier) {
    val bgColor = if (log.isSuccess) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
    val contentColor = if (log.isSuccess) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
    val icon = if (log.isSuccess) Icons.Default.CheckCircle else Icons.Default.Warning

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = log.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor
                )
            }

            if (log.exceptionType != "None") {
                Text(
                    text = "Caught Exception: ${log.exceptionType}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = contentColor
                )
            }

            Text(
                text = log.message,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
        }
    }
}
