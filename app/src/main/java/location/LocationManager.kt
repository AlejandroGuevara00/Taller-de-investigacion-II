package com.prototipo.alertacusco.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationManager(
    private val context: Context
) {

    @SuppressLint("MissingPermission")
    fun obtenerUbicacion(
        onSuccess: (Double, Double) -> Unit,
        onFailure: (String) -> Unit
    ) {

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        )
            .addOnSuccessListener { location ->

                if (location != null) {

                    onSuccess(
                        location.latitude,
                        location.longitude
                    )

                } else {

                    onFailure(
                        "No se pudo obtener ubicación actual"
                    )
                }
            }
            .addOnFailureListener {

                onFailure(
                    it.message ?: "Error GPS"
                )
            }
    }
}