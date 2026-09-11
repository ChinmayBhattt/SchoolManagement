package com.tx.edusphere.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun ReportProblemScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppTopBar(title = "Report a Problem", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Help us improve", style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            
            AppTextField(value = category, onValueChange = { category = it }, label = "Category (e.g. App Crash, Bug)")
            AppTextField(
                value = description, 
                onValueChange = { description = it }, 
                label = "Description",
                singleLine = false,
                modifier = Modifier.height(150.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = "Submit Report",
                onClick = {
                    if (category.isNotBlank() && description.isNotBlank()) {
                        Toast.makeText(context, "Problem reported. Thank you!", Toast.LENGTH_LONG).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
