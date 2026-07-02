package com.prototipo.alertacusco.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.prototipo.alertacusco.firebase.AdminManager
import com.prototipo.alertacusco.firebase.FirebaseAuthManager
import com.prototipo.alertacusco.ui.theme.AlertaBlue
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.AlertaSecondaryButton
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val authManager = FirebaseAuthManager()
    val usuario = FirebaseAuth.getInstance().currentUser
    val esAdmin = AdminManager.esAdministrador()
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) {
                    DarkBackground
                } else {
                    LightBackground
                }
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Información de tu cuenta en Alerta Cusco.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        AlertaCard {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(
                            AlertaBlue.copy(
                                alpha = 0.16f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = usuario?.email ?: "Usuario no identificado",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (esAdmin) {
                        "Rol: Administrador"
                    } else {
                        "Rol: Ciudadano"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Opciones de cuenta",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            AlertaSecondaryButton(
                text = "Mis reportes"
            ) {
                navController.navigate("mis_reportes")
            }

            Spacer(modifier = Modifier.height(10.dp))

            AlertaSecondaryButton(
                text = "Contacto de emergencia"
            ) {
                navController.navigate("contacto_emergencia")
            }

            if (esAdmin) {

                Spacer(modifier = Modifier.height(10.dp))

                AlertaPrimaryButton(
                    text = "Panel administrador"
                ) {
                    navController.navigate("admin_reportes")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Sesión",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Puedes cerrar sesión y volver a ingresar con otra cuenta.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.72f
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            TextButton(
                onClick = {
                    authManager.logout()

                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Cerrar sesión",
                    color = AlertaRed
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}