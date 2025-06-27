package com.example.njwi_ficonnect.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.njwi_ficonnect.R
import androidx.lifecycle.viewmodel.compose.viewModel

val PrimaryBlue = Color(0xFF32EF07)
val PrimaryPurple = Color(0xFFCACE0C)

@Composable
fun WifiLoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel(),
    onForgotPasswordClicked: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE0E8F0),
                        Color(0xFFE8E0F0)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F3F1))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_wifi_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("NJ Connect", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Affordable Wi-Fi for Everyone", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.97f).heightIn(min = 440.dp, max = 550.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFCAEAA9))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE0E0E0)),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TabButton("Sign In", selectedTab == 0, {
                                selectedTab = 0
                                emailAddress = ""
                                password = ""
                                fullName = ""
                                phoneNumber = ""
                                confirmPassword = ""
                                localError = null
                                viewModel.reset()
                            }, Modifier.weight(1f).padding(2.dp))
                            TabButton("Create Account", selectedTab == 1, {
                                selectedTab = 1
                                phoneNumber = ""
                                password = ""
                                fullName = ""
                                emailAddress = ""
                                confirmPassword = ""
                                localError = null
                                viewModel.reset()
                            }, Modifier.weight(1f).padding(2.dp))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        localError?.let {
                            Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
                        }
                        if (authState is AuthState.Error) {
                            Text((authState as AuthState.Error).message, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
                        }

                        if (selectedTab == 0) {
                            OutlinedTextField(
                                value = emailAddress,
                                onValueChange = { emailAddress = it },
                                label = { Text("Email Address") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        if (selectedTab == 1) {
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Phone Number (0712345678)") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = emailAddress,
                                onValueChange = { emailAddress = it },
                                label = { Text("Email Address") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off), contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = Color.LightGray
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        if (selectedTab == 1) {
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = { Text("Confirm Password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off), contentDescription = null)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(
                            onClick = {
                                localError = null
                                if (selectedTab == 0) {
                                    viewModel.signIn(emailAddress, password){ hasProfile ->
                                        if (hasProfile) {
                                            navController.navigate("home") // or whatever route
                                        } else {
                                            navController.navigate("edit_profile") // prompt to finish profile
                                        }
                                    }
                                } else {
                                    if (password != confirmPassword) {
                                        localError = "Passwords do not match"
                                    } else {
                                        viewModel.signUp(fullName, phoneNumber, emailAddress, password)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(38.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(8.dp),
                            enabled = authState !is AuthState.Loading
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().background(
                                    brush = Brush.horizontalGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (authState is AuthState.Loading) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                                } else {
                                    Text(
                                        text = if (selectedTab == 0) "Sign In" else "Create Account",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        if (selectedTab == 0) {
                            TextButton(onClick = onForgotPasswordClicked) {
                                Text("Forgot Password?", color = PrimaryBlue, fontSize = 12.sp)
                            }
                        }

                        if (selectedTab == 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "By continuing, you agree to our Terms of Service and Privacy Policy",
                                fontSize = 9.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }

                        if (authState is AuthState.Success) {
                            LaunchedEffect(authState, selectedTab) {
                                viewModel.reset()
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            if (selectedTab == 0) {
                Text(
                    text = "By continuing, you agree to our Terms of Service and Privacy Policy",
                    fontSize = 9.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.White else Color.Transparent,
            contentColor = if (isSelected) PrimaryBlue else Color.Gray
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 1)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWifiLoginScreen() {
    WifiLoginScreen(
        navController = rememberNavController(),
        onForgotPasswordClicked = { println("Preview: Forgot Password Clicked") }
    )
}
