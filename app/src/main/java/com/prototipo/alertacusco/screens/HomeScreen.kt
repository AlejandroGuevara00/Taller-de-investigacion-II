package com.prototipo.alertacusco.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.prototipo.alertacusco.firebase.AdminManager
import com.prototipo.alertacusco.firebase.FirebaseAuthManager
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaMetricRow
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.AlertaSecondaryButton
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val authManager = FirebaseAuthManager()
    val firestoreManager = FirestoreManager()
    val esAdmin = AdminManager.esAdministrador()
    val isDark = isSystemInDarkTheme()

    val usuario =
        FirebaseAuth.getInstance()
            .currentUser

    var reportes by remember {
        mutableStateOf<List<Reporte>>(emptyList())
    }

    LaunchedEffect(Unit) {
        firestoreManager.obtenerReportes(
            onSuccess = {
                reportes = it
            },
            onFailure = { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    val totalReportes =
        reportes.size

    val reportesAlto =
        reportes.count {
            it.nivelRiesgo == "Alto"
        }

    val pendientes =
        reportes.count {
            it.estado == "Pendiente"
        }

    val distritoMasReportado =
        reportes
            .filter {
                it.distrito.isNotBlank()
            }
            .groupBy {
                it.distrito
            }
            .maxByOrNull {
                it.value.size
            }
            ?.key ?: "Sin datos"

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
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {

        HeaderHomeFinal(
            correo = usuario?.email ?: "Usuario"
        )

        Spacer(modifier = Modifier.height(18.dp))

        AlertaCard {

            Text(
                text = "Resumen preventivo",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(14.dp))

            AlertaMetricRow(
                titulo = "Reportes registrados",
                valor = totalReportes.toString(),
                icono = "📊"
            )

            Spacer(modifier = Modifier.height(12.dp))

            AlertaMetricRow(
                titulo = "Riesgo alto",
                valor = reportesAlto.toString(),
                icono = "🔴"
            )

            Spacer(modifier = Modifier.height(12.dp))

            AlertaMetricRow(
                titulo = "Pendientes",
                valor = pendientes.toString(),
                icono = "⏳"
            )

            Spacer(modifier = Modifier.height(12.dp))

            AlertaMetricRow(
                titulo = "Zona más reportada",
                valor = distritoMasReportado,
                icono = "📍"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Emergencia",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Acciones rápidas para situaciones de riesgo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.72f
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            AlertaPrimaryButton(
                text = "SOS de Emergencia"
            ) {
                navController.navigate("sos")
            }

            Spacer(modifier = Modifier.height(10.dp))

            AlertaSecondaryButton(
                text = "Contacto de Emergencia"
            ) {
                navController.navigate("contacto_emergencia")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Reportes",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Registra incidentes y consulta tus reportes.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.72f
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            AlertaPrimaryButton(
                text = "Reportar Incidente"
            ) {
                navController.navigate("report")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                AlertaSecondaryButton(
                    text = "Mis Reportes",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("mis_reportes")
                }

                AlertaSecondaryButton(
                    text = "Historial",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("historial")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Prevención inteligente",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Consulta alertas, zonas de riesgo, mapa y estadísticas.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.72f
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            AlertaPrimaryButton(
                text = "Alertas Cercanas"
            ) {
                navController.navigate("alertas_cercanas")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                AlertaSecondaryButton(
                    text = "Alertas",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("alerts")
                }

                AlertaSecondaryButton(
                    text = "Zonas",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("zonas")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                AlertaSecondaryButton(
                    text = "Ranking",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("ranking_zonas")
                }

                AlertaSecondaryButton(
                    text = "Mapa",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("mapa")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AlertaSecondaryButton(
                text = "Dashboard Estadístico"
            ) {
                navController.navigate("dashboard")
            }
        }

        if (esAdmin) {

            Spacer(modifier = Modifier.height(16.dp))

            AlertaCard {

                Text(
                    text = "Administración",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Gestión de reportes ciudadanos, estados de atención y encuestas del sistema.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                AlertaPrimaryButton(
                    text = "Panel Administrador"
                ) {
                    navController.navigate("admin_reportes")
                }

                Spacer(modifier = Modifier.height(10.dp))

                AlertaSecondaryButton(
                    text = "Resultados de encuestas"
                ) {
                    navController.navigate("admin_encuestas")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Cuenta",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            AlertaSecondaryButton(
                text = "Mi Perfil"
            ) {
                navController.navigate("profile")
            }

            Spacer(modifier = Modifier.height(10.dp))

            AlertaSecondaryButton(
                text = "Encuesta del sistema"
            ) {
                navController.navigate("encuesta")
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                    text = "Cerrar Sesión",
                    color = AlertaRed
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun HeaderHomeFinal(
    correo: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.14f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🛡️",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Alerta Cusco",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = correo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.68f
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    AlertaCard {

        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Reporta incidentes, consulta zonas de riesgo y recibe alertas preventivas en tiempo real.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.72f
            )
        )
    }
}