package com.prototipo.alertacusco.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prototipo.alertacusco.screens.*
import com.prototipo.alertacusco.screens.EncuestaScreen
import com.prototipo.alertacusco.screens.AdminEncuestasScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("report") {
            ReportScreen(navController)
        }

        composable("alerts") {
            AlertScreen(navController)
        }

        composable("profile") {
            ProfileScreen(navController)
        }

        composable("historial") {
            HistorialScreen(navController)
        }

        composable("dashboard") {
            DashboardScreen(navController)
        }

        composable("mapa") {
            MapaScreen(navController)
        }

        composable("sos") {
            SosScreen(navController)
        }

        composable("zonas") {
            ZonaRiesgoScreen(navController)
        }

        composable("mis_reportes") {
            MisReportesScreen(navController)
        }

        composable("contacto_emergencia") {
            ContactoEmergenciaScreen(navController)
        }

        composable("alertas_cercanas") {
            AlertasCercanasScreen(navController)
        }

        composable("detalle_reporte/{reporteId}") { backStackEntry ->

            val reporteId =
                backStackEntry.arguments
                    ?.getString("reporteId") ?: ""

            DetalleReporteScreen(
                navController = navController,
                reporteId = reporteId
            )
        }

        composable("admin_reportes") {
            AdminReportesScreen(navController)
        }

        composable("ranking_zonas") {
            RankingZonasScreen(navController)
        }

        composable("admin_detalle_reporte/{reporteId}") { backStackEntry ->

            val reporteId =
                backStackEntry.arguments
                    ?.getString("reporteId") ?: ""

            AdminDetalleReporteScreen(
                navController = navController,
                reporteId = reporteId
            )
        }

        composable("encuesta") {
            EncuestaScreen(navController)
        }

        composable("admin_encuestas") {
            AdminEncuestasScreen(navController)
        }
    }
}
