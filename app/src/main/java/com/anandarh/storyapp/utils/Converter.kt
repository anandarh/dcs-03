package com.anandarh.storyapp.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.anandarh.storyapp.models.ResponseModel
import com.google.gson.Gson
import java.util.*


fun exceptionResponse(exception: Exception?): ResponseModel {
    return Gson().fromJson(exception?.message.toString(), ResponseModel::class.java)
}

fun dpsToPixels(context: Context, dps: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (dps * scale + 0.5f).toInt()
}

@Suppress("DEPRECATION")
fun addressFromCoordinate(
    context: Context,
    latitude: Double,
    longitude: Double,
    formatted: Boolean = false
): String {
    val addresses: List<Address>?
    val geocoder = Geocoder(context, Locale.getDefault())

    addresses = geocoder.getFromLocation(
        latitude,
        longitude,
        1
    )

    val address: String =
        addresses!![0].getAddressLine(0)

    val subLocality: String = addresses[0].subLocality ?: ""
    val locality: String = addresses[0].locality ?: ""
    val subAdminArea: String = addresses[0].subAdminArea ?: ""
    val adminArea: String = addresses[0].adminArea ?: ""
    val countryName: String = addresses[0].countryName ?: ""

    return if (formatted) "$subLocality, $locality, $subAdminArea, $adminArea, $countryName" else address
}