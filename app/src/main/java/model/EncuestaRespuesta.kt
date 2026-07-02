package com.prototipo.alertacusco.model

data class EncuestaRespuesta(
    val id: String = "",

    val usuarioId: String = "",
    val correoUsuario: String = "",

    val fecha: String = "",
    val hora: String = "",

    val af1: Int = 0,
    val af2: Int = 0,

    val fi1: Int = 0,
    val fi2: Int = 0,

    val us1: Int = 0,
    val us2: Int = 0,

    val se1: Int = 0,
    val se2: Int = 0,

    val ed1: Int = 0,
    val ed2: Int = 0,

    val adecuacionFuncional: Double = 0.0,
    val fiabilidad: Double = 0.0,
    val usabilidad: Double = 0.0,
    val seguridad: Double = 0.0,
    val eficienciaDesempeno: Double = 0.0,

    val total: Int = 0,
    val promedio: Double = 0.0
)