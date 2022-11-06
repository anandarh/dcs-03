package com.anandarh.storyapp.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anandarh.storyapp.R
import com.anandarh.storyapp.databinding.ActivityMapsBinding
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.ui.components.ImageMarker
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.viewmodels.MapStoriesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel by viewModels<MapStoriesViewModel>()

    private val boundsBuilder = LatLngBounds.Builder()
    private val imageMarker = ImageMarker()

    companion object {
        private const val TAG = "Story App"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Move the camera to bandung
        val bandung = LatLng(-6.905977, 107.613144)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bandung, 8f))

        getMyLocation()
        setMapStyle()
        subscribeUI()
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun subscribeUI() {
        viewModel.storiesWithLocation.observe(this) { result ->
            when (result) {
                is DataState.Loading -> handleLoading(true)
                is DataState.Success -> handleSuccess(result.data.listStory)
                is DataState.Error -> handleError(result.exception)
            }
        }

        viewModel.fetchStoriesWithLocation()
    }

    private fun handleLoading(show: Boolean) {
        if (show) binding.llLoading.visibility = View.VISIBLE
        else binding.llLoading.visibility = View.GONE
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun handleSuccess(data: ArrayList<StoryModel>?) {
        handleLoading(false)

        data?.distinctBy { it.name }?.forEach { story ->
            val latLng = LatLng(story.lat!!, story.lon!!)
            imageMarker.addMarker(this, story.photoUrl, latLng, mMap)
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                100
            )
        )

        mMap.setOnMarkerClickListener { marker ->
            val story = data?.first {
                LatLng(it.lat!!, it.lon!!) == marker.tag
            }

            if (story != null) {
                val intent = Intent(this, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, story)
                startActivity(intent)
            }

            false
        }
    }

    private fun handleError(exception: Exception) {
        handleLoading(false)
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
    }

}