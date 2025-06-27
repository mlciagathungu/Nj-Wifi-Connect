// ===== 7. AuthViewModel.kt =====
package com.example.njwi_ficonnect.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.njwi_ficonnect.firebase.FirebaseAuthHelper
import com.example.njwi_ficonnect.firebase.AuthResult
import com.example.njwi_ficonnect.firebase.UserProfileRepository
import com.example.njwi_ficonnect.firebase.UserProfile
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.Result.Companion.success

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser? = null, val message: String? = null) : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class AuthAction {
    object SignIn : AuthAction()
    object SignUp : AuthAction()
    object ForgotPassword : AuthAction()
    object SignOut : AuthAction()
    object DeleteAccount : AuthAction()
}

data class AuthUiState(
    val authState: AuthState = AuthState.Idle,
    val currentUser: FirebaseUser? = null,
    val isUserSignedIn: Boolean = false,
    val showPasswordReset: Boolean = false,
    val lastAction: AuthAction? = null
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        checkAuthState()
    }

    fun signIn(email: String,
               password: String,
               onProfileCheckComplete: (hasProfile: Boolean) -> Unit // ✅ callback added
    ) {
        viewModelScope.launch {
            if (!validateSignInInput(email, password)) return@launch

            updateState(AuthState.Loading, AuthAction.SignIn)

            FirebaseAuthHelper.signInWithEmail(email, password) { result ->
                when (result) {
                    is AuthResult.Success -> {
                        updateState(
                            AuthState.Success(result.user, "Welcome back!"),
                            AuthAction.SignIn
                        )
                        // ✅ Check if profile exists in Firestore
                        UserProfileRepository.checkIfProfileExists { exists ->
                            onProfileCheckComplete(exists)
                        }
                    }
                    is AuthResult.Error -> {
                        updateState(AuthState.Error(result.message), AuthAction.SignIn)
                    }
                }
            }
        }
    }

    fun signUp(fullName: String,
               phone: String,
               email: String,
               password: String,
               onSuccess: () -> Unit = {}, // 🔁 optional success callback
               onFailure: (String) -> Unit = {}   // ⚠️ error callback for UI if needed

    ) {
        viewModelScope.launch {
            if (!validateSignUpInput(fullName, phone, email, password)) {
                val message = "Please fill in all fields correctly."
                updateState(AuthState.Error(message), AuthAction.SignUp)
                onFailure(message)


                return@launch}

            updateState(AuthState.Loading, AuthAction.SignUp)

            FirebaseAuthHelper.signUpWithEmail(email, password) { result ->
                when (result) {
                    is AuthResult.Success -> {
                        createUserProfile(fullName, phone, email, result.user){success ->
                            if (success){
                                updateState(
                                    AuthState.Success(result.user, "Account created successfully!"),
                                    AuthAction.SignUp
                                )
                                onSuccess()
                            } else {updateState(
                                AuthState.Error("Failed to save profile."),
                                AuthAction.SignUp
                            )
                                onFailure("Profile created in FirebaseAuth, but failed to save Firestore profile.")
                            }

                        }
                    }
                    is AuthResult.Error -> {
                        updateState(AuthState.Error(result.message), AuthAction.SignUp)
                    }
                }
            }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            if (!FirebaseAuthHelper.isValidEmail(email)) {
                updateState(AuthState.Error("Please enter a valid email address"), AuthAction.ForgotPassword)
                return@launch
            }

            updateState(AuthState.Loading, AuthAction.ForgotPassword)

            FirebaseAuthHelper.sendPasswordResetEmail(email) { success, error ->
                if (success) {
                    updateState(
                        AuthState.Success(message = "Password reset email sent! Check your inbox."),
                        AuthAction.ForgotPassword
                    )
                } else {
                    updateState(
                        AuthState.Error(error ?: "Could not send password reset email."),
                        AuthAction.ForgotPassword
                    )
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            updateState(AuthState.Loading, AuthAction.SignOut)

            try {
                FirebaseAuthHelper.signOut()
                updateState(
                    AuthState.Success(message = "Signed out successfully"),
                    AuthAction.SignOut
                )
                checkAuthState()
            } catch (e: Exception) {
                updateState(AuthState.Error("Sign out failed: ${e.localizedMessage}"), AuthAction.SignOut)
            }
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            val user = FirebaseAuthHelper.getCurrentUser()
            if (user != null && !user.isEmailVerified) {
                updateState(AuthState.Loading, null)

                user.sendEmailVerification()
                    .addOnSuccessListener {
                        updateState(AuthState.Success(message = "Verification email sent!"), null)
                    }
                    .addOnFailureListener { e ->
                        updateState(AuthState.Error("Failed to send verification email: ${e.localizedMessage}"), null)
                    }
            } else {
                updateState(AuthState.Error("No user found or email already verified"), null)
            }
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch {
            val user = FirebaseAuthHelper.getCurrentUser()
            user?.reload()?.addOnCompleteListener {
                checkAuthState()
            }
        }
    }

    private fun createUserProfile(
        fullName: String,
        phone: String,
        email: String,
        user: FirebaseUser?,
        onResult: (Boolean) -> Unit  // ✅ Add this

    ) {
        val userProfile = UserProfile(
            name = fullName,
            phone = phone,
            email = email
        )

        UserProfileRepository.saveUserProfile(userProfile) { success, _ ->
            if (success) {
                updateState(
                    AuthState.Success(user, "Account created successfully!"),
                    AuthAction.SignUp
                )
            } else {
                updateState(
                    AuthState.Success(user, "Account created! Please complete your profile."),
                    AuthAction.SignUp
                )
            }
            checkAuthState()
        }
    }

    private fun validateSignInInput(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                updateState(AuthState.Error("Email is required"), AuthAction.SignIn)
                false
            }
            password.isBlank() -> {
                updateState(AuthState.Error("Password is required"), AuthAction.SignIn)
                false
            }
            !FirebaseAuthHelper.isValidEmail(email) -> {
                updateState(AuthState.Error("Please enter a valid email address"), AuthAction.SignIn)
                false
            }
            else -> true
        }
    }

    private fun validateSignUpInput(fullName: String, phone: String, email: String, password: String): Boolean {
        return when {
            fullName.isBlank() -> {
                updateState(AuthState.Error("Full name is required"), AuthAction.SignUp)
                false
            }
            phone.isBlank() -> {
                updateState(AuthState.Error("Phone number is required"), AuthAction.SignUp)
                false
            }
            email.isBlank() -> {
                updateState(AuthState.Error("Email is required"), AuthAction.SignUp)
                false
            }
            password.isBlank() -> {
                updateState(AuthState.Error("Password is required"), AuthAction.SignUp)
                false
            }
            !FirebaseAuthHelper.isValidEmail(email) -> {
                updateState(AuthState.Error("Please enter a valid email address"), AuthAction.SignUp)
                false
            }
            !FirebaseAuthHelper.isValidPassword(password) -> {
                updateState(AuthState.Error("Password must be at least 6 characters long"), AuthAction.SignUp)
                false
            }
            !isValidPhoneNumber(phone) -> {
                updateState(AuthState.Error("Please enter a valid phone number"), AuthAction.SignUp)
                false
            }
            else -> true
        }
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val cleanPhone = phone.replace("[^0-9]".toRegex(), "")
        return cleanPhone.matches(Regex("^(254[7][0-9]{8}|07[0-9]{8})$"))
    }

    private fun updateState(newAuthState: AuthState, action: AuthAction?) {
        _authState.value = newAuthState
        _isLoading.value = newAuthState is AuthState.Loading
        _errorMessage.value = if (newAuthState is AuthState.Error) newAuthState.message else null

        _uiState.value = _uiState.value.copy(
            authState = newAuthState,
            currentUser = FirebaseAuthHelper.getCurrentUser(),
            isUserSignedIn = FirebaseAuthHelper.isUserSignedIn(),
            lastAction = action
        )
    }

    private fun checkAuthState() {
        val currentUser = FirebaseAuthHelper.getCurrentUser()
        val isSignedIn = FirebaseAuthHelper.isUserSignedIn()

        _uiState.value = _uiState.value.copy(
            currentUser = currentUser,
            isUserSignedIn = isSignedIn
        )
    }

    fun reset() {
        _authState.value = AuthState.Idle
        _isLoading.value = false
        _errorMessage.value = null

        _uiState.value = _uiState.value.copy(
            authState = AuthState.Idle,
            lastAction = null
        )
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            reset()
        }
    }

    fun togglePasswordResetDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showPasswordReset = show)
    }

    private fun autoResetAfterDelay(delayMs: Long = 3000) {
        viewModelScope.launch {
            delay(delayMs)
            if (_authState.value is AuthState.Success) {
                reset()
            }
        }
    }

    private fun handleSuccess(authState: AuthState.Success, action: AuthAction) {
        updateState(authState, action)
        autoResetAfterDelay()
    }

    fun isFormValid(email: String, password: String): Boolean {
        return FirebaseAuthHelper.isValidEmail(email) && FirebaseAuthHelper.isValidPassword(password)
    }

    fun getPasswordStrength(password: String): PasswordStrength {
        return when {
            password.length < 6 -> PasswordStrength.WEAK
            password.length < 8 -> PasswordStrength.MEDIUM
            password.length >= 8 && password.any { it.isUpperCase() } &&
                    password.any { it.isLowerCase() } && password.any { it.isDigit() } -> PasswordStrength.STRONG
            else -> PasswordStrength.MEDIUM
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

enum class PasswordStrength(val label: String, val color: Long) {
    WEAK("Weak", 0xFFFF5722),
    MEDIUM("Medium", 0xFFFF9800),
    STRONG("Strong", 0xFF4CAF50)
}
