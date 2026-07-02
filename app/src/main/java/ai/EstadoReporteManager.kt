package com.prototipo.alertacusco.ai

object EstadoReporteManager {

    fun obtenerIconoEstado(estado: String): String {
        return when (estado) {
            "Pendiente" -> "⏳"
            "En revisión" -> "🔎"
            "Atendido" -> "✅"
            "Descartado" -> "❌"
            else -> "⏳"
        }
    }

    fun obtenerDescripcionEstado(estado: String): String {
        return when (estado) {
            "Pendiente" -> "El reporte fue registrado y está esperando revisión."
            "En revisión" -> "El reporte está siendo evaluado."
            "Atendido" -> "El reporte fue atendido o considerado en las acciones preventivas."
            "Descartado" -> "El reporte fue descartado por falta de información o inconsistencia."
            else -> "El reporte se encuentra pendiente de revisión."
        }
    }
}