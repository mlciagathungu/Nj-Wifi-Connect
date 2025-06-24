package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.njwi_ficonnect.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel,
    onBack: () -> Unit
) {
    // Always reflect latest profile values in state in case of profile update from elsewhere
    var name by remember { mutableStateOf(profileViewModel.userProfile.name) }
    var phone by remember { mutableStateOf(profileViewModel.userProfile.phone) }
    var email by remember { mutableStateOf(profileViewModel.userProfile.email) }

    // Update text fields if profile changes externally (e.g. after reload)
    LaunchedEffect(profileViewModel.userProfile) {
        name = profileViewModel.userProfile.name
        phone = profileViewModel.userProfile.phone
        email = profileViewModel.userProfile.email
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !profileViewModel.isUpdating
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !profileViewModel.isUpdating
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !profileViewModel.isUpdating
            )
            if (profileViewModel.errorMessage != null) {
                Text(
                    profileViewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    profileViewModel.updateProfile(
                        name.trim(), phone.trim(), email.trim(),
                        onSuccess = onBack // Go back on success
                    )
                },
                enabled = !profileViewModel.isUpdating
                        && name.isNotBlank()
                        && phone.isNotBlank()
                        && email.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (profileViewModel.isUpdating) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text("Save Changes")
                }
            }
        }
    }
}