package com.anandarh.storyapp.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


class ImageMarker {
    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())

    fun addMarker(context: Context, imageUrl: String, loc: LatLng, map: GoogleMap) {
        myExecutor.execute {
            val result = getBitmapFromURL(imageUrl)
            myHandler.post {
                val imageView = ImageView(context)
                val iconGenerator = IconGenerator(context)
                val layoutParam = LinearLayout.LayoutParams(180, 180)
                imageView.layoutParams = layoutParam
                imageView.adjustViewBounds = true
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.setImageBitmap(result)
                iconGenerator.setContentView(imageView)

                val markerOptions = MarkerOptions().position(loc)
                    .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))
                val marker = map.addMarker(markerOptions)
                marker?.tag = loc
            }
        }
    }

    private fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url
                .openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}