package com.tx.edusphere.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppTopBar

data class LanguageItem(val code: String, val name: String)

@Composable
fun LanguageSettingsScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val currentLang by viewModel.language.collectAsState()
    val languages = listOf(
        LanguageItem("en", "English"),
        LanguageItem("es", "Español"),
        LanguageItem("fr", "Français"),
        LanguageItem("hi", "हिन्दी")
    )

    Scaffold(
        topBar = {
            AppTopBar(title = "Language", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(languages) { lang ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setLanguage(lang.code) }
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = lang.name, style = MaterialTheme.typography.bodyLarge)
                    if (currentLang == lang.code) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}
