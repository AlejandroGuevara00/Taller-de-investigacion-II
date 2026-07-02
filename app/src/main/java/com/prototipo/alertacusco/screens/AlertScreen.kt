package com.prototipo.alertacusco.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prototipo.alertacusco.alerts.AlertaManager
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.Alerta

@Composable
fun AlertScreen(
    navController: NavController
) {

    val firestoreManager = FirestoreManager()

    var alertas by remember {
        mutableStateOf<List<Alerta>>(emptyList())
    }

    LaunchedEffect(Unit) {

        firestoreManager.obtenerReportes(

            onSuccess = { reportes ->

                println("REPORTES ENCONTRADOS: ${reportes.size}")

                reportes.forEach {
                    println("INCIDENTE: ${it.tipoIncidente}")
                    println("RIESGO: ${it.nivelRiesgo}")
                }

                alertas =
                    reportes.map {
                        AlertaManager.generarAlerta(it)
                    }
            },

            onFailure = {

            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Alertas Inteligentes",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Cantidad de alertas: ${alertas.size}"
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {

            items(alertas) { alerta ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = alerta.titulo,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = alerta.mensaje
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        TextButton(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("Volver")
        }
    }
}