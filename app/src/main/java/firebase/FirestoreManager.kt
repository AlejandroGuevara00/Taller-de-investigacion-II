package com.prototipo.alertacusco.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.model.EncuestaRespuesta

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()

    fun guardarReporte(
        reporte: Reporte,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        db.collection("reportes")
            .add(reporte)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error")
            }
    }

    fun obtenerReportes(
        onSuccess: (List<Reporte>) -> Unit,
        onFailure: (String) -> Unit
    ) {

        db.collection("reportes")
            .get()
            .addOnSuccessListener { resultado ->

                val lista = resultado.documents.mapNotNull { documento ->
                    documento.toObject(Reporte::class.java)?.copy(
                        id = documento.id
                    )
                }

                onSuccess(lista)
            }
            .addOnFailureListener {

                onFailure(
                    it.message ?: "Error"
                )
            }
    }

    fun obtenerAlertas(
        onSuccess: (List<Reporte>) -> Unit,
        onFailure: (String) -> Unit
    ) {

        db.collection("reportes")
            .orderBy("fecha")
            .get()
            .addOnSuccessListener { resultado ->

                val lista =
                    resultado.documents.mapNotNull {

                        it.toObject(
                            Reporte::class.java
                        )
                    }

                onSuccess(lista)
            }
            .addOnFailureListener {

                onFailure(
                    it.message ?: "Error"
                )
            }
    }

    fun obtenerReportesPorUsuario(
        usuarioId: String,
        onSuccess: (List<Reporte>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("reportes")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .addOnSuccessListener { resultado ->
                val lista =
                    resultado.documents.mapNotNull { documento ->
                        documento.toObject(Reporte::class.java)?.copy(
                            id = documento.id
                        )
                    }
                onSuccess(lista)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error")
            }
    }

    fun obtenerReportePorId(
        reporteId: String,
        onSuccess: (Reporte?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("reportes")
            .document(reporteId)
            .get()
            .addOnSuccessListener { documento ->

                val reporte =
                    documento.toObject(Reporte::class.java)?.copy(
                        id = documento.id
                    )

                onSuccess(reporte)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error")
            }
    }

    fun actualizarEstadoReporte(
        reporteId: String,
        nuevoEstado: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("reportes")
            .document(reporteId)
            .update("estado", nuevoEstado)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error actualizando estado")
            }
    }

    fun guardarEncuestaRespuesta(
        respuesta: EncuestaRespuesta,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("encuestas")
            .add(respuesta)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error al guardar encuesta")
            }
    }

    fun obtenerEncuestas(
        onSuccess: (List<EncuestaRespuesta>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("encuestas")
            .get()
            .addOnSuccessListener { resultado ->

                val lista =
                    resultado.documents.mapNotNull { documento ->
                        documento.toObject(EncuestaRespuesta::class.java)
                            ?.copy(id = documento.id)
                    }

                onSuccess(lista)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error al obtener encuestas")
            }
    }

}