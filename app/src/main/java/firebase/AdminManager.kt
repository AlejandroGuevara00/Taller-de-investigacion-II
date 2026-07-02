package com.prototipo.alertacusco.firebase

import com.google.firebase.auth.FirebaseAuth

object AdminManager {

    private val correosAdmin = listOf(
        "72697573@continental.edu.pe"
    )

    fun esAdministrador(): Boolean {
        val correoUsuario =
            FirebaseAuth.getInstance()
                .currentUser
                ?.email ?: ""

        return correosAdmin.contains(correoUsuario)
    }
}