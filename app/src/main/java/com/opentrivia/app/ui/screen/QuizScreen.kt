package com.opentrivia.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.opentrivia.advance.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    categories: List<Pair<String, String>>,
    isInputEnabled: Boolean,
    isLoading: Boolean,
    isResultVisible: Boolean,
    resultTime: String,
    resultCorrectCount: String,
    onCategorySelected: (Int) -> Unit,
    onNextClick: () -> Unit,
    onResultClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember(categories) {
        mutableStateOf(categories.firstOrNull()?.first ?: "")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Configure your option to get started",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(text = "Select category:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedText,
                        onValueChange = {},
                        readOnly = true,
                        enabled = isInputEnabled,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEachIndexed { index, (name, _) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    selectedText = name
                                    expanded = false
                                    onCategorySelected(index)
                                }
                            )
                        }
                    }
                }
            }
        }

        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Button(
                onClick = onNextClick,
                enabled = isInputEnabled
            ) {
                Text("Next")
            }
        }

        if (isResultVisible) {
            Card(
                onClick = onResultClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Below are your quiz result. Click here to view more details on each questions.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = resultTime, style = MaterialTheme.typography.bodyLarge)
                    Text(text = resultCorrectCount, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
