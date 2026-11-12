package com.example.simpleapp_77742nathalia.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.simpleapp_77742nathalia.ui.viewmodel.ExceptionLog

/**
 * Section 1: Custom Exception Component (Exceptions 2).
 * Demonstrates:
 * 1. Provoking a custom exception when word length > 10 characters.
 * 2. An open, extendable exception class (`WordTooLongException`) and its subclass (`ExceededWordLimitException`).
 */
@Composable
fun CustomExceptionSection(
    wordInputText: String,
    useSubclass: Boolean,
    wordLog: ExceptionLog?,
    onUpdateWordInput: (String) -> Unit,
    onToggleUseSubclass: (Boolean) -> Unit,
    onCheckWordLength: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Exceptions 2 — Custom Exception",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Custom extendable exception 'WordTooLongException' (marked open). " +
                        "Provoked when a word exceeds 10 characters in length.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = wordInputText,
                        onValueChange = onUpdateWordInput,
                        label = { Text("Word to check length (Limit: 10 chars)") },
                        supportingText = { Text("Current length: ${wordInputText.length} characters") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Switch to test Base Exception vs Extended Subclass Exception
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (useSubclass) "Throw Extended Subclass (ExceededWordLimitException)" else "Throw Base Open Exception (WordTooLongException)",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Switch(
                            checked = useSubclass,
                            onCheckedChange = onToggleUseSubclass
                        )
                    }

                    Button(
                        onClick = onCheckWordLength,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Check Word Length & Provoke Exception")
                    }

                    if (wordLog != null) {
                        LogCard(log = wordLog)
                    }
                }
            }
        }
    }
}
