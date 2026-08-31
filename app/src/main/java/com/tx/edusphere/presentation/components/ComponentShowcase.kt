package com.tx.edusphere.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.theme.TXEduSphereTheme

@Composable
fun ComponentShowcase() {
    var textValue by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Buttons", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        AppButton(text = "Primary Button", onClick = {})
        Spacer(Modifier.height(8.dp))
        AppButton(text = "Secondary Button", isSecondary = true, onClick = {})
        Spacer(Modifier.height(8.dp))
        AppButton(text = "Loading Button", isLoading = true, onClick = {})

        Spacer(Modifier.height(24.dp))
        Text("Cards & Stats", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        StatCard(
            title = "Total Students",
            value = "1,250",
            icon = Icons.Default.Group,
            subtitle = "+12% from last month"
        )
        Spacer(Modifier.height(16.dp))
        AppCard {
            Text("This is a generic AppCard content.")
        }

        Spacer(Modifier.height(24.dp))
        Text("Inputs", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        AppTextField(
            value = textValue,
            onValueChange = { textValue = it },
            label = "Email Address",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )
        Spacer(Modifier.height(8.dp))
        AppTextField(
            value = "",
            onValueChange = {},
            label = "Password",
            isPassword = true
        )

        Spacer(Modifier.height(24.dp))
        Text("Search", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        SearchBar(query = "", onQueryChange = {})

        Spacer(Modifier.height(24.dp))
        Text("Empty State Sample", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        AppEmptyState(
            icon = Icons.Default.Info,
            title = "Nothing Here",
            description = "This is what an empty state looks like."
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComponentShowcasePreview() {
    TXEduSphereTheme {
        ComponentShowcase()
    }
}
