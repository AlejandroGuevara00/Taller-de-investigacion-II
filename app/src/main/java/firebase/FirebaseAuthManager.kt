package com.prototipo.alertacusco.firebase

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()

    fun registerUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error desconocido")
            }
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error desconocido")
            }
    }

    fun logout() {
        auth.signOut()
    }
}