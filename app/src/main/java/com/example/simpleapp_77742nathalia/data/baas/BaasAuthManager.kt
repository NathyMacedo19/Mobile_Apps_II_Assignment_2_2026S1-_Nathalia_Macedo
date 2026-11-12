package com.example.simpleapp_77742nathalia.data.baas

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Data class representing a BaaS User.
 */
data class BaasUser(
    val uid: String,
    val email: String,
    val isAnonymous: Boolean = false,
    val provider: String = "BaaS Firebase Auth"
)

/**
 * BaaS Authentication Manager.
 * Implements Backend-as-a-Service (BaaS) authorization logic with Firebase Auth.
 */
class BaasAuthManager {

    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            // Fallback for JVM unit tests where FirebaseApp is not mocked/initialized
            null
        }
    }

    private val _currentUser = MutableStateFlow<BaasUser?>(
        try {
            auth?.currentUser?.let { toBaasUser(it) }
        } catch (e: Exception) {
            null
        }
    )
    val currentUser: StateFlow<BaasUser?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private fun toBaasUser(user: FirebaseUser): BaasUser {
        return BaasUser(
            uid = user.uid,
            email = user.email ?: (if (user.isAnonymous) "anonymous@baas.service" else ""),
            isAnonymous = user.isAnonymous,
            provider = if (user.isAnonymous) "Firebase Anonymous Auth" else "Firebase Email Auth"
        )
    }

    /**
     * Signs in a user with Email and Password.
     */
    fun signInWithEmail(email: String, pass: String) {
        _authError.value = null
        if (email.isBlank() || pass.isBlank()) {
            _authError.value = "Email and password cannot be empty."
            return
        }
        if (!email.contains("@")) {
            _authError.value = "Invalid email format."
            return
        }
        if (pass.length < 6) {
            _authError.value = "Password must be at least 6 characters."
            return
        }

        try {
            auth?.signInWithEmailAndPassword(email.trim(), pass)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let { firebaseUser ->
                        _currentUser.value = toBaasUser(firebaseUser)
                    }
                } else {
                    _authError.value = task.exception?.message ?: "Authentication failed."
                }
            } ?: run {
                _authError.value = "Firebase not available"
            }
        } catch (e: Exception) {
            _authError.value = e.message ?: "Firebase not available"
        }
    }

    /**
     * Registers a new user.
     */
    fun registerWithEmail(email: String, pass: String) {
        _authError.value = null
        if (email.isBlank() || pass.isBlank()) {
            _authError.value = "Email and password cannot be empty."
            return
        }
        if (!email.contains("@")) {
            _authError.value = "Invalid email format."
            return
        }
        if (pass.length < 6) {
            _authError.value = "Password must be at least 6 characters."
            return
        }

        try {
            auth?.createUserWithEmailAndPassword(email.trim(), pass)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let { firebaseUser ->
                        _currentUser.value = toBaasUser(firebaseUser)
                    }
                } else {
                    _authError.value = task.exception?.message ?: "Registration failed."
                }
            } ?: run {
                _authError.value = "Firebase not available"
            }
        } catch (e: Exception) {
            _authError.value = e.message ?: "Firebase not available"
        }
    }

    /**
     * Signs in anonymously (common BaaS feature).
     */
    fun signInAnonymously() {
        _authError.value = null
        try {
            auth?.signInAnonymously()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let { firebaseUser ->
                        _currentUser.value = toBaasUser(firebaseUser)
                    }
                } else {
                    _authError.value = task.exception?.message ?: "Anonymous sign-in failed."
                }
            } ?: run {
                _authError.value = "Firebase not available"
            }
        } catch (e: Exception) {
            _authError.value = e.message ?: "Firebase not available"
        }
    }

    /**
     * Signs out current user.
     */
    fun signOut() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            // Ignore in unit tests
        }
        _currentUser.value = null
        _authError.value = null
    }

    fun clearError() {
        _authError.value = null
    }
}
