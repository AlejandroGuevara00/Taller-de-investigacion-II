package com.prototipo.alertacusco.model

data class Reporte(
    val id: String = "",
    val tipoIncidente: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val hora: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val distrito: String = "",
    val nivelRiesgo: String = "",
    val usuarioId: String = "",
    val evidenciaFotografica: Boolean = false,
    val fotoUrl: String = "",
    val estado: String = "Pendiente"
)