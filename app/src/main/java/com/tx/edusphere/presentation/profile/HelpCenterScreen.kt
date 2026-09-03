package com.tx.edusphere.presentation.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.components.SearchBar

data class FAQItem(val question: String, val answer: String)

@Composable
fun HelpCenterScreen(
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val allFaqs = listOf(
        FAQItem("How to view attendance?", "Go to the Home or Explore screen and select 'Attendance' to view your daily and monthly records."),
        FAQItem("Where can I find assignments?", "Assignments are available on the Home screen under 'Quick Overview' and in the Explore section."),
        FAQItem("How to pay school fees?", "Navigate to Explore > Fees. You can view your pending fees and pay them online."),
        FAQItem("How to update my profile?", "Go to the Profile tab and click on the 'Edit Profile' button to update your information."),
        FAQItem("What to do if I miss an exam?", "Please contact the school administration immediately through the 'Contact School' option in your profile.")
    )

    val filteredFaqs = if (searchQuery.isBlank()) allFaqs else allFaqs.filter { it.question.contains(searchQuery, ignoreCase = true) }

    Scaffold(
        topBar = {
            AppTopBar(title = "Help Center", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search FAQs...",
                modifier = Modifier.padding(20.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 20.dp)) {
                items(filteredFaqs) { faq ->
                    FAQCard(faq)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun FAQCard(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = faq.question, style = MaterialTheme.typography.bodyLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, modifier = Modifier.weight(1f))
                Icon(imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
            }
            AnimatedVisibility(visible = expanded) {
                Text(text = faq.answer, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}
