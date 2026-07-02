package com.prototipo.alertacusco.location

import kotlin.math.*

object DistanciaManager {

    fun calcularDistanciaMetros(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {

        val radioTierra = 6371000.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)

        val a =
            sin(dLat / 2).pow(2.0) +
                    cos(lat1Rad) *
                    cos(lat2Rad) *
                    sin(dLon / 2).pow(2.0)

        val c =
            2 * atan2(
                sqrt(a),
                sqrt(1 - a)
            )

        return radioTierra * c
    }
}