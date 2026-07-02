package com.prototipo.alertacusco.location

import android.content.Context
import android.location.Geocoder
import java.util.Locale

class GeocodingManager(
    private val context: Context
) {

    fun obtenerDistrito(
        latitud: Double,
        longitud: Double
    ): String {

        return try {

            val geocoder = Geocoder(
                context,
                Locale("es", "PE")
            )

            val direcciones =
                geocoder.getFromLocation(
                    latitud,
                    longitud,
                    1
                )

            if (!direcciones.isNullOrEmpty()) {

                direcciones[0].subAdminArea
                    ?: direcciones[0].locality
                    ?: "Distrito desconocido"

            } else {

                "Distrito desconocido"
            }

        } catch (e: Exception) {

            "Distrito desconocido"
        }
    }
}